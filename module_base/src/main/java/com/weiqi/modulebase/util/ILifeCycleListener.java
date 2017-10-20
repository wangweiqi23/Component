package com.weiqi.modulebase.util;

/**
 * Created by jiao.js on 2017/6/29.
 */

public interface ILifeCycleListener {
    void onBaseResume();

    void onBasePause();

    void onBaseDestroy();
}
