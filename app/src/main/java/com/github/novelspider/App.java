package com.github.novelspider;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.github.novelspider.util.DbManager;
import com.github.novelspider.util.SettingsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class App extends Application {
    private static Context sContext = null;
    private List<Activity> mActivities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        DbManager.getInstance().init(sContext);
        SettingsUtil.init(sContext);
    }

    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : mActivities) {
            activity.finish();
            mActivities.remove(activity);
        }
        System.exit(0);
    }
}
