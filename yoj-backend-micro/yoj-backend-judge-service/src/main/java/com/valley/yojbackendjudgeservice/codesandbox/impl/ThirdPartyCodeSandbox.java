package com.valley.yojbackendjudgeservice.codesandbox.impl;


import com.valley.yojbackendjudgeservice.codesandbox.CodeSandbox;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import org.springframework.stereotype.Component;

/**
 * 第三方代码沙箱 go-judge
 */
@Component
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
