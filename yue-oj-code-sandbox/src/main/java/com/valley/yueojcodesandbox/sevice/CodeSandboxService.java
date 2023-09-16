package com.valley.yueojcodesandbox.sevice;

import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandboxService {
    ExecuteResponse executeCode(ExecuteCodeRequest request);
}
