package com.weiqi.modulebase.util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.weiqi.modulebase.PreferenceKeyConstants;
import com.weiqi.modulebase.R;
import com.weiqi.modulebase.navigation.SettingNavigator;
import com.weiqi.modulebase.util.peimission.PermissionCheckUtil;
import com.weiqi.modulebase.util.peimission.PermissionUtils;
import com.weiqi.modulebase.widget.ToastUtil;
import com.weiqi.slog.SLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiao.js on 2017/9/12.
 */

public class LauncherPermissionManager implements ILifeCycleListener {

    private static final String TAG = LauncherPermissionManager.class.getSimpleName();

    public static final String[] PERMISSION_NECESSARY = new String[]{"android.permission.READ_PHONE_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"};
    private static final int REQUEST_FIRST_PERMISSION = 0;
    public static final int PERMISSION_REQUEST_CODE = 11;

    private Activity mActivity;
    private boolean mIsFirst = true;
    private CheckPermissionCallback mPermissionCallback;
    private boolean mIsDialogShow = false;

    public LauncherPermissionManager(Activity activity, CheckPermissionCallback checkPermissionCallback) {
        mActivity = activity;
        mPermissionCallback = checkPermissionCallback;
    }

    public void isFirstOpen() {
        String isFirst = SharedPreferenceUtil.get(PreferenceKeyConstants.RELEASE_TIP, null);

        if (TextUtils.isEmpty(isFirst)) {
            mIsFirst = true;
        } else {
            mIsFirst = false;
        }
    }

