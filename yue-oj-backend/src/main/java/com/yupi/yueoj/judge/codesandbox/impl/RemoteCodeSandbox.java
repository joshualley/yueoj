package com.yupi.yueoj.judge.codesandbox.impl;

import com.yupi.yueoj.judge.codesandbox.CodeSandbox;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
