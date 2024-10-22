package com.cao.oj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author cao13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    /**
     * 输出列表
     */
    private List<String> outputList;

    /**
     * 接口请求的信息，待判题还是判题中，或者已完成
     */
    private String questionSubmitStatus;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
