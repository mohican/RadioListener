package com.tst.bt.radio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Roman.
 */
public class ServiceRadioListener extends Service {


    private static final String LOG_TAG = "ServiceRadioLogStreamer";

    public static final String NETWORK_STATUS_AT = "+CREG";
    public static final String CALL_STATUS_AT = "+ECPI";
    public static final String ACTION_UPDATE_UI = "ServiceRadioLogStreamer.ACTION_UPDATE_UI";

    private int mNetworkStatus = -1;

    private int mCallStatus = -1;



    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");

        startListen();

        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }


    private void startListen() {

        Process LCprocess = null;

        try {
            Process process = Runtime.getRuntime().exec("su");
            Runtime.getRuntime().exec("logcat -c").waitFor();
            LCprocess = Runtime.getRuntime().exec(new String[]{"logcat", "-b", "radio"});
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(LCprocess.getInputStream()), 8192);

        new LogListener(this, reader);
    }





    private class LogListener implements Runnable {

        private final Context mCtx;
        private final BufferedReader mReader;
        private final Thread thread;

        LogListener(Context ctx, BufferedReader reader) {
            mCtx = ctx;
            mReader = reader;

            thread = new Thread(this);
            thread.start();
        }

        public void run() {
            while (true) {
                String nextLine = null;

                try {
                    nextLine = mReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (nextLine != null && nextLine.contains("AT<")) {
                    Log.w(LOG_TAG, "logcat: " + nextLine);

                    parseCommand(nextLine);
                }
            }
        }

        private void parseCommand(String logLine) {

            logLine = logLine.replace(" ", "");
            String[] splitedCmd = logLine.split(":");

            if (splitedCmd.length > 2) {
                String[] splitedParams = splitedCmd[2].split(",");

                String cmdName = splitedCmd[1];
                cmdName = cmdName.replace("AT<", "");

                if (cmdName.equals(NETWORK_STATUS_AT)) {
                    String netStat = splitedParams.length == 5 ? splitedParams[1] : splitedParams[0];
                    mNetworkStatus = Integer.valueOf(netStat).intValue();
                }

                 if (cmdName.equals(CALL_STATUS_AT)) {

                    mCallStatus = Integer.valueOf(splitedParams[1]).intValue();
                }

                sendData();
            }
        }

        private void sendData() {

            Intent local = new Intent();
            local.setAction(ACTION_UPDATE_UI);
            local.putExtra(CALL_STATUS_AT, mCallStatus);
            local.putExtra(NETWORK_STATUS_AT, mNetworkStatus);
            mCtx.sendBroadcast(local);

        }
    }


}
