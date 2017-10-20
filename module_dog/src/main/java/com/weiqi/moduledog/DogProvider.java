package com.weiqi.moduledog;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.weiqi.modulebase.moduleinterface.provider.IDogProvider;

/**
 * Created by alexwangweiqi on 17/9/19.
 */
@Route(path = IDogProvider.DOG_SERVICE)
public class DogProvider implements IDogProvider{

    @Override
    public void init(Context context) {

    }

    @Override
    public String getDogName() {
        return "corgi";
    }
}
