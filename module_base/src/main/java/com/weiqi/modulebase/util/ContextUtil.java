package com.weiqi.modulebase.util;

import android.content.Context;

/**
 * ContextUtil，为Android程序提供全局Context对象，要在Application.onCreate中初始化。
 */
public final class ContextUtil {
    private static Context context;
    private static Context dbContext;

    public static void init(Context appContext) {
        context = appContext.getApplicationContext();
        dbContext = new DBContext(appContext);
    }

    public static Context getContext() {
        return context;
    }

    public static Context getDBContext() {
        return dbContext;
    }
}