package com.valley.yueojcodesandbox.sevice;

import cn.hutool.core.io.resource.ResourceUtil;
import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SpringBootTest
class CodeSandboxTest {

    @Resource
    CodeSandbox codeSandbox;

    @Test
    public void TestExecuteCode() {
        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setInputs(Arrays.asList("1 2", "3 4"));
        String code = ResourceUtil.readStr("testcode/Main.java", StandardCharsets.UTF_8);
        request.setCode(code);
        request.setLanguage("java");

        ExecuteResponse response = codeSandbox.executeCode(request);
        System.out.println(response);
    }
}