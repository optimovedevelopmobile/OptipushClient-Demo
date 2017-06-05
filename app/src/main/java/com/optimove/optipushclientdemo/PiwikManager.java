package com.optimove.optipushclientdemo;

import android.content.*;
import android.support.annotation.Nullable;

import org.piwik.sdk.*;
import org.piwik.sdk.extra.TrackHelper;

public class PiwikManager {

    private static final String TRACKER_NAME = "optipush_main_tracker";
    private static final String USER_ID_PREF_FILE_NAME = "user_id_pref";
    private static final String USER_ID_KEY = "user_id";

    private static PiwikManager instance;

    private final Tracker mainTracker;
    private final SharedPreferences userIdPreferences;

    private PiwikManager(Context context) {

        mainTracker = Piwik.getInstance(context).newTracker(new TrackerConfig("http://104.197.238.220", 1, TRACKER_NAME));
        userIdPreferences = context.getSharedPreferences(USER_ID_PREF_FILE_NAME, Context.MODE_PRIVATE);
        String userId = userIdPreferences.getString(USER_ID_KEY, null);
        mainTracker.setUserId(userId);
    }

    public static PiwikManager getInstance() {

        if (instance == null)
            throw new IllegalStateException("Must call PiwikManager.configure() first");
        return instance;
    }

    public static void configure(Context context) {

        instance = new PiwikManager(context.getApplicationContext());
    }

    public void screen(String path, String title) {

        TrackHelper.track().screen(path).title(title).with(mainTracker);
        sendNow();
    }

    public void event(String category, String action) {

        TrackHelper.track().event(category, action).with(mainTracker);
        sendNow();
    }

    public void updateUserId(@Nullable String userId) {

        mainTracker.setUserId(userId);
        userIdPreferences.edit().putString(USER_ID_KEY, userId).apply();
    }

    public void sendNow() {

        mainTracker.dispatch();
    }
}