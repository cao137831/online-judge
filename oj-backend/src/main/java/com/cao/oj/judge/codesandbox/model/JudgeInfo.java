package com.cao.oj.judge.codesandbox.model;

import lombok.Data;

/**
 * 判题信息
 * @author cao13
 */
@Data
public class JudgeInfo {

    /**
     * 判题状态 judgeInfoStatusEnum.getText()
     */
    private String judgeInfoStatus;

    /**
     * 如果有错误，返回错误信息，如编译错误或者空指针错误
     */
    private String errorMessage;

    /**
     * 消耗时间 -- ms
     */
    private long time;

    /**
     * 消耗内存 -- KB
     */
    private long memory;
}
