package com.cao.oj.judge.strategy;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cao.oj.judge.codesandbox.model.JudgeInfo;
import com.cao.oj.model.dto.question.JudgeCase;
import com.cao.oj.model.dto.question.JudgeConfig;
import com.cao.oj.model.entity.Question;
import com.cao.oj.model.enums.JudgeInfoStatusEnum;

import java.util.List;

/**
 * CPP 判题策略
 * 
 * @author cao13
 */
public class CppJudgeStrategy implements JudgeStrategy {

    /**
     * 判题策略
     * 
     * @param judgeContext 判题所需信息
     * @return 判题结果
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        // 5.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        // 沙箱的执行结果，拿到的是 bit 单位，转化为 KB
        long memory = judgeInfo.getMemory() / 1024L / 8L;
        long time = judgeInfo.getTime();

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);

        // 如果以下未发生异常,默认为通过
        JudgeInfoStatusEnum judgeInfoStatusEnum = JudgeInfoStatusEnum.ACCEPTED;
        judgeInfoResponse.setJudgeInfoStatus(judgeInfoStatusEnum.getText());

        if (outputList.size() != inputList.size()) {
            judgeInfoResponse.setErrorMessage(JudgeInfoStatusEnum.WRONG_ANSWER.getText());
            return judgeInfoResponse;
        }

        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            String rightOutput = judgeCaseList.get(i).getOutput();
            String output = outputList.get(i);

            // 1.先判断程序输出和原答案是否完全一样
            if (!StrUtil.equals(output, rightOutput)) {
                // 2.不一样的话，判断是否数据输出正确
                String trimOutput = output.trim().
                // 替换行末空格
                    replace(" \n", "\n").
                    // 替换多个空格为一个空格
                    replaceAll("\\s+", " ");
                if (StrUtil.equals(trimOutput, rightOutput)) {
                    judgeInfoResponse.setErrorMessage(JudgeInfoStatusEnum.PRESENTATION_ERROR.getText());
                } else {
                    judgeInfoResponse.setErrorMessage(JudgeInfoStatusEnum.WRONG_ANSWER.getText());
                }
            }
        }

        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        long needTimeLimit = judgeConfig.getTimeLimit();
        long needMemoryLimit = judgeConfig.getMemoryLimit();

        if (memory > needMemoryLimit) {
            judgeInfoResponse.setErrorMessage(JudgeInfoStatusEnum.MEMORY_LIMIT_EXCEEDED.getText());
            return judgeInfoResponse;
        }

        if (time > needTimeLimit) {
            judgeInfoResponse.setErrorMessage(JudgeInfoStatusEnum.TIME_LIMIT_EXCEEDED.getText());
            return judgeInfoResponse;
        }

        return judgeInfoResponse;
    }
}
