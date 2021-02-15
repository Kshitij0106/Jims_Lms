package com.medic.jimslms.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public Session(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLoggedIn(boolean result) {
        editor.putBoolean("loggedInMode", result);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean("loggedInMode", false);
    }


}
