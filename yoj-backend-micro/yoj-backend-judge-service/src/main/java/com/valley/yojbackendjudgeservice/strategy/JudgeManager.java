package com.yupi.yueoj.judge.strategy;

import com.yupi.yueoj.common.ErrorCode;
import com.yupi.yueoj.exception.BusinessException;
import com.yupi.yueoj.judge.strategy.impl.DefaultJudgeStrategy;
import com.yupi.yueoj.judge.strategy.impl.JavaJudgeStrategy;
import com.yupi.yueoj.judge.codesandbox.model.JudgeInfo;
import com.yupi.yueoj.model.enums.QuestionSubmitLanguageEnum;

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
