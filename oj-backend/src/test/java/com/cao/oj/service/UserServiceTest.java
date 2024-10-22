package com.cao.oj.service;

import javax.annotation.Resource;

import org.apache.ibatis.cache.TransactionalCacheManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务测试
 *

 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {
//        String userAccount = "yupi";
//        String userPassword = "";
//        String checkPassword = "123456";
//        try {
//            long result = userService.register(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            userAccount = "yu";
//            result = userService.register(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//        } catch (Exception e) {
//
//        }
    }

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Test
    void testTransactionManager() {
        String string = transactionManager.toString();
        System.out.println(string);
    }

}
