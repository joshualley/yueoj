package com.valley.yojbackendjudgeservice.codesandbox.impl;

import com.valley.yojbackendjudgeservice.codesandbox.CodeSandbox;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import com.valley.yojbackendserviceclient.service.CodeSandboxFeignClient;

import javax.annotation.Resource;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Resource
    private CodeSandboxFeignClient codeSandboxFeignClient;

    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        return codeSandboxFeignClient.execCode(request);
    }
}
