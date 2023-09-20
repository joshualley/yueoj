package com.valley.yojbackendcodesandbox.sevice.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.valley.yojbackendcodesandbox.sevice.CodeSandboxService;
import com.valley.yojbackendcodesandbox.utils.ProcessUtil;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteMessage;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import com.valley.yojbackendmodel.model.codesandbox.JudgeInfo;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Java原生实现的java代码沙箱
 */
@Service("native")
public class JavaNativeCodeSandboxService implements CodeSandboxService {

    /**
     * 保持存java代码文件的更目录
     */
    final private String CODE_PATH_NAME = "temp" + File.separator + "java";
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

    final private static WordTree mWordTree = new WordTree();

    static {
        // 添加程序执行时，禁用的操作
        mWordTree.addWords(Arrays.asList(
                "Files",
                "RunTime",
                "exec"
        ));
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
        String userCodeParentPath = codeDir + File.separator + UUID.randomUUID();
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
        List<String> outputs = new ArrayList<>();
        boolean existsError = false;
        // 程序最大运行时间
        Long maxTime = 0L;
        String securityManagerClassPath = userDir + File.separator + SECURITY_MANAGER_CLASS_PATH;
        for (String input : inputs) {
            // -Dfile.encoding=UTF-8: 指定运行的程序编码格式
            // -Xmx256m: 指定JVM运行程序的最大堆空间为 256M
            // -Xms: 指定JVM运行程序的初始堆空间
            // -Djava.security.manager=MySecurityManager: 切换为自定义的安全管理器
            String runCmd = String.format(
                    "java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=MySecurityManager %s %s",
                    userCodeParentPath, securityManagerClassPath, CLASS_NAME, input);
            ExecuteMessage runOutput = ProcessUtil.runCmd(runCmd, TIMEOUT);
            if (runOutput.getExitValue() == 0) {
                outputs.add(runOutput.getOutput());
            } else {
                outputs.add(runOutput.getError());
                existsError = true;
            }
            if (runOutput.getTime() > maxTime) {
                maxTime = runOutput.getTime();
            }
        }
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
        judgeInfo.setMemory(0);
        response.setJudgeInfo(judgeInfo);

        return response;
    }
}
