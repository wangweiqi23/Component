package com.weiqi.modulebase.util.peimission;

/**
 * 授权回调
 * Created by fengshzh on 05/04/2017.
 */
public interface PermissionCallback {
    /**
     * 授权成功
     */
    void onPermissionsGranted();

    /**
     * 授权失败
     */
    void onPermissionsDenied();
}
