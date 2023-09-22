package com.valley.yojbackendmodel.model.codesandbox;

import lombok.Data;

/**
 * 判题信息
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 执行时间 (ms)
     */
    private long time;
    /**
     * 消耗内存 (KB)
     */
    private long memory;
}
