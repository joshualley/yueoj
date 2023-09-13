package com.valley.yueojcodesandbox.sevice.impl;

import cn.hutool.core.io.FileUtil;
import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import com.valley.yueojcodesandbox.sevice.CodeSandbox;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    final private String FILE_NAME = "Main";
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
        String userCodePath = userCodeParentPath + File.separator + FILE_NAME + FILE_SUFFIX;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        // 2. 编译代码，得到class文件
        String cmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        Process process = null;
        int exitCode = -1;
        try {
            process = Runtime.getRuntime().exec(cmd);
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException ignored) {}
        if (process == null) {
            return null;
        }
        // 收集编译信息
        BufferedReader reader;
        if (exitCode == 0) {
            System.out.println("编译成功");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } else {
            System.out.println("编译失败:" + exitCode);
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        }
        StringBuilder compileOutput = new StringBuilder();
        String outputLine = "";
        try {
            while ((outputLine = reader.readLine()) != null) {
                compileOutput.append(outputLine);
            }
        } catch (IOException ignored) {}
        System.out.println(compileOutput);
        // 3. 编译成功后，执行代码，得到输出结果
        // 4. 收集输出结果
        // 5. 文件清理
        // 6. 错误处理，提升程序健壮性

        return null;
    }
}
