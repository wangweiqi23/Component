package com.weiqi.modulebase.moduleinterface.module;

import com.weiqi.modulebase.BuildConfig;
import com.weiqi.modulebase.moduleinterface.ModuleManager;
import com.weiqi.modulebase.moduleinterface.ServiceManager;
import com.weiqi.modulebase.moduleinterface.provider.IDogProvider;


/**
 * Created by jiao.js on 2017/9/8.
 */

public class DogModuleService {
    private static boolean hasModule() {
        return BuildConfig.isRunAlone && ModuleManager.getInstance().hasModule(IDogProvider
                .DOG_SERVICE);
    }

    public static String getDogName(){
        if (!hasModule()) return null;
        return ServiceManager.getInstance().getDogProvider().getDogName();
    }
}
