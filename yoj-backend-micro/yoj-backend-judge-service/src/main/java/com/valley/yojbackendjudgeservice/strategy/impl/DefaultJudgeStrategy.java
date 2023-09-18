package com.valley.yojbackendjudgeservice.strategy.impl;


import com.valley.yojbackendjudgeservice.strategy.JudgeContext;
import com.valley.yojbackendjudgeservice.strategy.JudgeStrategy;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import com.valley.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.valley.yojbackendmodel.model.codesandbox.enums.ExecuteStatusEnum;
import com.valley.yojbackendmodel.model.dto.question.JudgeCase;
import com.valley.yojbackendmodel.model.dto.question.JudgeConfig;
import com.valley.yojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    /**
     * 根据沙箱的执行结果，设置题目的判题状态和其他信息
     * @param context
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext context) {
        ExecuteResponse sandCodeBoxResponse = context.getSandCodeBoxResponse();
        JudgeInfo judgeInfo = sandCodeBoxResponse.getJudgeInfo();
        if (judgeInfo == null) {
            judgeInfo = new JudgeInfo();
            judgeInfo.setMessage("");
            judgeInfo.setTime(0L);
            judgeInfo.setMemory(0L);
        }
        long time = judgeInfo.getTime();
        long memory = judgeInfo.getMemory();

        JudgeConfig judgeConfig = context.getJudgeConfig();
        Long timeLimit = judgeConfig.getTimeLimit();
        Long memoryLimit = judgeConfig.getMemoryLimit();
        // Long stackLimit = judgeConfig.getStackLimit();

        // 创建判题信息对象
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);

        // 先根据返回状态判断
        if (sandCodeBoxResponse.getStatus().equals(ExecuteStatusEnum.ILLEGAL_OPERATION.getValue())) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.DANGEROUS_OPERATION.getValue());
            return judgeInfoResponse;
        } else if (sandCodeBoxResponse.getStatus().equals(ExecuteStatusEnum.COMPILE_ERROR.getValue())) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getValue());
            return judgeInfoResponse;
        } else if (sandCodeBoxResponse.getStatus().equals(ExecuteStatusEnum.RUNNING_ERROR.getValue())) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getValue());
            return judgeInfoResponse;
        }

        List<String> inputs = context.getInputs();
        List<String> outputs = sandCodeBoxResponse.getOutputs();
        // a. 先判断输出数量和预期数量是否相同
        if (outputs.size() != inputs.size()) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
            return judgeInfoResponse;
        }
        // b. 判断结果是否正确
        List<JudgeCase> judgeCases = context.getJudgeCases();
        for (int i = 0; i < judgeCases.size(); i++) {
            JudgeCase aCase = judgeCases.get(i);
            String output = outputs.get(i);
            if (!aCase.getOutput().equals(output)) {
                judgeInfoResponse.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
                return judgeInfoResponse;
            }
        }
        // c. 判断是否符合限制条件
        if (memory > memoryLimit) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return judgeInfoResponse;
        }
        if (time > timeLimit) {
            judgeInfoResponse.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
            return judgeInfoResponse;
        }
        // 解题符合要求
        judgeInfoResponse.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        return judgeInfoResponse;
    }
}
