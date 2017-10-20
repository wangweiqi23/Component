package com.weiqi.modulebase.moduleinterface.module;

import com.weiqi.modulebase.BuildConfig;
import com.weiqi.modulebase.moduleinterface.ModuleManager;
import com.weiqi.modulebase.moduleinterface.provider.IHomeProvider;


/**
 * Created by jiao.js on 2017/9/8.
 */

public class HomeModuleService {
    private static boolean hasModule() {
        return BuildConfig.isRunAlone && ModuleManager.getInstance().hasModule(IHomeProvider
                .HOME_SERVICE);
    }

}
