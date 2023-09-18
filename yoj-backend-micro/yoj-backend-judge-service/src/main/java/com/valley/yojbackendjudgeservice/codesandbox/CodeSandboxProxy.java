package com.yupi.yueoj.judge.codesandbox;

import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱代理
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        log.info("代码沙箱请求：" + request.toString());
        ExecuteResponse response = codeSandbox.executeCode(request);
        log.info("代码沙箱响应：" + response.toString());
        return response;
    }
}
