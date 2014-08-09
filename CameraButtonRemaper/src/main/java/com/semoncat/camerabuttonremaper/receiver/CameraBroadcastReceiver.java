package com.semoncat.camerabuttonremaper.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.semoncat.camerabuttonremaper.MainActivity;

/**
 * Created by SemonCat on 2014/1/18.
 */
public class CameraBroadcastReceiver extends BroadcastReceiver {

    private SharedPreferences mPreferences;

    private String mDefaultCameraApp = "com.android.camera2";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
// Prevent other apps from launching
        abortBroadcast();
// Your Program
        mPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        String mPackageName = mPreferences.
                getString(MainActivity.DefaultCameraButtonAppName, mDefaultCameraApp);

        startNewActivity(context,mPackageName);
    }

    public void startNewActivity(Context mContext,String packageName)
    {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null)
        {
            // we found the activity
            // now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        else
        {
            // bring user to the market
            // or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            mContext.startActivity(intent);
        }
    }
}
