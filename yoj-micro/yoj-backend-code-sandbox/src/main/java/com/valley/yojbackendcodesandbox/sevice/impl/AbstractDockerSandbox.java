package com.valley.yojbackendcodesandbox.sevice.impl;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.valley.yojbackendcodesandbox.docker.MemoryCollector;
import com.valley.yojbackendcodesandbox.sevice.CodeSandboxService;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteMessage;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import com.valley.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.valley.yojbackendmodel.model.codesandbox.enums.ExecuteStatusEnum;

import java.util.List;
import java.util.UUID;


public abstract class AbstractDockerSandbox implements CodeSandboxService {
    /**
     * 保存代码文件的目录
     */
    protected static String SAVE_CODE_ROOT_PATH = "/app";
    /**
     * 程序执行超时时间
     */
    final protected long TIMEOUT = 5000L;

    /**
     * Docker客户端
     */
    final protected static DockerClient mDockerClient = DockerClientBuilder
            .getInstance("tcp://192.168.0.110:2375")
            .build();
    /**
     * Docker容器ID
     */
    protected static String mContainerId;

    final protected static WordTree mWordTree = new WordTree();

    /**
     * 检查代码中操作的合法性
     * @param code 用户代码
     * @return
     */
    public boolean checkCodeLegality(String code) {
        // 校验代码的安全性
        FoundWord foundWord = mWordTree.matchWord(code);
        return foundWord == null;
    }

    /**
     * 将代码保存到文件中，便于后续执行编译
     * @param code 代码
     * @param dirName 保存代码的文件夹名称
     * @return 容器中的代码文件夹全路径
     */
    public abstract String saveCodeToFile(String code, String dirName);

    public abstract ExecuteMessage compile(String codeDirPath);

    public abstract ExecuteResponse runInputCases(List<String> inputs, String classPath, MemoryCollector memoryCollector);

    public abstract void deleteCodeFile(String codeDirPath);

    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        List<String> inputs = request.getInputs();
        String code = request.getCode();

        ExecuteResponse response = new ExecuteResponse();
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage("");
        judgeInfo.setTime(0L);
        judgeInfo.setMemory(0L);
        response.setJudgeInfo(judgeInfo);
        // 1. 检查代码合法性
        if (!checkCodeLegality(code)) {
            response.setMessage(ExecuteStatusEnum.ILLEGAL_OPERATION.getText());
            response.setStatus(ExecuteStatusEnum.ILLEGAL_OPERATION.getValue());
            return response;
        }
        // 等待容器创建结束
        while (true) {
            if (mContainerId != null) {
                break;
            }
        }
        // 开启容器内存监听
        MemoryCollector memoryCollector = mDockerClient.statsCmd(mContainerId).exec(new MemoryCollector());
        // 2. 保存并编译用户代码
        // 当前用户上传代码的文件夹名称
        String codeDirName = UUID.randomUUID().toString();
        String codeDirPath = saveCodeToFile(code, codeDirName);
        ExecuteMessage compileOutput = compile(codeDirPath);
        if (compileOutput.getExitValue() != 0) {
            response.setMessage(compileOutput.getError());
            // 返回错误状态
            response.setStatus(ExecuteStatusEnum.COMPILE_ERROR.getValue());
            deleteCodeFile(codeDirPath);
            return response;
        }
        // 3. 编译成功后，执行代码，得到输出结果
        response = runInputCases(inputs, codeDirPath, memoryCollector);
        // 关闭内存监听
        memoryCollector.close();
        // 4. 文件清理
        deleteCodeFile(codeDirPath);
        return response;
    }
}
