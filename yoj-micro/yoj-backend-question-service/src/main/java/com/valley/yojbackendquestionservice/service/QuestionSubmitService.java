package com.valley.yojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.valley.yojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.valley.yojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.valley.yojbackendmodel.model.entity.QuestionSubmit;
import com.valley.yojbackendmodel.model.entity.User;
import com.valley.yojbackendmodel.model.vo.QuestionSubmitVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author joshu
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-09-08 10:47:14
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 题目提交（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionSubmitInner(long userId, long questionId);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取问题的封装
     *
     * @param questionSubmit
     * @param loginUser
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser, HttpServletRequest request);

    /**
     * 分页获取问题的封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser, HttpServletRequest request);
}
