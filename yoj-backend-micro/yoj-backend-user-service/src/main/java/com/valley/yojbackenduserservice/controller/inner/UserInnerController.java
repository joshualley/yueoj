package com.valley.yojbackenduserservice.controller.inner;

import com.valley.yojbackendcommon.common.ErrorCode;
import com.valley.yojbackendcommon.exception.BusinessException;
import com.valley.yojbackendmodel.model.entity.User;
import com.valley.yojbackendserviceclient.service.UserFeignClient;
import com.valley.yojbackenduserservice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 该服务仅内部调用
 */
@Controller("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 根据ID获取用户
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.getById(userId);
    }

    /**
     * 根据多个ID，获取多个用户
     * @param userIds
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("userIds") Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.listByIds(userIds);
    }
}
