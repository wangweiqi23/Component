package com.weiqi.modulecat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.DataConstants;
import com.weiqi.modulebase.activity.BaseActivity;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;
import com.weiqi.modulebase.util.KeyUtil;
import com.weiqi.modulecat.R;

/**
 * Created by alexwangweiqi on 17/9/20.
 */
@Route(path = ICatProvider.CAT_ACTIVITY)
public class CatActivity extends BaseActivity {

    private int mAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        ARouter.getInstance().inject(this);
        initIntentData();
        initView();
    }

    @Override
    protected void initIntentData() {
        mAge = getIntent().getIntExtra(DataConstants.EXTRA_AGE, -1);
    }

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("CatActivity");
        ((TextView) findViewById(R.id.tv_name)).setText("name:Kitty");
        ((TextView) findViewById(R.id.tv_age)).setText(String.format("age:%s \naliPayId:%s",
                mAge, KeyUtil.getAlipayKey()));

        String foodNumb = null;
        try {
            //TODO 可以用工具类优化
            Context homeContext = this.createPackageContext("com.weiqi.test",
                    CONTEXT_IGNORE_SECURITY);
            SharedPreferences homeContextSharedPreferences = homeContext.getSharedPreferences
                    ("feeding", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
            foodNumb = homeContextSharedPreferences.getString("food", null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.tv_food)).setText(String.format("获取房间中的食物数量:%s", foodNumb));
    }

    @Override
    public void onContinue() {

    }
}
