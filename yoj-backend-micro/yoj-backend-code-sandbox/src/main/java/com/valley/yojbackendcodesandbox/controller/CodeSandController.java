package com.valley.yojbackendcodesandbox.controller;


import com.valley.yojbackendcodesandbox.sevice.CodeSandboxService;
import com.valley.yojbackendcommon.constant.CodeSandboxAuthConstant;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class CodeSandController {
    @Resource(name = "docker")
    CodeSandboxService codeSandboxService;

    @PostMapping("/exec")
    public ExecuteResponse execCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                    HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(CodeSandboxAuthConstant.AUTH_REQUEST_HEADER);
        if (!CodeSandboxAuthConstant.AUTH_REQUEST_SECRET.equals(header)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return codeSandboxService.executeCode(executeCodeRequest);
    }
}
