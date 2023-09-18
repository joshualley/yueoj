package com.yupi.yueoj.judge.codesandbox;

import com.yupi.yueoj.common.ErrorCode;
import com.yupi.yueoj.exception.BusinessException;
import com.yupi.yueoj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yupi.yueoj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.yupi.yueoj.judge.codesandbox.impl.ThirdPartyCodeSandbox;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;

/**
 * 代码沙箱工厂 (简单工厂)
 */
public class CodeSandboxFactory {
    /**
     * 创建代码沙箱实例
     * @param type 代码沙箱类别
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        String t = type.toLowerCase();
        switch (t) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "third_party":
                return new ThirdPartyCodeSandbox();
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}
