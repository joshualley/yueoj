package com.yupi.yueoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.yueoj.common.ErrorCode;
import com.yupi.yueoj.exception.BusinessException;
import com.yupi.yueoj.judge.codesandbox.CodeSandbox;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yueoj.judge.codesandbox.model.ExecuteResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {

    /**
     * 鉴权请求头
     */
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteResponse executeCode(ExecuteCodeRequest request) {
        String url = "http://localhost:8090/codesandbox";
        String body = HttpUtil.createPost(url)
                .body(JSONUtil.toJsonStr(request))
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .execute()
                .body();
        if (StringUtils.isBlank(body)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "调用远程代码沙箱失败");
        }
        return JSONUtil.toBean(body, ExecuteResponse.class);
    }
}
