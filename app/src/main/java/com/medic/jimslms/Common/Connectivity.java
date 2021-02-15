package com.medic.jimslms.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {
    public static boolean isConnectedToInternet(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }

                }
            }
        }
        return false;
    }
}
