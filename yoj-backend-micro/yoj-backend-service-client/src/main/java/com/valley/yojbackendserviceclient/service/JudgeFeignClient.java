package com.valley.yojbackendserviceclient.service;


import com.valley.yojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 判题服务
 */
@FeignClient(name = "yoj-backend-question-service")
public interface JudgeFeignClient {

    /**
     * 判题
     * @param questionSubmitId 题目提交ID
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
