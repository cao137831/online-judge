package com.cao.oj.judge;

import com.cao.oj.judge.codesandbox.model.JudgeInfo;
import com.cao.oj.judge.strategy.*;
import com.cao.oj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

import static com.cao.oj.constant.JudgeConstant.*;

/**
 * 判题管理，简化调用
 * @author cao13
 */
@Service
public class JudgeManager {

    /**
     * 将预先设定的限制和代码上想执行结果进行比对
     * @param judgeContext 比对上下文
     * @return 判题信息
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = null;
        switch (language){
            case JAVA:
                judgeStrategy = new JavaJudgeStrategy();
                break;
            case CPP:
                judgeStrategy = new CppJudgeStrategy();
                break;
            case GO:
                judgeStrategy = new GolangJudgeStrategy();
                break;
            case PYTHON:
                judgeStrategy = new PythonJudgeStrategy();
                break;
            default:
                judgeStrategy = new DefaultJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
