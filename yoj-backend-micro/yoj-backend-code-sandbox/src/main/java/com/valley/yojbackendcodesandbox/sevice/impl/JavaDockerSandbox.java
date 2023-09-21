package com.valley.yojbackendcodesandbox.sevice.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.valley.yojbackendcodesandbox.docker.MemoryCollector;
import com.valley.yojbackendcodesandbox.docker.DockerUtil;
import com.valley.yojbackendcommon.common.ErrorCode;
import com.valley.yojbackendcommon.exception.BusinessException;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteMessage;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import com.valley.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.valley.yojbackendmodel.model.codesandbox.enums.ExecuteStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Docker实现的java代码沙箱
 */
@Service("docker")
@Slf4j
public class JavaDockerSandbox extends AbstractDockerSandbox {
    /**
     * java代码的默认类名
     */
    final private String CLASS_NAME = "Main";

    static {
        // 添加程序执行时，禁用的操作
        mWordTree.addWords(Arrays.asList(
                "Files",
                "RunTime",
                "exec"
        ));

        // 初始化Docker容器
        String containerName = "java8";
        String imageName = "openjdk:8-alpine";
        mContainerId = DockerUtil.initDockerContainer(mDockerClient, imageName, containerName);
    }

    @Override
    public String saveCodeToFile(String code, String dirName) {
        // 1. 创建本地文件夹
        String codeLocDir = System.getProperty("user.dir") + File.separator + "temp" + File.separator + dirName;
        // 判断本地存放代码的目录是否存在
        if (!FileUtil.exist(codeLocDir)) {
            FileUtil.mkdir(codeLocDir);
        }
        // 写入本地代码文件
        String codeLocPath = codeLocDir + File.separator + CLASS_NAME + ".java";
        File codeFile = FileUtil.writeString(code, codeLocPath, StandardCharsets.UTF_8);
        // 2. 创建Docker文件夹，并将本地文件复制到docker
        String codeDirPath = SAVE_CODE_ROOT_PATH + "/" + dirName;
        // 用户代码文件的路径
        String[] mkdirCmd = { "mkdir", codeDirPath };
        log.info("创建代码文件夹：" + String.join(" ", mkdirCmd));
        ExecuteMessage message = DockerUtil.execRunCmd(mDockerClient, mContainerId, mkdirCmd, 0);
        if (message.getExitValue() != 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建代码文件夹失败: " + message.getOutput());
        }
        // 将本地代码文件复制到docker容器
        String codeLocFilePath = codeFile.getAbsolutePath();
        log.info("复制用户代码 {} 至容器 {}", codeLocFilePath, codeDirPath);
        boolean success = DockerUtil.copyFileToContainer(mDockerClient, mContainerId, codeLocFilePath, codeDirPath);
        // 删除本地文件
        if (FileUtil.exist(codeFile)) {
            FileUtil.del(codeFile.getParentFile());
        }
        if (!success) {
            deleteCodeFile(codeDirPath);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "复制用户代码至容器: " + message.getOutput());
        }
        return codeDirPath;
    }

    /**
     * 编译代码，得到class文件
     * @return
     */
    public ExecuteMessage compile(String codeDirPath) {
        String cmdStr = String.format("javac -encoding utf-8 %s/%s.java", codeDirPath, CLASS_NAME);
        log.info("编译用户代码：" + cmdStr);
        String[] cmd = cmdStr.split(" ");
        return DockerUtil.execRunCmd(mDockerClient, mContainerId, cmd, 0);
    }

    /**
     * 运行编译后的程序，执行输入用例，获取结果
     * @return
     */
    public ExecuteResponse runInputCases(List<String> inputs, String classPath, MemoryCollector memoryCollector) {
        List<String> outputs = new ArrayList<>();
        boolean existsError = false;
        // 程序最大运行时间
        Long maxTime = 0L;
        Long maxMemory = 0L;
        for (String input : inputs) {
            // 传递执行java程序的命令
            // docker exec java8 java -cp /app Main [输入的参数，如: 1 3]
            String[] cmd = {"java", "-cp", classPath, CLASS_NAME};
            // 将参数按空格拆分
            cmd = ArrayUtil.append(cmd, input.split(" "));
            log.info("运行用户代码: " + String.join(" ", cmd));
            ExecuteMessage runOutput = DockerUtil.execRunCmd(mDockerClient, mContainerId, cmd, TIMEOUT);
            runOutput.setMemory(memoryCollector.getMemoryUsage());
            if (runOutput.getExitValue() == 0) {
                outputs.add(runOutput.getOutput());
            } else {
                outputs.add(runOutput.getError());
                existsError = true;
            }
            // 获取最大时间
            if (runOutput.getTime() > maxTime) {
                maxTime = runOutput.getTime();
            }
            // 获取最大内存
            if (runOutput.getMemory() > maxMemory) {
                maxMemory = runOutput.getMemory();
            }
        }
        // 构造返回结果
        ExecuteResponse response = new ExecuteResponse();
        response.setOutputs(outputs);
        if (existsError) {
            // 返回失败状态
            response.setStatus(ExecuteStatusEnum.RUNNING_ERROR.getValue());
            response.setMessage(ExecuteStatusEnum.RUNNING_ERROR.getText());
        } else  {
            // 返回成功状态
            response.setStatus(ExecuteStatusEnum.ACCEPT.getValue());
            response.setMessage(ExecuteStatusEnum.ACCEPT.getText());
        }

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory);
        response.setJudgeInfo(judgeInfo);
        return response;
    }

    public void deleteCodeFile(String codeDirPath) {
        String cmdStr = String.format("rm -rf %s", codeDirPath);
        log.info("删除用户代码：" + cmdStr);
        String[] cmd = cmdStr.split(" ");
        DockerUtil.execRunCmd(mDockerClient, mContainerId, cmd, 0);
    }


}
