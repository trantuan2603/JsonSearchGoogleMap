package com.landsoft.jsonsearchgooglemap.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static com.landsoft.jsonsearchgooglemap.Constants.Contant.ANY;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.LIST_PREFERENCE;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.WIFI;

/**
 * Created by TRANTUAN on 02-Dec-17.
 */

public class SettingReceiver extends BroadcastReceiver {
    String mPreference;
    boolean refreshDislay;
    public SettingReceiver(String mPreference, boolean refreshDislay) {
        this.mPreference = mPreference;
        this.refreshDislay = refreshDislay;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (WIFI.equals(mPreference) && networkInfo!=null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI ){
                refreshDislay = true;
                //Log.d(WIFI, "onReceive: "+refreshDislay);
            }else if (ANY.equals(mPreference) && networkInfo!=null){
                refreshDislay = true;
                //Log.d(ANY, "onReceive: "+refreshDislay);
            } else {
                refreshDislay = false;
                //Log.d("sai", "onReceive: "+refreshDislay);
            }
    }
}
