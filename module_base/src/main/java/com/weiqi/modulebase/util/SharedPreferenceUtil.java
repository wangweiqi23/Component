package com.weiqi.modulebase.util;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 共享数据存取类
 */
public class SharedPreferenceUtil {

    private static String CONFIG_FILE_NAME = "eastmoney_global_config";
    private static SharedPreferences pref;

    static {
        pref = ContextUtil.getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean contains(String key) {
        return pref.contains(key);
    }

    /**
     * 重设指定字段
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 共享数据中存储数据
     *
     * @param key
     * @param value
     */
    public static void save(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 共享数据中获取数据
     *
     * @param key
     * @param defVal
     * @return
     */
    public static String get(String key, String defVal) {
        return pref.getString(key, defVal);
    }

    /**
     * 共享数据中存储数据
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 共享数据中获取数据
     *
     * @param key
     * @param defVal
     * @return
     */
    public static long getLong(String key, long defVal) {
        return pref.getLong(key, defVal);
    }

    /**
     * 共享数据中存储数据
     *
     * @param key
     * @param value
     */
    public static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 共享数据中存储数据
     *
     * @param key
     * @param value
     */
    public static void syncSaveInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 共享数据中获取数据
     *
     * @param key
     * @param defVal
     * @return
     */
    public static int getInt(String key, int defVal) {
        return pref.getInt(key, defVal);
    }

    /**
     * 共享数据中存储布尔型数据
     *
     * @param key
     * @param value
     */
    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 共享数据中获取布尔型数据
     *
     * @param key
     * @param defVal
     * @return
     */
    public static boolean getBoolean(String key, boolean defVal) {
        return pref.getBoolean(key, defVal);
    }

    /**
     * 清除本地数据
     */
    public static void clear() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}