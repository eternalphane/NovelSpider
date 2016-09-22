package com.github.novelspider.util.novel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.novelspider.model.novel.Novel;
import com.github.novelspider.model.database.DbContract;
import com.github.novelspider.util.DbManager;
import com.github.novelspider.util.StorageUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by EternalPhane on 2016/9/22.
 */
public class Shelf {
    private static Shelf ourInstance = new Shelf();
    private List<Novel> mNovels;
    private DbManager mDbMgr;

    public static Shelf getInstance() {
        return ourInstance;
    }

    private Shelf() {
        mNovels = new LinkedList<>();
        loadNovels();
        mDbMgr = DbManager.getInstance();
    }

    public synchronized List<Novel> getNovels() {
        return Collections.unmodifiableList(mNovels);
    }

    public synchronized boolean addNovel(Novel novel) {
        if (mDbMgr.insert(novel)) {
            StorageUtil.storeNovel(novel);
            mNovels.add(0, novel);
            return true;
        }
        return false;
    }

    public synchronized boolean updateNovel(final int index, final boolean regenerated) {
        if (mDbMgr.operate(new DbManager.DbCallBack() {
            @Override
            public void operateDb(SQLiteDatabase db) {
                db.execSQL(String.format(
                        "UPDATE %s SET %s = %s + 1 WHERE %s < %d",
                        DbContract.Novels.TABLE_NAME,
                        DbContract.Novels.COLUMN_NAME_ORDER,
                        DbContract.Novels.COLUMN_NAME_ORDER,
                        DbContract.Novels.COLUMN_NAME_ORDER,
                        index
                ));
                ContentValues values = new ContentValues();
                if (regenerated) {
                    values.put(DbContract.Novels.COLUMN_NAME_SITE_ID, mNovels.get(index).getSiteId());
                    values.put(DbContract.Novels.COLUMN_NAME_CONTENT_URL, mNovels.get(index).getContentUrl());
                }
                values.put(DbContract.Novels.COLUMN_NAME_ORDER, 0);
                db.update(
                        DbContract.Novels.TABLE_NAME,
                        values,
                        "? = ?",
                        new String[]{
                                DbContract.Novels.COLUMN_NAME_TITLE,
                                mNovels.get(index).getTitle()
                        }
                );
            }
        })) {
            StorageUtil.storeNovel(mNovels.get(index));
            mNovels.add(0, mNovels.remove(index));
            return true;
        }
        return false;
    }

    public synchronized boolean removeNovel(final int index) {
        if (mDbMgr.operate(new DbManager.DbCallBack() {
            @Override
            public void operateDb(SQLiteDatabase db) {
                db.execSQL(String.format(
                        "UPDATE %s SET %s = %s - 1 WHERE %s > %d",
                        DbContract.Novels.TABLE_NAME,
                        DbContract.Novels.COLUMN_NAME_ORDER,
                        DbContract.Novels.COLUMN_NAME_ORDER,
                        DbContract.Novels.COLUMN_NAME_ORDER,
                        index
                ));
                db.delete(
                        DbContract.Novels.TABLE_NAME,
                        DbContract.Novels.COLUMN_NAME_TITLE,
                        new String[]{
                                mNovels.get(index).getTitle()
                        }
                );
            }
        })) {
            StorageUtil.removeNovel(mNovels.get(index));
            mNovels.remove(index);
            return true;
        }
        return false;
    }

    private void loadNovels() {
        try (Cursor c = mDbMgr.openDb().query(
                DbContract.Novels.TABLE_NAME,
                new String[]{
                        DbContract.Novels.COLUMN_NAME_TITLE,
                        DbContract.Novels.COLUMN_NAME_SITE_ID,
                        DbContract.Novels.COLUMN_NAME_CONTENT_URL,
                        DbContract.Novels.COLUMN_NAME_ORDER
                },
                null,
                null,
                null,
                null,
                DbContract.Novels.COLUMN_NAME_ORDER
        )) {
            while (c.moveToNext()) {
                Novel novel = new Novel(
                        c.getString(c.getColumnIndexOrThrow(DbContract.Novels.COLUMN_NAME_TITLE)),
                        c.getInt(c.getColumnIndexOrThrow(DbContract.Novels.COLUMN_NAME_SITE_ID)),
                        c.getString(c.getColumnIndexOrThrow(DbContract.Novels.COLUMN_NAME_CONTENT_URL))
                );
                StorageUtil.restoreNovel(novel);
                mNovels.add(novel);
            }
        } catch (IllegalArgumentException e) {
            // TODO: Add Exception Handler.
        } finally {
            mDbMgr.closeDb();
        }
    }
}
