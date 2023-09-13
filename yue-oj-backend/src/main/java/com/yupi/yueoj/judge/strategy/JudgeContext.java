package com.yupi.yueoj.judge.strategy;

import com.yupi.yueoj.model.dto.question.JudgeCase;
import com.yupi.yueoj.model.dto.question.JudgeConfig;
import com.yupi.yueoj.judge.codesandbox.model.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于在策略中传递的上下文
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeContext {
    /**
     * 代码沙箱返回的判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 代码沙箱返回的判题结果
     */
    private List<String> outputs;
    /**
     * 题目的判题输入用例
     */
    private List<String> inputs;
    /**
     * 提交代码时使用的编程语言
     */
    private String language;
    /**
     * 题目的判题用例
     */
    private List<JudgeCase> judgeCases;
    /**
     * 题目的判题配置(限制条件)
     */
    private JudgeConfig judgeConfig;
}
