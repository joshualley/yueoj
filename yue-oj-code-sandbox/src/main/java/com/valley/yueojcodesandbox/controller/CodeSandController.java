package com.valley.yueojcodesandbox.controller;

import com.valley.yueojcodesandbox.model.ExecuteCodeRequest;
import com.valley.yueojcodesandbox.model.ExecuteResponse;
import com.valley.yueojcodesandbox.sevice.CodeSandboxService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/")
public class CodeSandController {
    @Resource(name = "docker")
    CodeSandboxService codeSandboxService;

    @PostMapping("/codesandbox")
    public ExecuteResponse execCode(@RequestBody ExecuteCodeRequest request) {
        return codeSandboxService.executeCode(request);
    }
}
