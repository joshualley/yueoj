package com.yupi.yueoj.judge.strategy.impl;

import com.yupi.yueoj.judge.strategy.JudgeContext;
import com.yupi.yueoj.judge.strategy.JudgeStrategy;
import com.yupi.yueoj.model.dto.question.JudgeCase;
import com.yupi.yueoj.model.dto.question.JudgeConfig;
import com.yupi.yueoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.yueoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * java的判题策略
 */
public class JavaJudgeStrategy implements JudgeStrategy {
    /**
     * 根据沙箱的执行结果，设置题目的判题状态和其他信息
     * @param context
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext context) {
        JudgeInfo judgeInfo = context.getJudgeInfo();
        // String message = judgeInfo.getMessage();
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

        List<String> inputs = context.getInputs();
        List<String> outputs = context.getOutputs();
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
