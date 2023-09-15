package com.valley.yueojcodesandbox.security;

import java.security.Permission;

/**
 * Java安全管理器，禁止所有权限
 */
public class DenySecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
        throw new SecurityException("限制权限" + perm.toString());
    }
}
