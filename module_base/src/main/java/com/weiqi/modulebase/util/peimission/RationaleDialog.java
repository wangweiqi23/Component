package com.weiqi.modulebase.util.peimission;

/**
 * 对话框提示用户需要系统权限
 * Created by fengshzh on 07/04/2017.
 */
final class RationaleDialog {
    static void show(final PermissionAssistFragment fragment,
                     final int requestCode,
                     final String[] perms) {
//        new MaterialDialog.Builder(fragment.getContext())
//                .title(R.string.permission_request)
//                .content(R.string.permission_rational_camera_and_audio)
//                .positiveText(R.string.ok)
//                .negativeText(R.string.cancel)
//                .negativeColorRes(R.color.home_gray_8)
//                .cancelable(false)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
//                            which) {
//                        fragment.requestPermissions(perms, requestCode);
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
