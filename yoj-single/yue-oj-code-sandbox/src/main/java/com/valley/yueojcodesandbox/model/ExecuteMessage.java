package com.valley.yueojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 进程执行信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteMessage {
    private Integer exitValue;
    private String output;
    private String error;
    /**
     * 程序消耗内存
     */
    private Long memory;
    /**
     * 程序执行时间
     */
    private Long time;
}
