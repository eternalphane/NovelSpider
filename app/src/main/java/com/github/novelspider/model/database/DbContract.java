package com.github.novelspider.model.database;

import android.content.ContentValues;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by EternalPhane on 2016/9/20.
 */
public class DbContract {
    private DbContract() {
    }

    public static abstract class Sites implements BaseColumns {
        public static final String TABLE_NAME = "sites";
        public static final String COLUMN_NAME_SITE = "site";
        public static final List<ContentValues> DEFAULT_VALUES;
        private static List<ContentValues> sDefaultValues;

        static {
            sDefaultValues = new ArrayList<>();
            ContentValues v = new ContentValues();
            v.put(_ID, 0);
            v.put(COLUMN_NAME_SITE, "www.23wx.com");
            sDefaultValues.add(v);
            String sites[] = {
                    "www.biquge.io",
                    "www.qingkan520.com",
                    "www.bxwx8.org"
            };
            for (String s : sites) {
                v = new ContentValues();
                v.put(COLUMN_NAME_SITE, s);
                sDefaultValues.add(v);
            }
            DEFAULT_VALUES = Collections.unmodifiableList(sDefaultValues);
        }
    }

    public static abstract class Novels implements BaseColumns {
        public static final String TABLE_NAME = "novels";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SITE_ID = "site_id";
        public static final String COLUMN_NAME_CONTENT_URL = "content_url";
        public static final String COLUMN_NAME_ORDER = "order";
    }
}
