package com.weiqi.modulebase.util;

import android.content.Context;

import com.weiqi.slog.SLog;

/**
 * ContextUtil，为Android程序提供全局Context对象，要在Application.onCreate中初始化。
 */
public final class ContextUtil {
    private static boolean isRunAlone;

    private static Context context;
    private static Context dbContext;

    public static void init(boolean ConfigIsRunAlone, Context appContext) {
        isRunAlone = ConfigIsRunAlone;
        context = appContext.getApplicationContext();
        dbContext = new DBContext(appContext);
    }

    public static Context getContext() {
        return context;
    }

    public static Context getDBContext() {
        SLog.d("ContextUtil", "getDBContext isRunAlone:" + isRunAlone);
        if (!isRunAlone) {
            return context;
        }
        return dbContext;
    }
}