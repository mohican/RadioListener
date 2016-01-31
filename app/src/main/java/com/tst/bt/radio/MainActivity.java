package com.tst.bt.radio;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.util.List;


public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";

    private MainActivity mClass;
    private IntentFilter mFilter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClass = this;

        if (!isServiceRunning(ServiceRadioListener.class.getName())) {
            Intent intentMyIntentService = new Intent(mClass, ServiceRadioListener.class);
            startService(intentMyIntentService);
        }

        mFilter = new IntentFilter();
        mFilter.addAction(ServiceRadioListener.ACTION_UPDATE_UI);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(updateUIReciver, mFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(updateUIReciver);
    }

    private BroadcastReceiver updateUIReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


        }
    };


    public boolean isServiceRunning(String serviceClassName) {

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }

        return false;
    }
}
