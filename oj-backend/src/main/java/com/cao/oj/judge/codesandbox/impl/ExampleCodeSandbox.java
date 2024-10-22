package com.cao.oj.judge.codesandbox.impl;

import com.cao.oj.judge.codesandbox.CodeSandbox;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cao.oj.judge.codesandbox.model.JudgeInfo;
import com.cao.oj.model.enums.JudgeInfoStatusEnum;
import com.cao.oj.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例代码沙箱（为了跑通业务流程）
 */
@Slf4j
@Component
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("示例代码沙箱（为了跑通业务流程）");
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        for (int i = 0; i < executeCodeRequest.getInputList().size(); i++) {
            outputList.add("test" + i);
        }
        executeCodeResponse.setOutputList(outputList);
        executeCodeResponse.setQuestionSubmitStatus("测试信息");
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setJudgeInfoStatus(JudgeInfoStatusEnum.ACCEPTED.getText());
        judgeInfo.setErrorMessage(QuestionSubmitStatusEnum.SUCCEED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
