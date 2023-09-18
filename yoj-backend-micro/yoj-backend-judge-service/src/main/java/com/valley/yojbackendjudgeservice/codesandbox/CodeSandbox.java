package com.valley.yojbackendjudgeservice.codesandbox;


import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    ExecuteResponse executeCode(ExecuteCodeRequest request);
}
