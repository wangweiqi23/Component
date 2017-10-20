package com.weiqi.modulebase.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import com.weiqi.modulebase.util.CheckPhoneSystemUtil;
import com.weiqi.modulebase.util.rom.MiuiUtils;
import com.weiqi.modulebase.widget.ToastUtil;

/**
 * 用于从应用跳转到原生系统或第三方ROM的各种设置页面
 * Created by jiao.js on 16/8/18.
 */
public final class SettingNavigator {
    /**
     * 跳转Android原生系统设置页面
     */
    public static void skip(Context context) {
        String SCHEME = "package";
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME, context.getPackageName(), null);
        intent.setData(uri);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void skip(Activity activity, int requestCode) {
        String SCHEME = "package";
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME, activity.getPackageName(), null);
        intent.setData(uri);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转魅族,小米手机权限设置页面
     */
    public static void skipSpec(Context context) {
        Intent intent;
        if (CheckPhoneSystemUtil.isMIUI()) {
            intent = MiuiUtils.getMuUiPermissionActivity(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show("进入设置页面失败，请手动设置");
            }
        } else if (CheckPhoneSystemUtil.isFlyme()) {
            intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show("进入设置页面失败，请手动设置");
            }
        } else {
            skip(context);
        }
    }

    public static void skipWithResult(Activity activity, int requestCode) {
        Intent intent;
        if (CheckPhoneSystemUtil.isMIUI()) {
            intent = MiuiUtils.getMuUiPermissionActivity(activity);
            try {
                activity.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show("进入设置页面失败，请手动设置");
            }
        } else if (CheckPhoneSystemUtil.isFlyme()) {
            intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", activity.getPackageName());
            try {
                activity.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show("进入设置页面失败，请手动设置");
            }
        } else {
            skip(activity, requestCode);
        }
    }

    public static void startSystemSettingForResult(Fragment fragment, int requestCode) {
        Intent intent;
        if (CheckPhoneSystemUtil.isMIUI()) {
            intent = MiuiUtils.getMuUiPermissionActivity(fragment.getActivity());
            try {
                fragment.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show("进入设置页面失败，请手动设置");
            }
        } else if (CheckPhoneSystemUtil.isFlyme()) {
            intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", fragment.getContext().getPackageName());
            try {
                fragment.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show("进入设置页面失败，请手动设置");
            }
        } else {
            startNativeAndroidSettingForResult(fragment, requestCode);
        }
    }

    /**
     * 跳转Android原生系统设置页面
     */
    private static void startNativeAndroidSettingForResult(Fragment fragment, int requestCode) {
        String SCHEME = "package";
        final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
        final String APP_DETAILS_CLASS_NAME = "com.android.setting.InstalledAppDetails";
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME, fragment.getContext().getPackageName(), null);
        intent.setData(uri);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fragment.startActivityForResult(intent, requestCode);
    }


}
