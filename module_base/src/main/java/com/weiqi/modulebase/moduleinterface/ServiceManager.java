package com.weiqi.modulebase.moduleinterface;

import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;
import com.weiqi.modulebase.moduleinterface.provider.IDogProvider;
import com.weiqi.modulebase.moduleinterface.provider.IHomeProvider;

/**
 * Created by alexwangweiqi on 17/9/19.
 */

public class ServiceManager {

    private IHomeProvider mIHomeProvider;
    private ICatProvider mISocialProvider;
    private IDogProvider mILiveProvider;

    public ServiceManager() {
        ARouter.getInstance().inject(this);
    }

    private static final class ServiceManagerHolder {
        private static final ServiceManager instance = new ServiceManager();
    }

    public static ServiceManager getInstance() {
        return ServiceManagerHolder.instance;
    }

    public IHomeProvider getHomeProvider() {
        return mIHomeProvider != null ? mIHomeProvider : (mIHomeProvider = (IHomeProvider) ARouter.getInstance().
                build(IHomeProvider.HOME_SERVICE).navigation());
    }

    public ICatProvider getCatProvider() {
        return mISocialProvider != null ? mISocialProvider :
                (mISocialProvider = (ICatProvider) ARouter.getInstance()
                        .build(ICatProvider.CAT_SERVICE).navigation());
    }

    public IDogProvider getDogProvider() {
        return mILiveProvider != null ? mILiveProvider :
                (mILiveProvider = (IDogProvider) ARouter.getInstance()
                        .build(IDogProvider.DOG_SERVICE).navigation());
    }

}
