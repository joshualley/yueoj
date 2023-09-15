package com.valley.yueojcodesandbox.sevice.impl;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.valley.yueojcodesandbox.docker.MemoryCollector;
import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteMessage;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import com.valley.yueojcodesandbox.model.JudgeInfo;
import com.valley.yueojcodesandbox.sevice.CodeSandbox;
import com.valley.yueojcodesandbox.utils.ProcessUtil;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Docker实现的java代码沙箱
 */
@Service("docker")
public class JavaDockerCodeSandbox implements CodeSandbox {

    /**
     * 保持存java代码文件的更目录
     */
    final private static String CODE_PATH_NAME = "temp" + File.separator + "java";
    /**
     * java代码的默认类名
     */
    final private String CLASS_NAME = "Main";
    final private String FILE_SUFFIX = ".java";
    /**
     * 自定义的安全管理器的相对路径
     */
    final private String SECURITY_MANAGER_CLASS_PATH =
            "src" + File.separator
            + "main" + File.separator
            + "resources" + File.separator
            + "security";

    /**
     * 程序执行超时时间
     */
    final private long TIMEOUT = 5000L;

    /**
     * Docker客户端
     */
    final private static DockerClient mDockerClient = DockerClientBuilder
            .getInstance("tcp://127.0.0.1:2375")
            .build();
    /**
     * Docker容器ID
     */
    private static String mContainerId;

    final private static WordTree mWordTree = new WordTree();

