package com.valley.yueojcodesandbox.sevice.impl;

import cn.hutool.core.io.FileUtil;
import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteMessage;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import com.valley.yueojcodesandbox.model.JudgeInfo;
import com.valley.yueojcodesandbox.sevice.CodeSandbox;
import com.valley.yueojcodesandbox.utils.ProcessUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Java原生实现的java代码沙箱
 */
@Service
public class JavaNativeCodeSandbox implements CodeSandbox {

    /**
     * 保持存java代码文件的更目录
     */
    final private String CODE_PATH_NAME = "temp" + File.separator + "java";
    /**
     * java代码的默认类名
     */
    final private String CLASS_NAME = "Main";
    final private String FILE_SUFFIX = ".java";

    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {

        List<String> inputs = request.getInputs();
        String code = request.getCode();
        String language = request.getLanguage();

        // 1. 将用户代码保存到文件中
        String userDir = System.getProperty("user.dir");
        String codeDir = userDir + File.separator + CODE_PATH_NAME;
        // 判断存放代码的目录是否存在
        if (!FileUtil.exist(codeDir)) {
            FileUtil.mkdir(codeDir);
        }
        String userCodeParentPath = codeDir + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + CLASS_NAME + FILE_SUFFIX;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        // 2. 编译代码，得到class文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        ExecuteMessage compileOutput = ProcessUtil.runCmd(compileCmd);
        if (compileOutput.getExitValue() != 0) {
            ExecuteResponse response = new ExecuteResponse();
            response.setMessage("编译失败：\n" + compileOutput.getError());
            response.setStatus(0);
            return response;
        }
        // 3. 编译成功后，执行代码，得到输出结果
        List<String> outputs = new ArrayList<>();
        boolean existsError = false;
        for (String input : inputs) {
            String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s %s %s",
                    userCodeParentPath, CLASS_NAME, input);
            // System.out.println(runCmd);
            ExecuteMessage runOutput = ProcessUtil.runCmd(runCmd);
            // System.out.println("RUN:" + runOutput);
            if (runOutput.getExitValue() == 0) {
                outputs.add(runOutput.getOutput());
            } else {
                outputs.add(runOutput.getError());
                existsError = true;
            }
        }
        // 4. 文件清理
        FileUtil.del(userCodeParentPath);
        // 5. 错误处理，提升程序健壮性
        ExecuteResponse response = new ExecuteResponse();
        response.setOutputs(outputs);
        if (existsError) {
            response.setMessage("存在错误");
        } else  {
            response.setMessage("执行成功");
        }
        response.setStatus(0);
        JudgeInfo judgeInfo = new JudgeInfo();
        response.setJudgeInfo(judgeInfo);
        

        return response;
    }
}
