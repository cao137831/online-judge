package com.cao.oj.judge;

import com.cao.oj.model.entity.QuestionSubmit;
import com.cao.oj.model.vo.QuestionSubmitVO;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
