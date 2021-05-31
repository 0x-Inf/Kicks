package com.diablo.jayson.kicksv1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.diablo.jayson.kicksv1.Constants;

public class SharedPreferencesUtil {
    private Context mContext;

    public SharedPreferencesUtil(Context context) {
        this.mContext = context;
    }

    public void setIsFirstTimeLaunch(Boolean b) {
        SharedPreferences pref = getSharedPreferences(mContext);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().putBoolean("Firsttime", b).apply();
    }

    public boolean isFirstTimeLaunch() {
        SharedPreferences prefs = getSharedPreferences(mContext);
        return prefs.getBoolean("Firsttime", true);
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.SharedPreferencesName, Context.MODE_PRIVATE);
    }

    public String getLocationBroadcastId() {
        SharedPreferences preferences = getSharedPreferences(mContext);
        return preferences.getString(Constants.PREFERENCES_LOCATION_BROADCAST_ID, "");
    }

    public void setLocationBroadcastId(String locationBroadcastId) {
        SharedPreferences sharedPreferences = getSharedPreferences(mContext);
        sharedPreferences.edit().putString(Constants.PREFERENCES_LOCATION_BROADCAST_ID, locationBroadcastId).apply();
    }

    public String getPublicLocationBroadcastId() {
        SharedPreferences preferences = getSharedPreferences(mContext);
        return preferences.getString(Constants.PREFERENCES_PUBLIC_LOCATION_BROADCAST_ID, "");
    }

    public void setPublicLocationBroadcastId(String publicLocationBroadcastId) {
        SharedPreferences sharedPreferences = getSharedPreferences(mContext);
        sharedPreferences.edit().putString(Constants.PREFERENCES_PUBLIC_LOCATION_BROADCAST_ID, publicLocationBroadcastId).apply();
    }
}
