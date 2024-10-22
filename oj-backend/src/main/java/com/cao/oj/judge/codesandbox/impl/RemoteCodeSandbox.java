package com.cao.oj.judge.codesandbox.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cao.oj.common.ErrorCode;
import com.cao.oj.exception.BusinessException;
import com.cao.oj.judge.codesandbox.CodeSandbox;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cao.oj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cao.oj.constant.JudgeConstant.AUTH_REQUEST_HEADER;
import static com.cao.oj.constant.JudgeConstant.AUTH_REQUEST_SECRET;

/**
 * 远程代码沙箱（实际调用的沙箱）
 * @author cao13
 */
@Component
//@ConfigurationProperties(prefix = "codesandbox")
//@Data
public class RemoteCodeSandbox implements CodeSandbox {

    @Value("${codesandbox.host}")
    private String host;

    @Value("${codesandbox.port}")
    private String port;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        String url = "http://" + host + ":" + port + "/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        HttpRequest request = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json);
        HttpResponse response = request.execute(true);
        String responseStr = response.body();

        if (!response.isOk()) {
            String error = "调用远程代码沙箱失败，请求头 => " + response.headers() + "请求体 => " + response.body();
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, error);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }

    public static void main(String[] args) {
//        String url = "http://124.70.69.159:8090/executeCode";
        String url = "http://192.168.56.101:8090/executeCode";
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setLanguage("go");
        List<String> inputList = new ArrayList<>();
        inputList.add("1 1");
        inputList.add("12 454");
        executeCodeRequest.setInputList(inputList);
        executeCodeRequest.setCode("package main\n" +
                "\n" +
                "import (\n" +
                "\t\"fmt\"\n" +
                ")\n" +
                "\n" +
                "func main() {\n" +
                "\tvar a, b int\n" +
                "\tfmt.Scan(&a, &b)\n" +
                "\tfmt.Println(a / b)\n" +
                "}\n");
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
        System.out.println(executeCodeResponse);
    }
}
