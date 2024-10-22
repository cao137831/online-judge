package com.cao.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cao.oj.common.DeleteRequest;
import com.cao.oj.model.dto.question.QuestionAddRequest;
import com.cao.oj.model.dto.question.QuestionQueryRequest;
import com.cao.oj.model.dto.question.QuestionUpdateRequest;
import com.cao.oj.model.entity.Question;
import com.cao.oj.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cao.oj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cao13
 * @description 针对表【question(题目表)】的数据库操作Service
 * @createDate 2024-04-07 21:17:20
 */
public interface QuestionService extends IService<Question> {

    Long add(QuestionAddRequest questionAddRequest, HttpServletRequest request);

    boolean remove(DeleteRequest deleteRequest, HttpServletRequest request);

    boolean update(QuestionUpdateRequest questionUpdateRequest);

    Question getQuestionById(Long id, HttpServletRequest request);

    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @return
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @return
     */
    Page<QuestionVO> getQuestionVoPage(Page<Question> questionPage);

    Page<QuestionVO> listMyQuestionVoByPage(QuestionQueryRequest questionQueryRequest, HttpServletRequest request);

    Page<QuestionVO> getQuestionVoPage(long current, long size, QuestionQueryRequest questionQueryRequest);
}
