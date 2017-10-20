/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.weiqi.modulebase.util.rom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Vivo x9i, Android 6.0.1默认前台给权限，后台无权限（onStop()可判权限无）。
 * 跳转悬浮窗权限设置打开悬浮窗权限，仅对此次后台生效。进入系统权限设置页面，显示App仍未获取悬浮窗权限。下次再切后台，仍无悬浮窗权限。
 * 跳转权限设置主页，设置有效。
 */
public final class VivoUtils {
    private static final String TAG = "VivoUtils";

    /**
     * 去权限申请页面
     */
    public static void applyPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.iqoo.secure",
                "com.iqoo.secure.safeguard.SoftPermissionDetailActivity");
        intent.putExtra("packagename", context.getPackageName());
        if (isIntentAvailable(intent, context)) {
            FloatWindowManager.getInstance().startActivityOrForResult(context, intent);
        } else {
                Log.e(TAG, "can't open permission page with particular name, please use " +
                        "\"adb shell dumpsys activity\" command and tell me the name of the float window permission page");
        }
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