    public void checkPermissionForContinue() {
        if (PermissionUtils.hasSelfPermissions(mActivity, PERMISSION_NECESSARY)) {
            mPermissionCallback.onContinue();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(mActivity, PERMISSION_NECESSARY)
                    || mIsFirst) {
                SLog.i(TAG, "@Jiao first show Rational");
                showRational();
            } else if (PermissionCheckUtil.hasGrantedPermission(mActivity, PERMISSION_NECESSARY)) {
                showDeniedForFirstOpen();
            } else {
                SLog.i(TAG, "@Jiao first request permission");
                ActivityCompat.requestPermissions(mActivity, PERMISSION_NECESSARY,
                        REQUEST_FIRST_PERMISSION);
            }
        }
    }

    public void checkActivityResultFromPermissionSetPage(int requestCode) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            checkPermissionForContinue();
        }
    }

    public void checkReqPermissionResult(int reqCode, String[] permissions, int[] grantResults) {
        SharedPreferenceUtil.save(PreferenceKeyConstants.RELEASE_TIP, PreferenceKeyConstants.PERMISSION_FIRST);
        mIsFirst = false;
        if (grantResults.length != PERMISSION_NECESSARY.length) {
            if (!PermissionUtils.hasSelfPermissions(mActivity, PERMISSION_NECESSARY)) {
                showDeniedForFirstOpen();
            } else {
                mPermissionCallback.onContinue();
            }
        } else {
            if (reqCode == REQUEST_FIRST_PERMISSION) {
                if (PermissionUtils.getTargetSdkVersion(mActivity) < 23 && !PermissionUtils
                        .hasSelfPermissions(mActivity
                                , PERMISSION_NECESSARY)) {
                    showDeniedForFirstOpen();
                    return;
                }
                if (PermissionUtils.hasSelfPermissions(mActivity, PERMISSION_NECESSARY)) {
                    mPermissionCallback.onContinue();
                } else {
                    showDeniedForFirstOpen();
                }
            }
        }
    }

    /**
     * 显示应用启动需申请的所有权限的展示说明对话框,点击下一步连续进行每个权限的授权询问
     */
    private void showRational() {
        final MaterialDialog.Builder bu = new MaterialDialog.Builder(mActivity);
        bu.canceledOnTouchOutside(false);
        bu.cancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SLog.e(TAG,"cancel permission request first");
            }
        });
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View dialog = inflater.inflate(R.layout.partial_permission_first, null);
        TextView phoneText = (TextView) dialog.findViewById(R.id.home_phone);
        TextView locationText = (TextView) dialog.findViewById(R.id.home_location);
        TextView storageText = (TextView) dialog.findViewById(R.id.home_storage);
        Button nextStepBtn = (Button) dialog.findViewById(R.id.home_next_step_btn);
        final List<String> firstPermissionList = new ArrayList<>();

        if (!PermissionUtils.hasSelfPermissions(mActivity, Manifest.permission.READ_PHONE_STATE)) {
            phoneText.setVisibility(View.VISIBLE);
            firstPermissionList.add("android.permission.READ_PHONE_STATE");
        } else {
            phoneText.setVisibility(View.GONE);
        }
        if (!PermissionUtils.hasSelfPermissions(mActivity, Manifest.permission
                .ACCESS_COARSE_LOCATION)
                || !PermissionUtils.hasSelfPermissions(mActivity, Manifest.permission
                .ACCESS_FINE_LOCATION)) {
            locationText.setVisibility(View.VISIBLE);
            firstPermissionList.add("android.permission.ACCESS_COARSE_LOCATION");
            firstPermissionList.add("android.permission.ACCESS_FINE_LOCATION");
        } else {
            locationText.setVisibility(View.GONE);
        }
        if (!PermissionUtils.hasSelfPermissions(mActivity, Manifest.permission
                .WRITE_EXTERNAL_STORAGE)) {
            storageText.setVisibility(View.VISIBLE);
            firstPermissionList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            storageText.setVisibility(View.GONE);
        }
        if (firstPermissionList.size() == 2 &&
                firstPermissionList.contains("android.permission.ACCESS_COARSE_LOCATION") &&
                PermissionUtils.shouldShowRequestPermissionRationale(mActivity, "android.permission" +
                        ".ACCESS_COARSE_LOCATION")) {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(mActivity, "android.permission" +
                    ".READ_PHONE_STATE")) {
                //在其他的权限都被设为不再显示后, 则不管位置信息权限,直接显示其他请求的被拒绝状态
                showDeniedForFirstOpen();
                return;
            } else {
                //如果只有位置权限没被申请 可以不需要该权限进行启动
                mPermissionCallback.onContinue();
                return;
            }
        }

        final String[] livePermissionArray = firstPermissionList.toArray(new
                String[0]);
        final MaterialDialog materialDialog = bu.customView(dialog, true)
                .show();
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                try {
                    ActivityCompat.requestPermissions(mActivity, livePermissionArray,
                            REQUEST_FIRST_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show(ContextUtil.getContext().
                            getString(R.string.permission_open_failure_tip));
                }
            }
        });
    }

    void showDeniedForFirstOpen() {
        if (PermissionUtils.shouldShowRequestPermissionRationale(mActivity, "android.permission" +
                        ".READ_PHONE_STATE",
                "android.permission.WRITE_EXTERNAL_STORAGE")) {
            showRational();
        } else if (!PermissionUtils.hasSelfPermissions(mActivity, Manifest.permission
                .WRITE_EXTERNAL_STORAGE)) {
            showPermissionDialog(R.string.permission_storage_content, true);
        }
    }

    private void showPermissionDialog(@StringRes int stringId, final boolean isNeedToExit) {
        if (!mIsDialogShow) {
            mIsDialogShow = true;
            MaterialDialog.Builder bu = new MaterialDialog.Builder(mActivity);
            bu.title(R.string.permission_request)
                    .content(stringId)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                                which) {
                            dialog.dismiss();
                            startPermissionSettingPage();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                                which) {
                            dialog.dismiss();
                            if (isNeedToExit) {
                                SLog.e(TAG,"cancel permission request");
                            } else {
                                mPermissionCallback.onContinue();
                            }
                        }
                    })
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mIsDialogShow = false;
                        }
                    })
                    .cancelable(false)
                    .positiveText(R.string.go_set_permission)
                    .negativeColorRes(R.color.home_gray_8)
                    .negativeText(R.string.cancel)
                    .show();
        }
    }

    private void startPermissionSettingPage() {
        SettingNavigator.skipWithResult(mActivity, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onBaseResume() {

    }

    @Override
    public void onBasePause() {

    }

    @Override
    public void onBaseDestroy() {
        mActivity = null;
    }

    public interface CheckPermissionCallback {
        void onContinue();
    }
}
