package com.valley.yueojcodesandbox.controller;

import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import com.valley.yueojcodesandbox.sevice.CodeSandboxService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController("/")
public class CodeSandController {

    /**
     * 定义鉴权请求头
     */
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Resource(name = "docker")
    CodeSandboxService codeSandboxService;

    @PostMapping("/codesandbox")
    public ExecuteResponse execCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                    HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(header)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return codeSandboxService.executeCode(executeCodeRequest);
    }
}
