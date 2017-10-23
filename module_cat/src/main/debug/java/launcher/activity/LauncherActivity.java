package launcher.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.DataConstants;
import com.weiqi.modulebase.activity.BaseLauncherActivity;
import com.weiqi.modulebase.manager.AccountManager;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;
import com.weiqi.modulebase.widget.ToastUtil;
import com.weiqi.modulecat.R;

/**
 * Created by alexwangweiqi on 17/9/5.
 */

public class LauncherActivity extends BaseLauncherActivity {

    private static final String TAG = LauncherActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("LauncherActivity");
        ((Button) findViewById(R.id.btn_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountManager.isLogin()) {
                    ARouter.getInstance().build(ICatProvider.CAT_ACTIVITY)
                            .withInt(DataConstants.EXTRA_AGE, 2)
                            .navigation();
                    LauncherActivity.this.finish();
                } else {
                    ToastUtil.show(getString(R.string.need_login));
                }
            }
        });
    }

    @Override
    public void onContinue() {
    }
}
