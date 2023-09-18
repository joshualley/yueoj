package com.valley.yojbackendjudgeservice.codesandbox;


import com.valley.yojbackendcommon.common.ErrorCode;
import com.valley.yojbackendcommon.exception.BusinessException;
import com.valley.yojbackendjudgeservice.codesandbox.impl.ExampleCodeSandbox;
import com.valley.yojbackendjudgeservice.codesandbox.impl.RemoteCodeSandbox;
import com.valley.yojbackendjudgeservice.codesandbox.impl.ThirdPartyCodeSandbox;

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