    static {
        // 添加程序执行时，禁用的操作
        mWordTree.addWords(Arrays.asList(
                "Files",
                "RunTime",
                "exec"
        ));

        // 进行容器初始化
        String containerName = "java8";
        List<Image> images = mDockerClient.listImagesCmd().exec();
        List<String> imageNames = images.stream()
                .map((image) -> String.join(" ", image.getRepoTags()))
                .collect(Collectors.toList());

        String image = "openjdk:8-alpine";
        // 1. 如果镜像不存在，拉取java镜像
        if (!imageNames.contains(image)) {
            try {
                mDockerClient.pullImageCmd(image).exec(new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        super.onNext(item);
                        System.out.println(item.getProgressDetail());
                    }
                }).awaitCompletion();
            } catch (InterruptedException ignored) {
                System.out.println("拉取镜像异常");
                throw new RuntimeException();
            }
        }

        // 2. 如果容器不存在，创建容器
        List<Container> containers = mDockerClient.listContainersCmd().exec();
        boolean existsContainer = false;
        for (Container container : containers) {
            // 如果存在容器，则获取容器ID
            if (String.join(" ", container.getNames()).contains(containerName)) {
                existsContainer = true;
                mContainerId = container.getId();
                break;
            }
        }
        if (!existsContainer) {
            String userDir = System.getProperty("user.dir");
            String codeDir = userDir + File.separator + CODE_PATH_NAME;
            HostConfig hostConfig = new HostConfig();
            // 将用户代码的路径映射到docker容器中的 /app 目录下
            hostConfig.setBinds(new Bind(codeDir, new Volume("/app")));
            // 限制容器内存 1000 M
            hostConfig.withMemory(1000 * 1024 * 1024L);
            // 限制仅能使用单核cpu
            hostConfig.withCpuCount(1L);
            CreateContainerResponse containerResponse = mDockerClient.createContainerCmd(image)
                    .withName(containerName)
                    .withHostConfig(hostConfig)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .withTty(true) // 创建可交互的终端
                    .exec();
            // 获取容器ID
            String containerId = containerResponse.getId();
            // 3. 启动容器
            mDockerClient.startContainerCmd(containerId).exec();
            mContainerId = containerId;
        }
    }

    private ExecuteMessage execDockerRunCmd(String[] cmd) {
        ExecCreateCmdResponse execCreateCmdResponse = mDockerClient.execCreateCmd(mContainerId)
                .withCmd(cmd)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        String cmdId = execCreateCmdResponse.getId();

        ExecuteMessage runOutput = ExecuteMessage.builder()
                .time(0L)
                .memory(0L)
                .exitValue(0)
                .build();
        // 创建计时器
        StopWatch stopWatch = new StopWatch();
        try {
            ExecStartCmd execStartCmd = mDockerClient.execStartCmd(cmdId);
            Thread.sleep(1000);
            stopWatch.start();
            execStartCmd.exec(new ExecStartResultCallback() {
                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    String output = new String(frame.getPayload());
                    switch (streamType) {
                        case STDIN:
                            break;
                        case STDOUT:
                            System.out.println("输出结果：" + output);
                            runOutput.setOutput(output.trim());
                            break;
                        case STDERR:
                            System.out.println("错误输出：" + output);
                            runOutput.setError(output);
                            runOutput.setExitValue(-1);
                            break;
                    }
                    super.onNext(frame);
                }
            }).awaitCompletion();
            stopWatch.stop();
        } catch (InterruptedException e) {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            String msg = "Docker容器中执行命令时发生异常: " + e.getMessage();
            System.out.println(msg);
            runOutput.setExitValue(-1);
            runOutput.setError(msg);
        }
        runOutput.setTime(stopWatch.getLastTaskTimeMillis());
        return runOutput;
    }

    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        List<String> inputs = request.getInputs();
        String code = request.getCode();
        String language = request.getLanguage();

        // 校验代码的安全性
        FoundWord foundWord = mWordTree.matchWord(code);
        if (foundWord != null) {
            ExecuteResponse response = new ExecuteResponse();
            response.setMessage("代码中存在非法操作：\n" + foundWord.getFoundWord());
            // 返回错误状态
            response.setStatus(2);
            return response;
        }
        // 1. 将用户代码保存到文件中
        String userDir = System.getProperty("user.dir");
        String codeDir = userDir + File.separator + CODE_PATH_NAME;
        // 判断存放代码的目录是否存在
        if (!FileUtil.exist(codeDir)) {
            FileUtil.mkdir(codeDir);
        }
        // 当前用户上传代码的文件夹名称
        String userCodeParentDirName = UUID.randomUUID().toString();
        String userCodeParentPath = codeDir + File.separator + userCodeParentDirName;
        // 用户代码文件的路径
        String userCodePath = userCodeParentPath + File.separator + CLASS_NAME + FILE_SUFFIX;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        // 2. 编译代码，得到class文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        ExecuteMessage compileOutput = ProcessUtil.runCmd(compileCmd, 0);
        if (compileOutput.getExitValue() != 0) {
            ExecuteResponse response = new ExecuteResponse();
            response.setMessage("编译失败：\n" + compileOutput.getError());
            // 返回错误状态
            response.setStatus(2);
            return response;
        }
        // 3. 编译成功后，执行代码，得到输出结果
        // 等待容器创建结束
        while (true) {
            if (mContainerId != null) {
                break;
            }
        }
        // 监听容器内存
        MemoryCollector memoryCollector = mDockerClient.statsCmd(mContainerId)
                .exec(new MemoryCollector());
        List<String> outputs = new ArrayList<>();
        boolean existsError = false;
        // 程序最大运行时间
        Long maxTime = 0L;
        Long maxMemory = 0L;
        for (String input : inputs) {
            // 传递执行java程序的命令
            // docker exec java8 java -cp /app Main [输入的参数，如: 1 3]
            String[] cmd = {"java", "-cp", "/app/" + userCodeParentDirName, CLASS_NAME};
            // 将参数按空格拆分
            cmd = ArrayUtil.append(cmd, input.split(" "));
            ExecuteMessage runOutput = execDockerRunCmd(cmd);
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
        // 关闭内存监听
        memoryCollector.close();
        // 4. 文件清理
        if (userCodeFile.getParentFile() != null) {
            FileUtil.del(userCodeParentPath);
        }
        // 5. 返回结果
        ExecuteResponse response = new ExecuteResponse();
        response.setOutputs(outputs);
        if (existsError) {
            // 返回失败状态
            response.setStatus(3);
            response.setMessage("存在错误");
        } else  {
            // 返回成功状态
            response.setStatus(1);
            response.setMessage("执行成功");
        }

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory);
        response.setJudgeInfo(judgeInfo);

        return response;
    }
}
