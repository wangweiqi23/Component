package com.weiqi.modulebase.moduleinterface;

/**
 * Created by alexwangweiqi on 17/9/19.
 */

public class ModuleManager {

    private ModuleConfig mModuleConfig;

    private ModuleManager() {

    }

    private static class ModuleManagerHolder {
        private static final ModuleManager instance = new ModuleManager();
    }

    public static ModuleManager getInstance() {
        return ModuleManagerHolder.instance;

    }

    public void init(ModuleConfig options) {
        if (this.mModuleConfig == null && options != null) {
            this.mModuleConfig = options;
        }
    }

    public ModuleConfig getConfgi() {
        return mModuleConfig;
    }

    public boolean hasModule(String key) {
        return mModuleConfig.hasModule(key);
    }

}
