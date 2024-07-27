package com.cao.oj.judge.codesandbox;

import com.cao.oj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.cao.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.cao.oj.judge.codesandbox.impl.ThirdPartyCodeSandbox;
import lombok.Data;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 代码沙箱创建工厂（根据字符串参数创建指定的代码沙箱实例）
 */
@Data
@Component
public class CodeSandboxFactory {

    private static RemoteCodeSandbox remoteCodeSandbox;

    private static ThirdPartyCodeSandbox thirdPartyCodeSandbox;

    private static ExampleCodeSandbox exampleCodeSandbox;

    @Resource
    public void setRemoteCodeSandbox(RemoteCodeSandbox newRemoteCodeSandbox) {
        remoteCodeSandbox = newRemoteCodeSandbox;
    }

    @Resource
    public void setThirdPartyCodeSandbox(ThirdPartyCodeSandbox newThirdPartyCodeSandbox) {
        thirdPartyCodeSandbox = newThirdPartyCodeSandbox;
    }

    @Resource
    public void setExampleCodeSandbox(ExampleCodeSandbox newExampleCodeSandbox) {
        exampleCodeSandbox = newExampleCodeSandbox;
    }

    /**
     * 创建代码沙箱实例
     * @param type 沙箱类型
     * @return 沙箱实例
     */
    public static CodeSandbox newInstance(String type){
        switch (type){
            case "remote":
                return remoteCodeSandbox;
            case "thirdParty":
                return thirdPartyCodeSandbox;
            default:
                return exampleCodeSandbox;
        }
    }
}
