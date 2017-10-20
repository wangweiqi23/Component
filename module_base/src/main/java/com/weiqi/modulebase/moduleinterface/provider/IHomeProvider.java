package com.weiqi.modulebase.moduleinterface.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by alexwangweiqi on 17/9/19.
 */

public interface IHomeProvider extends IProvider {

    String HOME_SERVICE = "/home/service/homemodule";

    //主模块
    String FIRST_ACTIVITY = "/home/activity/first";
    String HOME_ACTIVITY = "/home/activity/home";
}
