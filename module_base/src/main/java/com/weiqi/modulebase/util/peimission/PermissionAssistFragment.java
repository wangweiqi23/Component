package com.weiqi.modulebase.util.peimission;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.weiqi.modulebase.widget.SafeDialogFragment;

import java.util.List;


/**
 * 授权辅助Fragment（无UI）
 * 授权流程：
 * 1. 首次授权shouldShowRationale为false，直接requestPermission。
 * 授权拒绝后当次失败(onRequestPermissionsResult)，下次授权shouldShowRationale为true。
 * 2. shouldShowRationale为true，弹RationaleDialog，点取消当次失败，下次重复2；否则系统弹出授权对话框(带"不再询问"的拒绝)。
 * 直接拒绝当次失败(onRequestPermissionsResult)，下次重复2；勾选"不再询问"并拒绝跳到3。
 * 3. onPermissionsDenied发现有永久拒绝权限，弹AppSettingsDialog。
 * 点取消当次失败，下次重复3；否则跳转系统设置授权，onActivityResult收到从系统设置页面返回后hasPermissions判断是否授权成功。如果失败当次失败，下次重复3。
 */
@SuppressLint("LongLogTag")
public class PermissionAssistFragment extends SafeDialogFragment implements
        EasyPermissions.PermissionCallbacks {
    private static final String TAG = "PermissionAssistFragment";
    private static final int REQUEST_CODE_LIVE = 100;

    private static String[] sPermissionGroup;

    private static PermissionCallback sExternalPermissionCallback;

    public static void checkPermissions(Object activityOrFragment,
                                        String[] permissionGroup,
                                        PermissionCallback callback) {
        if (sExternalPermissionCallback != null) {
            Log.w(TAG, "Can reqeust only one set of permissions at a time");
            callback.onPermissionsDenied();
            return;
        }
        sPermissionGroup = permissionGroup;
        sExternalPermissionCallback = callback;
        if (activityOrFragment instanceof FragmentActivity) {
            new PermissionAssistFragment().show(((FragmentActivity) activityOrFragment)
                    .getSupportFragmentManager(), TAG);
        } else if (activityOrFragment instanceof Fragment) {
            new PermissionAssistFragment().show(((Fragment) activityOrFragment)
                    .getChildFragmentManager(), TAG);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowsDialog(false);
        if (sPermissionGroup != null || sExternalPermissionCallback == null) {
            checkPermissions();
        } else {
            dismiss();
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_LIVE)
    public void checkPermissions() {
        Log.d(TAG, "checkStartPermission: ");
        if (EasyPermissions.hasPermissions(getContext(), sPermissionGroup)) {
            if (sExternalPermissionCallback != null) {
                sExternalPermissionCallback.onPermissionsGranted();
            }
            dismiss();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    REQUEST_CODE_LIVE,
                    sPermissionGroup);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted: " + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied: " + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) { // 引导用户去系统界面授权
            AppSettingsDialog.show(this);
        } else {
            onPermissionsDeniedWithoutMoreCheck();
        }
    }

    @Override
    public void onPermissionsDeniedWithoutMoreCheck() {
        if (sExternalPermissionCallback != null) {
            sExternalPermissionCallback.onPermissionsDenied();
        }
        dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + ", " + resultCode);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(getContext(), sPermissionGroup)) {
                if (sExternalPermissionCallback != null) {
                    sExternalPermissionCallback.onPermissionsGranted();
                }
                dismiss();
            } else {
                onPermissionsDeniedWithoutMoreCheck();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
        sExternalPermissionCallback = null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        sExternalPermissionCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sExternalPermissionCallback = null;
    }

}
