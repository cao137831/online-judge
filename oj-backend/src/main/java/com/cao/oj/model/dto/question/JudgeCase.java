package com.cao.oj.model.dto.question;

import lombok.Data;

/**
 * 题目用例
 * @author cao13
 */
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
