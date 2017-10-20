package com.weiqi.modulebase.moduleinterface;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexwangweiqi on 17/9/19.
 */

public class ModuleConfig {
    private Map<String, String> mImmutableModules;

    public ModuleConfig(ModuleBuilder moduleBuilder) {
        mImmutableModules = moduleBuilder.immutableModules;
    }

    public boolean hasModule(String key) {
        if (mImmutableModules == null) {
            return false;
        }
        return mImmutableModules.containsKey(key);
    }

    public static class ModuleBuilder {
        private Map<String, String> immutableModules;

        public ModuleBuilder() {
            immutableModules = new HashMap<>();
        }

        public ModuleBuilder addModule(@NonNull String key, @NonNull String value) {
            immutableModules.put(key, value);
            return this;
        }

        public ModuleConfig build() {
            return new ModuleConfig(this);
        }
    }
}
