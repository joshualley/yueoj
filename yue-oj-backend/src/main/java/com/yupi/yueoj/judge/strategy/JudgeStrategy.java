package com.yupi.yueoj.judge.strategy;

import com.yupi.yueoj.model.dto.questionsubmit.JudgeInfo;

/**
 * 判题策略接口
 */
public interface JudgeStrategy {
    /**
     * 执行判题
     * @param context
     * @return
     */
    JudgeInfo doJudge(JudgeContext context);
}
