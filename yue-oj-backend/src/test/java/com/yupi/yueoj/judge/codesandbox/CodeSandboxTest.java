package com.yupi.yueoj.judge.codesandbox;

import com.yupi.yueoj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;
import com.yupi.yueoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CodeSandboxTest {

    // 读取配置文件
    @Value("${codesandbox.value:example}")
    private String type;

    @Test
    void executeCode() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        String code = "int main() { return 0; }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputs = Arrays.asList("1 2", "3");
        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputs(inputs)
                .build();
        ExecuteResponse response = codeSandbox.executeCode(request);
        Assertions.assertNotNull(response);
    }

    @Test
    void executeCodeByProxy() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);
        String code = "int main() { return 0; }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputs = Arrays.asList("1 2", "3");
        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputs(inputs)
                .build();
        ExecuteResponse response = codeSandboxProxy.executeCode(request);
        Assertions.assertNotNull(response);
    }
}