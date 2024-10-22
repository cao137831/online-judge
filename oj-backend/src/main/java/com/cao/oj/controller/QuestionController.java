package com.cao.oj.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cao.oj.annotation.AuthCheck;
import com.cao.oj.common.BaseResponse;
import com.cao.oj.common.DeleteRequest;
import com.cao.oj.common.ErrorCode;
import com.cao.oj.common.ResultUtils;
import com.cao.oj.constant.UserConstant;
import com.cao.oj.exception.BusinessException;
import com.cao.oj.exception.ThrowUtils;
import com.cao.oj.model.dto.question.*;
import com.cao.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.cao.oj.model.entity.Question;
import com.cao.oj.model.entity.QuestionSubmit;
import com.cao.oj.model.entity.User;
import com.cao.oj.model.vo.QuestionSubmitVO;
import com.cao.oj.model.vo.QuestionVO;
import com.cao.oj.service.QuestionService;
import com.cao.oj.service.QuestionSubmitService;
import com.cao.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.cao.oj.constant.UserConstant.ADMIN_ROLE;

/**
 * 题目接口
 * 
 * @author cao13
 */
@RestController
@RequestMapping("/questions")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    // region 增删改查

    /**
     * 创建
     *
     * @param questionAddRequest 添加题目请求
     * @param request HttpServletRequest
     * @return 是否新增成功
     */
    @PostMapping
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> add(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(questionAddRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Long id = questionService.add(questionAddRequest, request);
        return ResultUtils.success(id);
    }

    /**
     * 删除
     *
     * @param deleteRequest 删除请求
     * @param request HttpServletRequest
     * @return 是否删除成功
     */
    @DeleteMapping
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> remove(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(deleteRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        boolean result = questionService.remove(deleteRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest 题目更新请求
     * @return 是否更新成功
     */
    @PutMapping
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> update(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (ObjectUtil.isEmpty(questionUpdateRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        boolean result = questionService.update(questionUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取（包装类）
     *
     * @param id 题目id
     * @return 题目包装类
     */
    @GetMapping("/{id}/vo")
    public BaseResponse<QuestionVO> getQuestionVoById(@PathVariable Long id, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question));
    }

    /**
     * 根据 id 获取（本人或管理员）
     *
     * @param id 题目id
     * @return 题目类
     */
    @GetMapping("/{id}")
    public BaseResponse<Question> getQuestionById(@PathVariable Long id, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        Question question = questionService.getQuestionById(id, request);
        return ResultUtils.success(question);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param questionQueryRequest 题目查询请求
     * @return 分页题目信息
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        if (ObjectUtil.isEmpty(questionQueryRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage =
            questionService.page(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest 题目查询请求
     * @return 分页封装的题目信息
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        if (ObjectUtil.isEmpty(questionQueryRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();

        Page<QuestionVO> questionVoPage = questionService.getQuestionVoPage(current, size, questionQueryRequest);
        return ResultUtils.success(questionVoPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest 题目查询请求
     * @param request HttpServletRequest
     * @return 分页封装的题目信息
     */
    @PostMapping("/list/page/vo/my")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
        HttpServletRequest request) {
        if (ObjectUtil.isEmpty(questionQueryRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Page<QuestionVO> questionVoPage = questionService.listMyQuestionVoByPage(questionQueryRequest, request);
        return ResultUtils.success(questionVoPage);
    }

    // endregion
}
