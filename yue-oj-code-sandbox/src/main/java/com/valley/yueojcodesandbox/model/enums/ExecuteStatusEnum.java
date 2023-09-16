package com.valley.yueojcodesandbox.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题完成后返回的状态
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum ExecuteStatusEnum {
    ACCEPT("执行成功", 1),
    COMPILE_ERROR("编译失败", 2),
    RUNNING_ERROR("执行失败", 3),
    ILLEGAL_OPERATION("非法操作", 4);


    private final String text;

    private final Integer value;

    ExecuteStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;

    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ExecuteStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ExecuteStatusEnum anEnum : ExecuteStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
