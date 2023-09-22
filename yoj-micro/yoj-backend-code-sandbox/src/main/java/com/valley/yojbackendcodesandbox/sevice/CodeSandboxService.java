package com.valley.yojbackendcodesandbox.sevice;


import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandboxService {
    ExecuteResponse executeCode(ExecuteCodeRequest request);
}
