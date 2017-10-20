package com.weiqi.modulehome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.activity.BaseActivity;
import com.weiqi.modulebase.manager.AccountManager;
import com.weiqi.modulebase.model.account.Account;
import com.weiqi.modulebase.moduleinterface.provider.IHomeProvider;
import com.weiqi.modulehome.R;

/**
 * Created by alexwangweiqi on 17/9/20.
 */
public class FirstActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
    }

    @Override
    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("FirstActivity");
        ((Button) findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AccountManager.isLogin()) {
                    Account account = new Account();
                    account.setName("admin");
                    account.setAge(3);
                    account.setLogin(true);
                    AccountManager.save(account);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ARouter.getInstance()
                                .build(IHomeProvider.HOME_ACTIVITY)
                                .navigation();
                    }
                }, 500);
            }
        });
    }


    @Override
    protected void initIntentData() {

    }

    @Override
    public void onContinue() {

    }

}
