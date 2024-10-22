package com.cao.oj.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cao.oj.annotation.AuthCheck;
import com.cao.oj.common.BaseResponse;
import com.cao.oj.common.ErrorCode;
import com.cao.oj.common.ResultUtils;
import com.cao.oj.constant.UserConstant;
import com.cao.oj.exception.BusinessException;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cao.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.cao.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.cao.oj.model.dto.questionsubmit.QuestionTestRequest;
import com.cao.oj.model.entity.QuestionSubmit;
import com.cao.oj.model.entity.User;
import com.cao.oj.model.vo.QuestionSubmitVO;
import com.cao.oj.service.QuestionSubmitService;
import com.cao.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.cao.oj.constant.UserConstant.DEFAULT_ROLE;

/**
 * 题目提交接口
 * 
 * @author cao13
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 题目提交记录添加请求
     * @param request HttpServletRequest
     * @return result 新记录的id
     */
    @PostMapping
    public BaseResponse<Long> doSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
        HttpServletRequest request) {
        if (ObjectUtil.isEmpty(questionSubmitAddRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, request);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 提交题目
     *
     * @param questionTestRequest 题目测试请求
     * @return 执行结果
     */
    @PostMapping("/test")
    public BaseResponse<ExecuteCodeResponse> doTest(@RequestBody QuestionTestRequest questionTestRequest) {
        if (ObjectUtil.isEmpty(questionTestRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        ExecuteCodeResponse executeCodeResponse = questionSubmitService.doQuestionTest(questionTestRequest);
        return ResultUtils.success(executeCodeResponse);
    }

    /**
     * 分页获取题目提交记录列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest 查询请求
     * @return 分页结果
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>>
        listByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(questionSubmitQueryRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();

        // 得到从数据库中查询到的原始数据信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
            questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);

        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

}
