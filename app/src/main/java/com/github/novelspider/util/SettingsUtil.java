package com.github.novelspider.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EternalPhane on 2016/9/22.
 */

public class SettingsUtil {
    private static final String PREFS_NAME = "settings";
    private static SharedPreferences sSettings;

    private SettingsUtil() {
    }

    public static synchronized void init(Context context) {
        sSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (getValue("storage_location") == null) {
            setValue("storage_location", StorageUtil.getDefaultStorage(context).getAbsolutePath());
        }
    }

    public static synchronized String getValue(String key) {
        return sSettings.getString(key, null);
    }

    public static synchronized void setValue(String key, String value) {
        sSettings.edit().putString(key, value);
    }
}
