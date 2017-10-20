package com.weiqi.modulebase.util;

import android.os.Build;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 检查手机系统的工具类
 * Created by jiao.js on 16/8/18.
 */
public final class CheckPhoneSystemUtil {
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_EMUI_VERSION_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    private static boolean isPropertiesExist(String... keys) {
        try {
            BuildProperties prop = BuildProperties.newInstance();
            boolean isSpecRom = false;
            for (String key : keys) {
                String str = prop.getProperty(key);
                if (str == null) {
                    isSpecRom |= false;
                } else {
                    isSpecRom |= true;
                }
            }
            return isSpecRom;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 检查是否是华为系统
     */
    public static boolean isEMUI() {
        return isPropertiesExist(KEY_EMUI_VERSION_CODE, KEY_EMUI_VERSION_API_LEVEL);
    }

    /**
     * 检测是否是小米系统
     */
    public static boolean isMIUI() {
        try {

            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null ||
                    prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null ||
                    prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 检测是否是魅族系统
     */
    public static boolean isFlyme() {
        try {

            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

}
