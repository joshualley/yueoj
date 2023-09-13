package com.yupi.yueoj.judge.impl;

import cn.hutool.json.JSONUtil;
import com.yupi.yueoj.common.ErrorCode;
import com.yupi.yueoj.exception.BusinessException;
import com.yupi.yueoj.judge.JudgeService;
import com.yupi.yueoj.judge.codesandbox.CodeSandbox;
import com.yupi.yueoj.judge.codesandbox.CodeSandboxFactory;
import com.yupi.yueoj.judge.codesandbox.CodeSandboxProxy;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;
import com.yupi.yueoj.judge.strategy.JudgeManager;
import com.yupi.yueoj.judge.strategy.JudgeContext;
import com.yupi.yueoj.model.dto.question.JudgeCase;
import com.yupi.yueoj.model.dto.question.JudgeConfig;
import com.yupi.yueoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.yueoj.model.entity.Question;
import com.yupi.yueoj.model.entity.QuestionSubmit;
import com.yupi.yueoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.yueoj.service.QuestionService;
import com.yupi.yueoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务实现
 */
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    // 读取配置文件
    @Value("${codesandbox.value:example}")
    private String codeSandboxType;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1) 传入题目的提交ID，获取到对应的题目及提交信息(代码，编程语言)
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交信息不存在");
        }

        // 先判断题目的提交状态，避免重复执行
        Integer status = questionSubmit.getStatus();
        if (status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题中");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 增加题目的提交数
        Integer submitNumb = question.getSubmitNumb();
        Question questionUpdate = new Question();
        questionUpdate.setId(questionId);
        questionUpdate.setSubmitNumb(submitNumb+1);
        boolean isUpdateSuccess = questionService.updateById(questionUpdate);
        if (!isUpdateSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交数更新失败");
        }

        // 修改题目提交的判题状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        isUpdateSuccess = questionSubmitService.updateById(questionSubmitUpdate);
        if (!isUpdateSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目判题状态更新失败");
        }

        // 2) 调用代码沙箱，获取执行结果
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputs = judgeCaseList.stream()
                .map(JudgeCase::getInput)
                .collect(Collectors.toList());

        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(codeSandboxType);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);
        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputs(inputs)
                .build();
        ExecuteResponse response = codeSandboxProxy.executeCode(request);
        // 3) 调用判题策略进行判题
        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        JudgeContext judgeContext = JudgeContext.builder()
                .judgeInfo(response.getJudgeInfo())
                .judgeCases(judgeCaseList)
                .judgeConfig(judgeConfig)
                .outputs(response.getOutputs())
                .inputs(inputs)
                .build();
        JudgeManager manager = new JudgeManager();
        JudgeInfo judgeInfo = manager.doJudge(judgeContext);

        // 更新题目提交的状态
        QuestionSubmit questionSubmitSuccessUpdate = new QuestionSubmit();
        questionSubmitSuccessUpdate.setId(questionSubmitId);
        questionSubmitSuccessUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitSuccessUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        isUpdateSuccess = questionSubmitService.updateById(questionSubmitUpdate);
        if (!isUpdateSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目判题状态更新失败");
        }
        // 更新题目的通过数
        Integer acceptedNum = question.getAcceptedNum();
        Question q = new Question();
        q.setAcceptedNum(acceptedNum+1);
        isUpdateSuccess = questionService.updateById(q);
        if (!isUpdateSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目通过数更新失败");
        }
        // 返回题目提交信息
        return questionSubmitService.getById(questionSubmitId);
    }
}
