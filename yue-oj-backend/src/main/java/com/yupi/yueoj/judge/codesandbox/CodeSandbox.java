package com.yupi.yueoj.judge.codesandbox;

import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    ExecuteResponse executeCode(ExecuteCodeRequest request);
}
