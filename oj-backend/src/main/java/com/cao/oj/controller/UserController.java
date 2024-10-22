package com.cao.oj.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cao.oj.annotation.AuthCheck;
import com.cao.oj.common.BaseResponse;
import com.cao.oj.common.DeleteRequest;
import com.cao.oj.common.ErrorCode;
import com.cao.oj.common.ResultUtils;
import com.cao.oj.constant.UserConstant;
import com.cao.oj.exception.BusinessException;
import com.cao.oj.exception.ThrowUtils;
import com.cao.oj.model.dto.user.UserAddRequest;
import com.cao.oj.model.dto.user.UserLoginRequest;
import com.cao.oj.model.dto.user.UserQueryRequest;
import com.cao.oj.model.dto.user.UserRegisterRequest;
import com.cao.oj.model.dto.user.UserUpdateMyRequest;
import com.cao.oj.model.dto.user.UserUpdateRequest;
import com.cao.oj.model.entity.User;
import com.cao.oj.model.vo.UserVO;
import com.cao.oj.service.UserService;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import static com.cao.oj.constant.UserConstant.ADMIN_ROLE;

/**
 * 用户接口
 * 
 * @author cao13
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 新的用户Id
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (ObjectUtil.isEmpty(userRegisterRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long result = userService.register(userRegisterRequest);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @param request HttpServletRequest
     * @return 封装的登陆成功用户信息
     */
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(userLoginRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        UserVO loginUserVO = userService.login(userLoginRequest, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     *
     * @param request HttpServletRequest
     * @return 是否登出成功
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        boolean result = userService.logout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request HttpServletRequest
     * @return 封装的已登陆用户信息
     */
    @GetMapping("/login_user")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getUserVO(user));
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest 添加用户请求
     * @return 新添加的用户Id
     */
    @PostMapping
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> add(@RequestBody UserAddRequest userAddRequest) {
        if (ObjectUtil.isEmpty(userAddRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        long userId = userService.add(userAddRequest);
        return ResultUtils.success(userId);
    }

    /**
     * 删除用户
     * 
     * @param deleteRequest 删除请求
     * @param request HttpServletRequest
     * @return 是否删除成功
     */
    @DeleteMapping
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> remove(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(deleteRequest.getId());
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest 用户更新请求
     * @param request HttpServletRequest
     * @return 是否更新成功
     */
    @PutMapping
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> update(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.update(userUpdateRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id 用户Id
     * @param request HttpServletRequest
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<User> getUserById(@PathVariable long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        if (ObjectUtil.isEmpty(user)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id 用户Id
     * @param request HttpServletRequest
     * @return 脱敏用户信息
     */
    @GetMapping("/{id}/vo")
    public BaseResponse<UserVO> getUserVoById(@PathVariable long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest 用户查询请求
     * @param request HttpServletRequest
     * @return 分页用户信息
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
        HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage =
            userService.page(new Page<>(current, size), userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest 用户查询请求
     * @param request HttpServletRequest
     * @return 分页脱敏用户信息
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVoByPage(@RequestBody UserQueryRequest userQueryRequest,
        HttpServletRequest request) {
        if (ObjectUtil.isEmpty(userQueryRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();

        Page<User> userPage =
            userService.page(new Page<>(current, size), userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVoPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVoPage.setRecords(userVO);
        return ResultUtils.success(userVoPage);
    }

    // endregion

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest 用户个人信息更新请求
     * @param request HttpServletRequest
     * @return 是否更新成功
     */
    @PutMapping("/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
        HttpServletRequest request) {
        if (ObjectUtil.isEmpty(userUpdateMyRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

}
