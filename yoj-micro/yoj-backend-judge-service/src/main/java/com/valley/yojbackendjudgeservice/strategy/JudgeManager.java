package com.valley.yojbackendjudgeservice.strategy;


import com.valley.yojbackendcommon.common.ErrorCode;
import com.valley.yojbackendcommon.exception.BusinessException;
import com.valley.yojbackendjudgeservice.strategy.impl.DefaultJudgeStrategy;
import com.valley.yojbackendjudgeservice.strategy.impl.JavaJudgeStrategy;
import com.valley.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.valley.yojbackendmodel.model.enums.QuestionSubmitLanguageEnum;

/**
 * 简化调用
 */
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext context) {
        JudgeStrategy strategy;
        QuestionSubmitLanguageEnum language = QuestionSubmitLanguageEnum.getEnumByValue(context.getLanguage());
        if (language == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        switch (language) {
            case JAVA:
                strategy = new JavaJudgeStrategy();
                break;
            case CPLUSPLUS:
                strategy = new DefaultJudgeStrategy();
                break;
            default:
                strategy = new DefaultJudgeStrategy();
                break;
        }
        return strategy.doJudge(context);
    }
}
