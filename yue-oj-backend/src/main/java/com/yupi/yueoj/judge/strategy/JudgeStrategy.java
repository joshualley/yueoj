package com.yupi.yueoj.judge.strategy;

import com.yupi.yueoj.judge.codesandbox.model.JudgeInfo;

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
