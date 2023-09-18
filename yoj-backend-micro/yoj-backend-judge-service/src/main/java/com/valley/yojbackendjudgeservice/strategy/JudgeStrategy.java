package com.valley.yojbackendjudgeservice.strategy;


import com.valley.yojbackendmodel.model.codesandbox.JudgeInfo;

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
