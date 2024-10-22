package com.cao.oj.judge;

import com.cao.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cao.oj.model.entity.QuestionSubmit;
import com.cao.oj.model.vo.QuestionSubmitVO;

import java.util.List;

/**
 * 判题服务
 * @author cao13
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId 题目提交id
     */
    void doJudge(long questionSubmitId, long userId);

    ExecuteCodeResponse getExecuteCodeResponse(String code, String language, List<String> inputList);
}
