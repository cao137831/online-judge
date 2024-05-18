package com.cao.oj.judge;

import com.cao.oj.judge.strategy.*;
import com.cao.oj.judge.codesandbox.model.JudgeInfo;
import com.cao.oj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理，简化调用
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = null;
        switch (language){
            case "java":
                judgeStrategy = new JavaJudgeStrategy();
                break;
            case "cpp":
                judgeStrategy = new CppJudgeStrategy();
                break;
            case "go":
                judgeStrategy = new GolangJudgeStrategy();
                break;
            case "python":
                judgeStrategy = new PythonJudgeStrategy();
                break;
            default:
                judgeStrategy = new DefaultJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
