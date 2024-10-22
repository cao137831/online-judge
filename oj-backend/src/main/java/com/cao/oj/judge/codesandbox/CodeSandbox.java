package com.cao.oj.judge.codesandbox;

import com.cao.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 * @author cao13
 */
public interface CodeSandbox {
    /**
     * 执行代码
     * @param executeCodeRequest 执行代码所需的参数，(输入列表，代码片段，语言类型)
     * @return 执行结果
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
