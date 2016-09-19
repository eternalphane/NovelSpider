package com.github.novelspider;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class App extends Application {
    private List<Activity> mActivities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
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
