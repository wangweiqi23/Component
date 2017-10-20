package com.weiqi.modulebase.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqi.modulebase.R;
import com.weiqi.modulebase.util.DensityUtil;
import com.weiqi.slog.SLog;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by peijiadi on 16/6/7.
 */
public class HeaderToast implements View.OnClickListener {

    private static final String TAG = HeaderToast.class.getSimpleName();

    public final static int TYPE_FADE = 0;//淡入淡出动画
    public final static int TYPE_SLIDE = 1;//滑入滑出动画
    private final static int CLOSE = 0;

    private final static int ANIM_DURATION = 600;
    private final static int SHOW_DURATION = 2000;
    private final static int SHOW_MORE_DURATION = 3000;

    public final static int NORMAL = 0;
    public final static int SUCCESS = 1;
    public final static int ERROR = 2;


    private final Context mContext;
    private View mHeaderToastView;
    private WindowManager wm;
    private LinearLayout linearLayout;
    private FrameLayout frameLayout;
    private float downX;
    private float downY;
    private TextView mTvToast;
    private Handler mHeaderToastHandler;
    private boolean mFallBack;

    private int mType;
    private View mHeaderBgView;
    private boolean isDismissing = false;

    private WeakReference<ViewGroup> mDecorViewRef;


    public HeaderToast(Context context) {
        //默认淡入淡出动画
        this(context, TYPE_SLIDE);
    }

    public HeaderToast(Context context, int type) {
        //使用applicationContext保证Activity跳转时Toast不会消失
        this.mContext = context.getApplicationContext();
        mHeaderToastHandler = new MyHandler(this);
        mType = type;
    }

    public HeaderToast(Context context, ViewGroup contentView) {
        this(context, TYPE_SLIDE);
        mDecorViewRef = new WeakReference<>(contentView);
    }

    public void show(String toast) {
        showHeaderToast(toast, NORMAL);
    }

    public void show(String toast, int flag) {
        showHeaderToast(toast, flag);
    }

    private synchronized void showHeaderToast(String toast, int flag) {
        if (mDecorViewRef != null && mDecorViewRef.get() != null) {
            initHeaderToastView(flag, mDecorViewRef.get());
        } else {
            initHeaderToastView(flag);
        }

        if (mFallBack) {
            //fallback to system toast
            SLog.i(TAG, "@Jiao yuanshi");
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            mHeaderToastView.setVisibility(View.GONE);
        } else {
            setText(toast);
            setHeaderViewInAnim();
            //2s后自动关闭
            if (!toast.contains("\n")) {
                mHeaderToastHandler.sendEmptyMessageDelayed(CLOSE, SHOW_DURATION);
            } else {
                mHeaderToastHandler.sendEmptyMessageDelayed(CLOSE, SHOW_MORE_DURATION);
            }
        }

    }

    private synchronized void showHeaderToast(String toast, int flag, ViewGroup contentView) {

        showHeaderToast(toast, flag);

    }

    //    public void showWithCustomIcon(int icon, String toast){
    //        showHeaderToastWithCustomIcon(icon, toast);
    //    }
    //
    //    private synchronized void showHeaderToastWithCustomIcon(int icon, String toast) {
    //        initHeaderToastView();
    //
    //        setText(toast);
    //
    //        setHeaderViewInAnim();
    //
    //        //2s后自动关闭
    //        mHeaderToastHandler.sendEmptyMessageDelayed(CLOSE, SHOW_DURATION);
    //    }

    /**
     * 为mHeaderToastView添加进入动画
     */
    private void setHeaderViewInAnim() {
        if (mType == TYPE_SLIDE) {
            ObjectAnimator a = ObjectAnimator.ofFloat(mHeaderToastView, "translationY", -700, 0);
            a.setDuration(ANIM_DURATION);
            a.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mHeaderToastView, "alpha", 0.0f, 1.0f);
            animator.setDuration(ANIM_DURATION);
            animator.start();
        }
    }

    private void setText(String toast) {
        mTvToast.setText(toast);
    }

    private void initHeaderToastView(int flag) {
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        //为mHeaderToastView添加parent使其能够展示动画效果

        linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(llParams);

        mHeaderToastView = View.inflate(mContext, R.layout.header_toast, null);
        mHeaderBgView = mHeaderToastView.findViewById(R.id.header_bg);
        mTvToast = (TextView) mHeaderToastView.findViewById(R.id.tv_header_toast);
        switch (flag) {
            case NORMAL:
                mHeaderBgView.setBackgroundColor(calculateStatusColor(
                        mContext.getResources().getColor(R.color.toast_normal),
                        112));
//                mHeaderToastView.setBackgroundColor(Color.parseColor("#75000000"));
//                mTvToast.setBackgroundColor(calculateStatusColor(
//                        mContext.getResources().getColor(R.color.toast_normal),
//                        112));
                break;
            case SUCCESS:
                mHeaderBgView.setBackgroundColor(calculateStatusColor(
                        mContext.getResources().getColor(R.color.toast_normal),
                        112));
//                mHeaderToastView.setBackgroundColor(Color.parseColor("#3381e3"));
//                mTvToast.setBackgroundColor(calculateStatusColor(
//                        mContext.getResources().getColor(R.color.toast_normal),
//                        112));
                break;
            case ERROR:
                mHeaderBgView.setBackgroundColor(calculateStatusColor(Color.parseColor("#ff3b2f")
                        , 0));
//                mHeaderToastView.setBackgroundColor(Color.parseColor("#75000000"));
//                mTvToast.setBackgroundColor(calculateStatusColor(Color.parseColor("#ff3b2f"), 0));
                break;
        }
        //为mHeaderToastView添加滑动删除事件
        mHeaderToastView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float currentX = event.getRawX();
                        float currentY = event.getRawY();
                        if ((downX - currentX) >= 40 || (downY - currentY) >= 10) {
                            animDismiss();
                        }
                        break;
                }
                return true;
            }
        });
        if (mType == TYPE_SLIDE) {
            RelativeLayout.LayoutParams rootRL = (RelativeLayout.LayoutParams) mTvToast
                    .getLayoutParams();

            rootRL.topMargin = DensityUtil.getStatusBarHeight(mContext);
            mTvToast.setLayoutParams(rootRL);

//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTvToast
//                    .getLayoutParams();
//            layoutParams.topMargin = DensityUtil.getStatusBarHeight(mContext);
//            mTvToast.setLayoutParams(layoutParams);
        }

        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        if (mType == TYPE_SLIDE) {
            wmParams.flags = WindowManager.LayoutParams
                    .FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.y = 0;
        } else {
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.y = 0;
        }
        wmParams.gravity = Gravity.CENTER | Gravity.TOP;
        wmParams.x = 0;
