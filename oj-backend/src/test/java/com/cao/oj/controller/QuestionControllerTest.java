package com.cao.oj.controller;

import com.cao.oj.model.entity.Question;
import com.cao.oj.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionControllerTest {

    @Resource
    private QuestionService questionService;

    @Test
    void getQuestionVOById() {
        Question question = questionService.getById(1781270866839633921L);
        System.out.println(question);
    }
}