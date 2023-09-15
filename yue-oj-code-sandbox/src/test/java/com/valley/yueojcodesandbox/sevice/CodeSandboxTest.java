package com.valley.yueojcodesandbox.sevice;

import cn.hutool.core.io.resource.ResourceUtil;
import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import com.valley.yueojcodesandbox.model.enums.LanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
class CodeSandboxTest {

    @Resource(name = "docker")
    CodeSandbox codeSandbox;

    @Test
    public void TestExecuteCode() {
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputs(Arrays.asList("1 2", "3 4"));
        String code = ResourceUtil.readStr("testcode/Main.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage(LanguageEnum.JAVA.getValue());

        ExecuteResponse response = codeSandbox.executeCode(request);
        System.out.println(response);
    }

    /**
     * 测试休眠错误
     */
    @Test
    public void TestExecuteSleepCode() {
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputs(Arrays.asList("1 2", "3 4"));
        String code = ResourceUtil.readStr("testcode/SleepError.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage(LanguageEnum.JAVA.getValue());

        ExecuteResponse response = codeSandbox.executeCode(request);
        System.out.println(response);
    }

    /**
     * 测试无限内存错误
     */
    @Test
    public void TestExecuteMemoryErrCode() {
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputs(Arrays.asList("1 2", "3 4"));
        String code = ResourceUtil.readStr("testcode/MemoryError.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage(LanguageEnum.JAVA.getValue());

        ExecuteResponse response = codeSandbox.executeCode(request);
        System.out.println(response);
    }

    /**
     * 测试读取文件
     */
    @Test
    public void TestExecuteReadServerFileCode() {
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputs(Collections.singletonList("1 2"));
        String code = ResourceUtil.readStr("testcode/ReadServerFileError.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage(LanguageEnum.JAVA.getValue());

        ExecuteResponse response = codeSandbox.executeCode(request);
        System.out.println(response);
    }

    /**
     * 测试写入文件
     */
    @Test
    public void TestExecuteWriteServerFileCode() {
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputs(Collections.singletonList("1 2"));
        String code = ResourceUtil.readStr("testcode/WriteServerFileError.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage(LanguageEnum.JAVA.getValue());

        ExecuteResponse response = codeSandbox.executeCode(request);
        System.out.println(response);
    }

    /**
     * 测试执行注入的文件中的代码
     */
    @Test
    public void TestExecuteRunFileCode() {
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputs(Collections.singletonList("1 2"));
        String code = ResourceUtil.readStr("testcode/RunFileError.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage(LanguageEnum.JAVA.getValue());

        ExecuteResponse response = codeSandbox.executeCode(request);
        System.out.println(response);
    }
}