package com.valley.yueojcodesandbox.sevice.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.valley.yueojcodesandbox.docker.MemoryCollector;
import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteMessage;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import com.valley.yueojcodesandbox.model.enums.ExecuteStatusEnum;
import com.valley.yueojcodesandbox.sevice.CodeSandboxService;

import java.io.File;
import java.util.List;
import java.util.UUID;


public abstract class AbstractDockerCodeSandboxService implements CodeSandboxService {
    /**
     * 保存代码文件的目录
     */
    protected static String SAVE_CODE_ROOT_PATH;
    /**
     * 程序执行超时时间
     */
    final protected long TIMEOUT = 5000L;

    /**
     * Docker客户端
     */
    final protected static DockerClient mDockerClient = DockerClientBuilder
            .getInstance("tcp://127.0.0.1:2375")
            .build();
    /**
     * Docker容器ID
     */
    protected static String mContainerId;

    final protected static WordTree mWordTree = new WordTree();

    /**
     * 检查代码中操作的合法性
     * @param code
     * @return
     */
    public boolean checkCodeLegality(String code) {
        // 校验代码的安全性
        FoundWord foundWord = mWordTree.matchWord(code);
        return foundWord == null;
    }

    public abstract File saveCodeToFile(String code, String codeParentDirName);

    public abstract ExecuteMessage compile(String codeParentDirName);

    public abstract ExecuteResponse runInputCases(List<String> inputs, String classPath, MemoryCollector memoryCollector);

    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        List<String> inputs = request.getInputs();
        String code = request.getCode();
        // 1. 检查代码合法性
        if (!checkCodeLegality(code)) {
            ExecuteResponse response = new ExecuteResponse();
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
        String userCodeParentDirName = UUID.randomUUID().toString();
        File codeFile = saveCodeToFile(code, userCodeParentDirName);
        ExecuteMessage compileOutput = compile(userCodeParentDirName);
        if (compileOutput.getExitValue() != 0) {
            ExecuteResponse response = new ExecuteResponse();
            response.setMessage(compileOutput.getError());
            // 返回错误状态
            response.setStatus(ExecuteStatusEnum.COMPILE_ERROR.getValue());
            return response;
        }
        // 3. 编译成功后，执行代码，得到输出结果
        ExecuteResponse response = runInputCases(inputs, userCodeParentDirName, memoryCollector);
        // 关闭内存监听
        memoryCollector.close();
        // 4. 文件清理
        if (codeFile.getParentFile() != null) {
            FileUtil.del(codeFile.getParentFile());
        }
        return response;
    }
}
