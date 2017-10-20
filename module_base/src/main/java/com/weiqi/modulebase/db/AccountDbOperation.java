package com.weiqi.modulebase.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weiqi.modulebase.model.account.Account;
import com.weiqi.modulebase.util.ContextUtil;

/**
 * Created by alexwangweiqi on 17/9/27.
 */

public class AccountDbOperation {


    public static boolean saveAccount(Account account) {
        boolean isSave = false;
        AccountDBHelper accountDBHelper = new AccountDBHelper(ContextUtil.getContext());
        SQLiteDatabase db = accountDBHelper.getWritableDatabase();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ")
                .append(AccountDBHelper.TABLE_NAME)
                .append(" (name, age, login) VALUES (")
                .append("'" + account.getName() + "',")
                .append("'" + account.getAge() + "',")
                .append("'" + (account.isLogin() ? Account.IS_LOGIN : Account.NOT_LOGIN) + "',");

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");

        try {
            db.execSQL(stringBuilder.toString());
            isSave = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSave;
    }

    public static Account query() {
        Account account = new Account();
        AccountDBHelper accountDBHelper = new AccountDBHelper(ContextUtil.getContext());
        SQLiteDatabase db = accountDBHelper.getWritableDatabase();

        Cursor cursor = db.query(AccountDBHelper.TABLE_NAME,
                new String[]{AccountDBHelper.COLUMN_NAME, AccountDBHelper.COLUMN_AGE,
                        AccountDBHelper.COLUMN_LOGIN},
                null, null, null, null, null);

        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            account.setName(cursor.getString(0));
            account.setAge(cursor.getInt(1));
            account.setLogin((cursor.getInt(2) == Account.IS_LOGIN));
        }

        return account;
    }
}
