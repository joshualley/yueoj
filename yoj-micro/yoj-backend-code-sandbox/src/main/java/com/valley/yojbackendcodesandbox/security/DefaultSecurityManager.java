package com.valley.yojbackendcodesandbox.security;

import java.security.Permission;

/**
 * Java安全管理器
 */
public class DefaultSecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
        //super.checkPermission(perm);
    }

    @Override
    public void checkExec(String cmd) {
        super.checkExec(cmd);
    }

    @Override
    public void checkRead(String file) {
        super.checkRead(file);
    }

    @Override
    public void checkWrite(String file) {
        super.checkWrite(file);
    }

    @Override
    public void checkDelete(String file) {
        super.checkDelete(file);
    }

    @Override
    public void checkConnect(String host, int port) {
        super.checkConnect(host, port);
    }


}
