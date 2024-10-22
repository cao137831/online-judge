package com.cao.oj.judge;

import cn.hutool.json.JSONUtil;
import com.cao.oj.common.ErrorCode;
import com.cao.oj.exception.BusinessException;
import com.cao.oj.judge.codesandbox.CodeSandbox;
import com.cao.oj.judge.codesandbox.CodeSandboxFactory;
import com.cao.oj.judge.codesandbox.CodeSandboxProxy;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cao.oj.judge.codesandbox.model.JudgeInfo;
import com.cao.oj.judge.strategy.JudgeContext;
import com.cao.oj.model.dto.question.JudgeCase;
import com.cao.oj.model.entity.Question;
import com.cao.oj.model.entity.QuestionSubmit;
import com.cao.oj.model.enums.JudgeInfoStatusEnum;
import com.cao.oj.model.enums.QuestionSubmitStatusEnum;
import com.cao.oj.service.MyHistoryRecordService;
import com.cao.oj.service.MyQuestionStatusService;
import com.cao.oj.service.QuestionService;
import com.cao.oj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeManager judgeManager;

    @Resource
    private MyQuestionStatusService myQuestionStatusService;

    @Resource
    private MyHistoryRecordService myHistoryRecordService;

    @Value("${codesandbox.type:example}") // 设置默认值为example
    private String type;

    @Override
    // 事务执行
    @Transactional
    public void doJudge(long questionSubmitId, long userId) {

        // 1.传入题目的提交id，获取到相应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交记录不存在");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 2.如果不为等待状态，就不需要重复执行
        if (!QuestionSubmitStatusEnum.WAITING.getText().equals(questionSubmit.getQuestionSubmitStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }

        // 3.更改判题（题目提交）状态为“判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setQuestionSubmitStatus(QuestionSubmitStatusEnum.RUNNING.getText());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交记录更新错误");
        }

        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        String judgeCaseStr = question.getJudgeCase();

        // 获取输入用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        ExecuteCodeResponse executeCodeResponse = getExecuteCodeResponse(code, language, inputList);

        List<String> outputList = executeCodeResponse.getOutputList();

        // 5.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        // 6.进行判题
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 7.修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);

        // todo 一个方法不能那么长，待修改

        // 代表判题成功
        questionSubmitUpdate.setQuestionSubmitStatus(QuestionSubmitStatusEnum.SUCCEED.getText());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交记录更新错误");
        }

        // todo 这里需要添加事务，来保证多个更新操作能同时成功或者失败
//        UserQuestionAddRequest userQuestionAddRequest = new UserQuestionAddRequest();
//        userQuestionAddRequest.setQuestionId(questionId);
//        int status = 0;
//        if (judgeInfo.getJudgeInfoStatus().equals(JudgeInfoStatusEnum.ACCEPTED.getText())) {
//            status = 1;
//        }
//        userQuestionAddRequest.setStatus(status);
//
//        // 更新用户个人题目通过状态
//        update = userQuestionService.addUserQuestion(userId, userQuestionAddRequest);
//        if (!update) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户个人题目通过状态更新错误");
//        }
//
//        UserQuestionHistoryRecordAddRequest userQuestionHistoryRecordAddRequest =
//            new UserQuestionHistoryRecordAddRequest();
//        userQuestionHistoryRecordAddRequest.setQuestionId(questionId);
//        userQuestionHistoryRecordAddRequest.setHistoryCode(code);
//        userQuestionHistoryRecordAddRequest.setJudgeStatus(judgeInfo.getJudgeInfoStatus());
//
//        update =
//            userQuestionHistoryRecordService.addUserQuestionHistoryRecord(userId, userQuestionHistoryRecordAddRequest);
//        if (!update) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户个人题目提交记录更新错误");
//        }

        // 7.修改数据库中的题目通过人数结果
        Question questionUpdate = questionService.getById(questionId);
        questionUpdate.setSubmitNum(questionUpdate.getSubmitNum() + 1);
        if (JudgeInfoStatusEnum.ACCEPTED.getText().equals(judgeInfo.getJudgeInfoStatus())) {
            questionUpdate.setAcceptNum(questionUpdate.getAcceptNum() + 1);
        }

        update = questionService.updateById(questionUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目更新错误");
        }
    }

    @Override
    public ExecuteCodeResponse getExecuteCodeResponse(String code, String language, List<String> inputList) {

        // 调用代码沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);

        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest(inputList, code, language);

        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        return executeCodeResponse;
    }
}
