package com.weiqi.modulebase.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 处理 多组件取同一个数据库
 * Created by alexwangweiqi on 17/9/5.
 */

public class DBContext extends ContextWrapper {

    private static final String TAG = "LiveDBContext";
    private static final String MODULE_DATABASE_PATH = "/data/data/com.weiqi.home/databases/";

    public DBContext(Context base) {
        super(base);
        Log.d(TAG, "packageName:" + base.getPackageName());
    }

    @Override
    public File getDatabasePath(String name) {
        File dbFile = new File(MODULE_DATABASE_PATH, name);
        Log.d(TAG, "getDatabasePath file path:" + dbFile.getAbsolutePath() + " name:" + name);
        File parentFile = dbFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dbFile;

    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name, mode, factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
}