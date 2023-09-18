package com.valley.yojbackendjudgeservice.service;


import com.valley.yojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId 题目提交ID
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