//        if (contentView != null && contentView.getWindowToken() != null) {
//            wmParams.token = contentView.getWindowToken();
//            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
//        } else {
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//        }

        wmParams.format = PixelFormat.TRANSPARENT;

        linearLayout.addView(mHeaderToastView);

        try {
            wm.addView(linearLayout, wmParams);
        } catch (Exception e) {
            SLog.i(TAG, "@Jiao exception:" + e);
            mFallBack = true;
        }
    }

    private void initHeaderToastView(int flag, final ViewGroup decorView) {
        //为mHeaderToastView添加parent使其能够展示动画效果

        frameLayout = new FrameLayout(decorView.getContext());
        FrameLayout.LayoutParams llParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.gravity = Gravity.TOP;
        frameLayout.setLayoutParams(llParams);
        frameLayout.setHapticFeedbackEnabled(true);
        mHeaderToastView = View.inflate(frameLayout.getContext(), R.layout.header_toast, null);
        mHeaderBgView = mHeaderToastView.findViewById(R.id.header_bg);
        mTvToast = (TextView) mHeaderToastView.findViewById(R.id.tv_header_toast);
        switch (flag) {
            case NORMAL:
                mHeaderBgView.setBackgroundColor(calculateStatusColor(
                        mContext.getResources().getColor(R.color.toast_normal),
                        112));
                break;
            case SUCCESS:
                mHeaderBgView.setBackgroundColor(calculateStatusColor(
                        mContext.getResources().getColor(R.color.toast_normal),
                        112));
                break;
            case ERROR:
                mHeaderBgView.setBackgroundColor(calculateStatusColor(Color.parseColor("#ff3b2f")
                        , 0));
                break;
        }
        //为mHeaderToastView添加滑动删除事件
        mHeaderToastView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                return true;
            }
        });
        mHeaderToastView.setOnClickListener(this);
        if (mType == TYPE_SLIDE) {
            RelativeLayout.LayoutParams rootRL = (RelativeLayout.LayoutParams) mTvToast
                    .getLayoutParams();

            rootRL.topMargin = DensityUtil.getStatusBarHeight(mContext);
            mTvToast.setLayoutParams(rootRL);
        }
        frameLayout.addView(mHeaderToastView);
        if (decorView != null && frameLayout.getParent() == null) {
            decorView.addView(frameLayout);
        } else {
            mFallBack = true;
        }
    }

    @Override
    public void onClick(View v) {
        SLog.i(TAG, "toast onclick");
        animDismiss();
    }

    private static class MyHandler extends Handler {

        private SoftReference<HeaderToast> mheaderToastRef;

        private MyHandler(HeaderToast headerToast) {
            mheaderToastRef = new SoftReference<>(headerToast);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HeaderToast headerToast = mheaderToastRef.get();
            if (headerToast == null) {
                removeCallbacksAndMessages(null);
                return;
            }
            switch (msg.what) {
                case CLOSE:
                    headerToast.animDismiss();
                    break;
                default:
                    SLog.i(TAG, "no selection matches");
                    break;
            }
        }
    }

    /**
     * HeaderToast消失动画
     */
    private void animDismiss() {
        if (isDismissing) {
            return;
        }
        if ((null == linearLayout || null == linearLayout.getParent()) &&
                (frameLayout == null || frameLayout.getParent() == null)) {
            //如果linearLayout已经被从wm中移除，直接return
            return;
        }
        isDismissing = true;

        ObjectAnimator a;
        if (mType == TYPE_SLIDE) {
            a = ObjectAnimator.ofFloat(mHeaderToastView, "translationY", 0, -700);
        } else {
            a = ObjectAnimator.ofFloat(mHeaderToastView, "alpha", 1.0f, 0.0f);
        }
        a.setDuration(ANIM_DURATION);
        a.start();
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dismiss();
                        } catch (Exception e) {
                            SLog.e(TAG, e.getMessage());
                        }
                    }
                });
            }
        });


    }

    /**
     * 移除HeaderToast
     */
    public void dismiss() {
        if (null != linearLayout && null != linearLayout.getParent()) {
            SLog.i(TAG, "toast remove linearLayout");
            wm.removeView(linearLayout);
            isDismissing = false;
        }

        if (null != frameLayout && null != frameLayout.getParent() && mDecorViewRef != null &&
                mDecorViewRef.get() != null) {
            mDecorViewRef.get().removeView(frameLayout);
            isDismissing = false;
        }

        if (mHeaderToastHandler != null) {
            mHeaderToastHandler.removeCallbacksAndMessages(null);
        }

    }

    public void destroy() {
        dismiss();
        if (mHeaderToastHandler != null) {
            mHeaderToastHandler.removeCallbacksAndMessages(null);
        }
    }

    private static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}
