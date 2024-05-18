package com.cao.oj.judge.strategy;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cao.oj.model.dto.question.JudgeCase;
import com.cao.oj.model.dto.question.JudgeConfig;
import com.cao.oj.judge.codesandbox.model.JudgeInfo;
import com.cao.oj.model.entity.Question;
import com.cao.oj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * Java判题策略
 */
public class CppJudgeStrategy implements JudgeStrategy{

    /**
     * 判题策略
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        //5.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        long memory = judgeInfo.getMemory() == null ? 0L : judgeInfo.getMemory() / 1024L / 8L;    //沙箱的执行结果，拿到的是bit单位，转化为kb
        long time = judgeInfo.getTime() == null ? 0L : judgeInfo.getTime();     //单位是ms

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());

        if (outputList.size() != inputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        //依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            String output = outputList.get(i).replace('\n', ' ').trim();
            String judgeOutPut = judgeCase.getOutput().replace('\n', ' ').trim();
            if (!StrUtil.equals(judgeOutPut, output)){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
                return judgeInfoResponse;
            }
        }
        //判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needTimeLimit = judgeConfig.getTimeLimit();
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needStackLimit = judgeConfig.getStackLimit();

        if (memory > needMemoryLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }
        if (time > needTimeLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getText());
            return judgeInfoResponse;
        }

        return judgeInfoResponse;
    }
}
