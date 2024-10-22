package com.cao.oj.model.dto.question;

import lombok.Data;

/**
 * 题目配置
 * @author cao13
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（bit）
     */
    private Long memoryLimit;

}
