package com.valley.yojbackendserviceclient.service;


import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 判题服务
 */
@FeignClient(name = "yoj-backend-code-sandbox", path = "/api/codesandbox/inner")
public interface CodeSandboxFeignClient {

    /**
     * 判题
     * @param executeCodeRequest 执行代码请求
     * @return
     */
    @PostMapping("/exec")
    ExecuteResponse execCode(@RequestBody ExecuteCodeRequest executeCodeRequest);
}
