package com.cao.oj.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cao.oj.common.ErrorCode;
import com.cao.oj.constant.PageConstant;
import com.cao.oj.exception.BusinessException;
import com.cao.oj.judge.JudgeService;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cao.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.cao.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.cao.oj.model.dto.questionsubmit.QuestionTestRequest;
import com.cao.oj.model.entity.Question;
import com.cao.oj.model.entity.QuestionSubmit;
import com.cao.oj.model.entity.User;
import com.cao.oj.model.enums.QuestionSubmitLanguageEnum;
import com.cao.oj.model.enums.QuestionSubmitStatusEnum;
import com.cao.oj.model.vo.QuestionSubmitVO;
import com.cao.oj.service.QuestionService;
import com.cao.oj.service.QuestionSubmitService;
import com.cao.oj.mapper.QuestionSubmitMapper;
import com.cao.oj.service.UserService;
import com.cao.oj.util.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author cao13
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
 * @createDate 2024-04-07 21:18:16
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy // 产生循环依赖，需要懒加载
    private JudgeService judgeService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param request HttpServletRequest
     * @return id
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(questionSubmitAddRequest.getQuestionId())
            || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long questionId = questionSubmitAddRequest.getQuestionId();

        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "语言参数错误");
        }

        // 是否已提交题目
        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();

        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());

        // 设置初始状态
        questionSubmit.setQuestionSubmitStatus(QuestionSubmitStatusEnum.WAITING.getText());
        questionSubmit.setJudgeInfo("{}");

        boolean result = this.save(questionSubmit);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        Long questionSubmitId = questionSubmit.getId();

        // 执行判题服务（异步执行）
        CompletableFuture.runAsync(() -> {
            // todo 进一步探讨篇
            judgeService.doJudge(questionSubmitId, userId);
        });

        return questionSubmitId;
    }

    /**
     * 题目测试
     *
     * @param questionTestRequest 题目测试信息
     * @return 执行结果
     */
    @Override
    public ExecuteCodeResponse doQuestionTest(QuestionTestRequest questionTestRequest) {

        List<String> inputList = questionTestRequest.getInputList();
        String language = questionTestRequest.getLanguage();
        String code = questionTestRequest.getCode();
        Long questionId = questionTestRequest.getQuestionId();

        if (questionTestRequest.getQuestionId() <= 0 || CollectionUtil.isEmpty(inputList) || StrUtil.isEmpty(language)
            || StrUtil.isEmpty(code) || ObjectUtil.isEmpty(questionId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 校验编程语言是否合法
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        ExecuteCodeResponse executeCodeResponse = judgeService.getExecuteCodeResponse(code, language, inputList);
        return executeCodeResponse;
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到mybatis框架支持的QueryWrapper类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(PageConstant.SORT_ORDER_ASC),
            sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人或者管理员能够看到自己（提交userId和登录用户id不同）提交的代码
        Long userId = loginUser.getId();
        if (!Objects.equals(userId, questionSubmit.getUserId()) && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVoPage =
            new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());

        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
            .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser)).collect(Collectors.toList());

        questionSubmitVoPage.setRecords(questionSubmitVOList);
        return questionSubmitVoPage;
    }
}
