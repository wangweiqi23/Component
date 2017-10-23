package com.weiqi.modulehome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.activity.BaseLauncherActivity;
import com.weiqi.modulebase.manager.AccountManager;
import com.weiqi.modulebase.model.account.Account;
import com.weiqi.modulebase.moduleinterface.provider.IHomeProvider;
import com.weiqi.modulebase.widget.ToastUtil;
import com.weiqi.modulehome.R;
import com.weiqi.slog.SLog;

/**
 * Created by alexwangweiqi on 17/9/20.
 */
public class FirstActivity extends BaseLauncherActivity {

    private static final String TAG = FirstActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    @Override
    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("FirstActivity");
        ((Button) findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLog.d(TAG, "login:" + AccountManager.isLogin());
                if (!AccountManager.isLogin()) {
                    Account account = new Account();
                    account.setName("admin");
                    account.setAge(3);
                    account.setLogin(true);
                    if (AccountManager.save(account)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startHome();
                            }
                        }, 500);
                    } else {
                        ToastUtil.show("模拟登录失败");
                    }
                } else {
                    startHome();
                }

            }
        });
    }


    @Override
    protected void initIntentData() {

    }

    @Override
    public void onContinue() {

    }

    private void startHome() {
        ARouter.getInstance()
                .build(IHomeProvider.HOME_ACTIVITY)
                .navigation();
        this.finish();
    }

}
