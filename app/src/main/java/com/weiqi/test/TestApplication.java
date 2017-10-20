package com.weiqi.test;

import android.app.Application;

import com.weiqi.modulebase.BaseApplicationHelper;

/**
 * Created by alexwangweiqi on 17/9/19.
 */

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        boolean debug = BuildConfig.DEBUG;
        init(debug, this);
    }

    private void init(boolean debug, Application context) {

        BaseApplicationHelper.init(debug, context);

    }

    static {
        System.loadLibrary("keyutil");
    }
}
