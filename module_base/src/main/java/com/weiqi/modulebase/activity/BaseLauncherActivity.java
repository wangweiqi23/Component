package com.weiqi.modulebase.activity;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.weiqi.modulebase.R;
import com.weiqi.modulebase.manager.AccountManager;
import com.weiqi.modulebase.util.LauncherPermissionManager;
import com.weiqi.modulebase.widget.ToastUtil;

/**
 * Created by alexwangweiqi on 17/9/20.
 */

public abstract class BaseLauncherActivity extends BaseActivity implements LauncherPermissionManager
        .CheckPermissionCallback {

    private LauncherPermissionManager mPermissionManager;

    protected void finishInitView() {
        if (AccountManager.isLogin()) {
            mPermissionManager = new LauncherPermissionManager(this, this);
            mPermissionManager.isFirstOpen();
            mPermissionManager.checkPermissionForContinue();
        } else {
            ToastUtil.show(R.string.need_login);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.checkReqPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionManager.checkActivityResultFromPermissionSetPage(requestCode);
    }

    @Override
    public void onContinue() {

    }

    protected abstract void initIntentData();

    protected abstract void initView();

}
