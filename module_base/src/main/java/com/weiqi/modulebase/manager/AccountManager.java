package com.weiqi.modulebase.manager;

import com.weiqi.modulebase.db.AccountDbOperation;
import com.weiqi.modulebase.model.account.Account;
import com.weiqi.slog.SLog;

/**
 * Created by alexwangweiqi on 17/9/25.
 */

public class AccountManager {

    private static final String TAG = AccountManager.class.getSimpleName();

    private static Account mAccount;

    public static synchronized void init() {
        mAccount = AccountDbOperation.query();
        if (mAccount == null) {
            SLog.e(TAG ," init account error");
        }
    }

    public static boolean isLogin() {
        return mAccount.isLogin();
    }

    public static synchronized void save(Account account) {
        if (!AccountDbOperation.saveAccount(account)) {
            SLog.e(TAG ," save account error");
        }
    }
}
