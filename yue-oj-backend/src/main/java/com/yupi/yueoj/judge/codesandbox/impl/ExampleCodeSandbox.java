package com.yupi.yueoj.judge.codesandbox.impl;

import com.yupi.yueoj.judge.codesandbox.CodeSandbox;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;
import com.yupi.yueoj.judge.codesandbox.model.JudgeInfo;
import com.yupi.yueoj.model.enums.JudgeInfoMessageEnum;
import com.yupi.yueoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("示例代码沙箱");
        List<String> inputs = request.getInputs();

        ExecuteResponse response = new ExecuteResponse();
        response.setOutputs(inputs);
        response.setMessage("测试执行成功");
        response.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);

        response.setJudgeInfo(judgeInfo);

        return response;
    }
}
