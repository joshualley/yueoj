package com.yupi.yueoj.judge.codesandbox.impl;

import com.yupi.yueoj.judge.codesandbox.CodeSandbox;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;

/**
 * 第三方代码沙箱 go-judge
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
