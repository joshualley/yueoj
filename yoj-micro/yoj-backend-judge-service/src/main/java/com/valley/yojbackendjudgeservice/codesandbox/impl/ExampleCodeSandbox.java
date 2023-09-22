package com.valley.yojbackendjudgeservice.codesandbox.impl;

import com.valley.yojbackendjudgeservice.codesandbox.CodeSandbox;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import com.valley.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.valley.yojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.valley.yojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 示例代码沙箱
 */
@Component
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
