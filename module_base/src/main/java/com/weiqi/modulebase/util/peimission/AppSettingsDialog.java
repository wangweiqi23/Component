package com.weiqi.modulebase.util.peimission;

/**
 * 对话框提示用户需跳转到系统设置手动授权
 * Created by fengshzh on 07/04/2017.
 */
final class AppSettingsDialog {
    static final int DEFAULT_SETTINGS_REQ_CODE = 16061;

    static void show(final PermissionAssistFragment fragment) {
//        new MaterialDialog.Builder(fragment.getContext())
//                .title(R.string.permission_request)
//                .content(R.string.permission_rational_app_setting_camera_and_audio)
//                .positiveText(R.string.ok)
//                .negativeText(R.string.cancel)
//                .negativeColorRes(R.color.home_gray_8)
//                .cancelable(false)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
//                            which) {
//                        SettingNavigator.startSystemSettingForResult(fragment, DEFAULT_SETTINGS_REQ_CODE);
//                    }
//                })
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
//                            which) {
//                        fragment.onPermissionsDeniedWithoutMoreCheck();
//                    }
//                })
//                .show();

    }

}
