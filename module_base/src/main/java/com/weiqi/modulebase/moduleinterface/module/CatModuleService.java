package com.weiqi.modulebase.moduleinterface.module;

import com.weiqi.modulebase.BuildConfig;
import com.weiqi.modulebase.moduleinterface.ModuleManager;
import com.weiqi.modulebase.moduleinterface.ServiceManager;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;


/**
 * Created by jiao.js on 2017/9/8.
 */

public class CatModuleService {
    private static boolean hasModule() {
        return !BuildConfig.isRunAlone && ModuleManager.getInstance().hasModule(ICatProvider
                .CAT_SERVICE);
    }

    public static int getCatAge(String catName){
        if (!hasModule()) return -1;
        return ServiceManager.getInstance().getCatProvider().getCatAge(catName);
    }

}
