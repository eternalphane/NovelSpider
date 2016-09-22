package com.github.novelspider.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by EternalPhane on 2016/9/20.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "novel_spider.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_SITE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)",
            DbContract.Sites.TABLE_NAME,
            DbContract.Sites._ID,
            DbContract.Sites.COLUMN_NAME_SITE
    );
    private static final String SQL_CREATE_NOVEL = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s NTEXT, %s INTEGER, %s TEXT, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))",
            DbContract.Novels.TABLE_NAME,
            DbContract.Novels._ID,
            DbContract.Novels.COLUMN_NAME_TITLE,
            DbContract.Novels.COLUMN_NAME_SITE_ID,
            DbContract.Novels.COLUMN_NAME_CONTENT_URL,
            DbContract.Novels.COLUMN_NAME_ORDER,
            DbContract.Novels.COLUMN_NAME_SITE_ID,
            DbContract.Sites.TABLE_NAME,
            DbContract.Sites._ID
    );
    private static final String SQL_DELETE_SITE = "DROP TABLE IF EXISTS " + DbContract.Sites.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_SITE);
        sqLiteDatabase.execSQL(SQL_CREATE_NOVEL);
        for (ContentValues values : DbContract.Sites.DEFAULT_VALUES) {
            sqLiteDatabase.insert(DbContract.Sites.TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i) {
            default:
        }
    }
}
