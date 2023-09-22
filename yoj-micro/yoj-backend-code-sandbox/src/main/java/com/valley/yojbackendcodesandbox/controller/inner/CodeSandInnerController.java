package com.valley.yojbackendcodesandbox.controller.inner;


import com.valley.yojbackendcodesandbox.sevice.CodeSandboxServiceFactory;
import com.valley.yojbackendcommon.common.ErrorCode;
import com.valley.yojbackendcommon.exception.BusinessException;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.valley.yojbackendmodel.model.codesandbox.ExecuteResponse;
import com.valley.yojbackendserviceclient.service.CodeSandboxFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner")
public class CodeSandInnerController implements CodeSandboxFeignClient {
    @Override
    @PostMapping("/exec")
    public ExecuteResponse execCode(@RequestBody ExecuteCodeRequest executeCodeRequest) {
        if (executeCodeRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        return CodeSandboxServiceFactory.newInstance(executeCodeRequest.getLanguage())
                .executeCode(executeCodeRequest);
    }
}
