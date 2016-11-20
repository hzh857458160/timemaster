package com.dac.timemaster.dailyUsingTime;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.dac.timemaster.R;

/**
 * Created by GeniusHe on 2016/11/15.
 */

public class ScreenOnService extends Service {

    private boolean flag_dialog = false;
    private static final String TAG = "Receiver";
    private boolean isRegister = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(){
            public void run(){
                Log.d("ScreenOnService","run");
            }

        }.start();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() +  anHour/60;  //临界点设置为15min后，发送广播请求
        Intent i = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //unregisterReceiver(receiver);
    }


}
