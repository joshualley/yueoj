package com.valley.yojbackendcodesandbox.security;

import java.security.Permission;

/**
 * Java安全管理器
 */
public class MySecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {}

    @Override
    public void checkExec(String cmd) {
        super.checkExec(cmd);
        throw new SecurityException("Exec权限异常：" + cmd);
    }

    @Override
    public void checkRead(String file) {
        super.checkRead(file);
        throw new SecurityException("Read权限异常：" + file);
    }

    @Override
    public void checkWrite(String file) {
        super.checkWrite(file);
        throw new SecurityException("Write权限异常：" + file);
    }

    @Override
    public void checkDelete(String file) {
        super.checkDelete(file);
        throw new SecurityException("Delete权限异常：" + file);
    }

    @Override
    public void checkConnect(String host, int port) {
        super.checkConnect(host, port);
        throw new SecurityException("Connect权限异常：" + host + ":" + port);
    }


}
