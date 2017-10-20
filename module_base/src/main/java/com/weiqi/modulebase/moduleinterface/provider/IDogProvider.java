package com.weiqi.modulebase.moduleinterface.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by alexwangweiqi on 17/9/19.
 */

public interface IDogProvider extends IProvider {

    String DOG_SERVICE = "/dog/service/dogmodule";

    String DOG_FRAGMENT = "/dog/fragment/dog";

    String getDogName();
}
