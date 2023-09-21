package com.valley.yojbackendcodesandbox.sevice;

import com.valley.yojbackendcodesandbox.sevice.impl.JavaDockerSandbox;
import com.valley.yojbackendcommon.common.ErrorCode;
import com.valley.yojbackendcommon.exception.BusinessException;
import com.valley.yojbackendcommon.utils.BeanUtil;
import com.valley.yojbackendmodel.model.enums.QuestionSubmitLanguageEnum;

/**
 * 代码沙箱服务工厂
 */
public class CodeSandboxServiceFactory {
    public static CodeSandboxService newInstance(String language) {
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持该语言类型：" + language);
        }
        switch (languageEnum) {
            case JAVA:
                return BeanUtil.getBean(JavaDockerSandbox.class);
            case CPLUSPLUS:
                return BeanUtil.getBean(JavaDockerSandbox.class);
            case GOLANG:
                return BeanUtil.getBean(JavaDockerSandbox.class);
            case JAVASCRIPT:
                return BeanUtil.getBean(JavaDockerSandbox.class);
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持该语言类型：" + language);
        }
    }
}
