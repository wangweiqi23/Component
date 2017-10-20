package com.weiqi.modulecat;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;

/**
 * Created by alexwangweiqi on 17/9/19.
 */
@Route(path = ICatProvider.CAT_SERVICE)
public class CatProvider implements ICatProvider {


    private final int kittyAge = 6;

    @Override
    public void init(Context context) {

    }

    @Override
    public int getCatAge(String catName) {
        if (TextUtils.equals(catName, "Kitty")) {
            return kittyAge;
        }
        return 0;
    }


}
