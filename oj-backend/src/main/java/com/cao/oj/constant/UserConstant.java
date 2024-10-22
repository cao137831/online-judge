package com.cao.oj.constant;

/**
 * 用户常量
 * @author cao13
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    // region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion

    // region 账号密码信息

    /**
     * 用户账号长度
     */
    Integer USER_ACCOUNT_LENGTH = 4;

    /**
     * 用户密码长度
     */
    Integer USER_PASSWORD_LENGTH = 8;

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "12345678";

    /**
     * 默认盐值
     */
    String SALT = "cao137831";

    // endregion

    // region 用户题目状态

    Integer MY_QUESTION_ACCEPTED = 1;

    Integer MY_QUESTION_NOT_ACCEPTED = 0;
    // endregion
}
