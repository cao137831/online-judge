package com.cao.oj.judge.codesandbox;

import com.cao.oj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.cao.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.cao.oj.judge.codesandbox.impl.ThirdPartyCodeSandbox;
import org.apache.commons.lang3.ThreadUtils;

/**
 * 代码沙箱创建工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱实例
     * @param type 沙箱类型
     * @return 沙箱实例
     */
    public static CodeSandbox newInstance(String type){
        switch (type){
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
