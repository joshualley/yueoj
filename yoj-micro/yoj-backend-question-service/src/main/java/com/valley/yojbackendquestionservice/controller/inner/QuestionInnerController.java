package com.valley.yojbackendquestionservice.controller.inner;

import com.valley.yojbackendcommon.common.ErrorCode;
import com.valley.yojbackendcommon.exception.BusinessException;
import com.valley.yojbackendmodel.model.entity.Question;
import com.valley.yojbackendmodel.model.entity.QuestionSubmit;
import com.valley.yojbackendquestionservice.service.QuestionService;
import com.valley.yojbackendquestionservice.service.QuestionSubmitService;
import com.valley.yojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 仅供内部调用
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        if (questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.getById(questionId);
    }

    @Override
    @PostMapping("/update")
    public boolean updateQuestionById(@RequestBody Question question) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.updateById(question);
    }

    @Override
    @GetMapping("/submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        if (questionSubmitId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionSubmitService.updateById(questionSubmit);
    }

}
