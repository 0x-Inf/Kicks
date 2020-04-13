package com.diablo.jayson.kicksv1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private Context mContext;

    public SharedPreferencesUtil(Context context) {
        this.mContext = context;
    }

    public void setIsFirstTimeLaunch(Boolean b) {
        SharedPreferences pref = mContext.getSharedPreferences("com.color.kicks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().putBoolean("Firsttime", b).apply();
    }

    public boolean isFirstTimeLaunch() {
        SharedPreferences prefs = mContext.getSharedPreferences("com.color.kicks", Context.MODE_PRIVATE);
        return prefs.getBoolean("Firsttime", true);
    }
}
