package com.moises.generic;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Global {
    public static final String AUDIO_DESTINATION_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS) + "/";
    private static Activity context;
    private static Audio audio;
    private static String version;
    private static String serialNumber;

    private Global() {
    }

    /*public static User getUser() {
        if (user == null) user = new User();
        return user;
    }

    public static void unsetUser() {
        user = null;
    }*/

    public static Activity getContext() {
        return context;
    }

    public static void setContext(Activity activity) {
        context = activity;
    }

    public static Audio getAudio() {
        if (audio == null) audio = new Audio();
        return audio;
    }

    public static String getVersion() {
        if (version == null) {
            version = "unknown";
            try {
                version = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return version;
    }

    public static String getSerialNumber() {
        if (serialNumber == null) {
            serialNumber = "unknown";
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getDeviceId() != null)
                serialNumber = telephonyManager.getDeviceId();
        }
        return serialNumber;
    }

    public static boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    
    public static void toast(String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
    
}
