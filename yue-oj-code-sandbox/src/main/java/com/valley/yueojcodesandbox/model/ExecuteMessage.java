package com.valley.yueojcodesandbox.model;

import lombok.Builder;
import lombok.Data;

/**
 * 进程执行信息
 */
@Data
@Builder
public class ExecuteMessage {
    private Integer exitValue;
    private String output;
    private String error;
}
