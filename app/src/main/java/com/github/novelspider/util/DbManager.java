package com.github.novelspider.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.github.novelspider.model.database.DbContract;
import com.github.novelspider.model.database.DbHelper;
import com.github.novelspider.model.novel.Novel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by EternalPhane on 2016/9/20.
 */
public class DbManager {
    private static DbManager ourInstance = new DbManager();
    private DbHelper mDbHelper = null;
    private AtomicInteger mCounter = new AtomicInteger();
    private SQLiteDatabase mDb;

    private DbManager() {
    }

    public static DbManager getInstance() {
        return ourInstance;
    }

    public synchronized void init(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DbHelper(context);
        }
    }

    public synchronized SQLiteDatabase openDb() {
        if (mCounter.getAndIncrement() == 1) {
            mDb = mDbHelper.getWritableDatabase();
        }
        return mDb;
    }

    public synchronized void closeDb() {
        if (mCounter.getAndDecrement() == 0) {
            mDb.close();
        }
    }

    public synchronized boolean insert(final Novel novel) {
        return operate(new DbCallBack() {
            @Override
            public void operateDb(SQLiteDatabase db) {
                ContentValues values = new ContentValues();
                values.put(DbContract.Novels.COLUMN_NAME_TITLE, novel.getTitle());
                values.put(DbContract.Novels.COLUMN_NAME_SITE_ID, novel.getSiteId());
                values.put(DbContract.Novels.COLUMN_NAME_CONTENT_URL, novel.getContentUrl());
                values.put(DbContract.Novels.COLUMN_NAME_ORDER, 0);
                db.execSQL(String.format(
                        "UPDATE %s SET %s = %s + 1",
                        DbContract.Novels.TABLE_NAME,
                        DbContract.Novels.COLUMN_NAME_ORDER,
                        DbContract.Novels.COLUMN_NAME_ORDER
                ));
                db.insert(DbContract.Novels.TABLE_NAME, null, values);
            }
        });
    }

    public synchronized boolean operate(DbCallBack callback) {
        try {
            SQLiteDatabase db = openDb();
            callback.operateDb(db);
            return true;
        } catch (SQLException e) {
            // TODO: Add Exception Handler.
            return false;
        } finally {
            closeDb();
        }
    }

    public interface DbCallBack {
        public void operateDb(SQLiteDatabase db);
    }
}
