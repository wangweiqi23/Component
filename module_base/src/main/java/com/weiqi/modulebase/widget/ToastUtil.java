package com.weiqi.modulebase.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Toast;

import com.weiqi.modulebase.util.CheckPhoneSystemUtil;
import com.weiqi.slog.SLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Toast工具类
 * <p>
 * 若需要显示Toast的同时还要对Activity进行finish 若Toast必须显示 则需将ToastUtil放在finish()之后调用
 * </p>
 * Created by fengshzh on 4/21/16.
 */
public class ToastUtil {

    private static final String TAG = ToastUtil.class.getSimpleName();

    //原来的实现方案 用来维持异常情况下 Toast的正常显示
    private static Context mContext;
    private static boolean sUseSystemToast = true;

    private static List<WeakReference<Activity>> weakReferenceList = new ArrayList<>();
//    private static HeaderToast mHeaderToast;

    public static void create(@NonNull Activity activity) {

        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        SLog.i(TAG, "toast activity add");
        weakReferenceList.add(activityWeakReference);
    }

    public static void destroy(Activity activity) {
        if (weakReferenceList != null && weakReferenceList.size() >= 1) {
            if (activity != null) {
                int size = weakReferenceList.size();
                for (int i = size - 1; i >= 0; i--) {
                    SLog.i(TAG, "toast i" + i);
                    if (weakReferenceList.get(i).get() == activity) {
                        SLog.i(TAG, "toast" + weakReferenceList.get(i).get().getLocalClassName());
                        weakReferenceList.remove(i);
                        break;
                    }
                }
            } else {
                SLog.i(TAG, "toast" + weakReferenceList.get(weakReferenceList.size() - 1).get()
                        .getLocalClassName());
                weakReferenceList.remove(weakReferenceList.size() - 1);
            }
        }
    }


    public static void init(Context context) {
        mContext = context;
        sUseSystemToast = useSystemToast();
    }

    public static void show(final String content) {
        show(content, HeaderToast.NORMAL);
    }

    public static void show(final String content, final int flag) {
        //过滤空消息
        if (TextUtils.isEmpty(content)) {
            return;
        }

        if (getActivityDecorView() != null && getActivityDecorView().getWindowToken() != null
                && !isFinishing()) {
            SLog.i(TAG, "toast show activity window");
            getCurActivityWeakReference().get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HeaderToast headerToast = new HeaderToast(getCurActivityWeakReference().get(),
                            getActivityDecorView());
                    headerToast.show(content, flag);
                }
            });
        } else {
            SLog.i(TAG, "toast show application window");
            if (!sUseSystemToast) {
                HeaderToast toast = new HeaderToast(mContext);
                toast.show(content, flag);
            } else {
                Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void show(@StringRes int strResId) {
        if (strResId != 0) {
            show(mContext.getString(strResId));
        }
    }


    public static void showSlide(String content) {
        HeaderToast toast = new HeaderToast(mContext, HeaderToast.TYPE_SLIDE);
        toast.show(content);
    }

    public static void showSlide(@StringRes int strResId) {
        HeaderToast toast = new HeaderToast(mContext, HeaderToast.TYPE_SLIDE);
        toast.show(mContext.getResources().getString(strResId));
    }

    public static void showNetworkError() {
        show("网络不佳");
    }


    // 是否应该使用系统默认Toast
    private static boolean useSystemToast() {
        return CheckPhoneSystemUtil.isMIUI() || Build.VERSION.SDK_INT >= 25;
    }

    public static void showSysToast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    private static WeakReference<Activity> getCurActivityWeakReference() {
        if (weakReferenceList != null && weakReferenceList.size() >= 1) {
            return weakReferenceList.get(weakReferenceList.size() - 1);
        } else {
            return null;
        }
    }

    private static boolean isFinishing() {
        if (getCurActivityWeakReference() == null || getCurActivityWeakReference().get() == null ||
                getCurActivityWeakReference().get().isFinishing()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the enclosing Decor View
     *
     * @return The Decor View of the Activity the Alerter was called from
     */
    @Nullable
    private static ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;

        if (getCurActivityWeakReference() != null && getCurActivityWeakReference().get() != null) {
            decorView = (ViewGroup) getCurActivityWeakReference().get().getWindow().getDecorView();
        }

        return decorView;
    }


}
