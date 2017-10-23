package com.weiqi.modulebase.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alexwangweiqi on 17/9/20.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        finishInitView();
    }

    protected abstract void initIntentData();

    protected abstract void initView();

    protected abstract void finishInitView();

}
