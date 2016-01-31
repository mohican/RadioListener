package com.tst.bt.radio;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.tst.bt.radio.enums.ENetworkStatus;
import com.tst.bt.radio.enums.ECallStatus;

import java.util.List;


public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";

    private MainActivity mClass;
    private IntentFilter mFilter;

    private TextView txtNetworkStatus;

    private TextView txtCallStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClass = this;

        if (!isServiceRunning(ServiceRadioListener.class.getName())) {
            Intent intentMyIntentService = new Intent(mClass, ServiceRadioListener.class);
            startService(intentMyIntentService);
        }


        txtNetworkStatus = (TextView) findViewById(R.id.text_network_status);

        txtCallStatus = (TextView) findViewById(R.id.text_call_status);

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

            ENetworkStatus netStatus = ENetworkStatus.parse(intent.getIntExtra(ServiceRadioListener.NETWORK_STATUS_AT, -1));

            if (netStatus != null) {
                txtNetworkStatus.setText(netStatus.name());
            } else {
                txtNetworkStatus.setText("no_data");
            }

  			ECallStatus callStatus = ECallStatus.parse(intent.getIntExtra(ServiceRadioListener.CALL_STATUS_AT, -1));



            if (callStatus != null) {
                txtCallStatus.setText(callStatus.name());
            } else {
                txtCallStatus.setText("no_call");
            }
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
