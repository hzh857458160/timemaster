package com.dac.timemaster.dailyUsingTime;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dac.timemaster.leaveForLearning.WatchDogService;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by GeniusHe on 2016/11/15.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(!WatchDogService.WatchDogIsOpen){
            Log.i("dialog","open dialog");
            Intent i =new Intent(context,DialogActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }



        //重启服务
        //context.stopService(new Intent(context,ScreenOnService.class));
        context.startService(new Intent(context,ScreenOnService.class));
    }




}
