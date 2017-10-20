package com.weiqi.modulebase.moduleinterface.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by alexwangweiqi on 17/9/19.
 */

public interface ICatProvider extends IProvider {

    String CAT_SERVICE = "/cat/service/catmodule";

    String CAT_ACTIVITY = "/cat/activity/cat";
    String CAT_FRAGMENT = "/cat/fragment/cat";

    int getCatAge(String catName);
}
