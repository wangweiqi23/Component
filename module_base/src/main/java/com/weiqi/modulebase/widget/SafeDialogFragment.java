package com.weiqi.modulebase.widget;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 捕捉DialogFragment调用到FragmentTransaction#commit()可能抛出的异常
 * FragmentTransaction#commit()方法可能抛出异常：Can not perform this action after onSaveInstanceState
 * 注意：异步触发DialogFragment显示/消失的情况容易出现上述异常，必须处理
 * <p>
 * <p/>
 * Created by fengshzh on 16/7/13.
 */
public class SafeDialogFragment extends DialogFragment {

    @Override
    public void show(final FragmentManager manager, final String tag) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (manager.findFragmentByTag(tag) != null || isAdded()) {
                    return;
                }

                try {
                    SafeDialogFragment.super.show(manager, tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int show(final FragmentTransaction transaction, final String tag) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) return;
                try {
                    SafeDialogFragment.super.show(transaction, tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return -1;
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        try {
            super.onDismiss(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
