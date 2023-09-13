package com.valley.yueojcodesandbox.sevice;

import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteResponse;

import java.io.IOException;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    ExecuteResponse executeCode(ExecuteCodeRequest request);
}
