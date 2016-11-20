package com.dac.timemaster.dailyUsingTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;


import com.dac.timemaster.R;
import com.dac.timemaster.leaveForLearning.WatchDogService;

/**
 * Created by GeniusHe on 2016/11/15.
 */

public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setCancelable(false);
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE://稍后休息
                        dialog.dismiss();
                        onBackPressed();
                        break;
                    case Dialog.BUTTON_NEGATIVE://休息
                        startService();
                        dialog.dismiss();
                        BackToHome();

                        break;


                }
            }
        };

        builder.setTitle("提示"); //设置标题
        builder.setMessage("您连续使用手机的时间已达到15分钟，请休息一下吧"); //设置内容
        builder.setIcon(R.drawable.ic_menu_share);//设置图标，图片id即可
        builder.setPositiveButton("稍后休息",dialogOnclicListener);
        builder.setNegativeButton("休息5分钟", dialogOnclicListener);
        builder.create().show();




    }

    public void startService(){
        Intent watchDogIntent = new Intent(this, WatchDogService.class);
        watchDogIntent.putExtra("Time",5);
        startService(watchDogIntent);
        Toast.makeText(getApplicationContext(), "5分钟倒计时开始，您可以眺望一下远方", Toast.LENGTH_SHORT).show();

    }


    private void ScreenOff(Context context){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
        wakeLock.acquire();
        //做我们的工作，在这个阶段，我们的屏幕会持续点亮
        // 释放锁，屏幕熄灭。
        wakeLock.release();

    }
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void BackToHome(){
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction  ( "android.intent.action.MAIN" );
        intent.addCategory( "android.intent.category.HOME" );
        intent.addCategory( "android.intent.category.DEFAULT" );
        intent.addCategory( "android.intent.category.MONKEY" );
        startActivity(intent);
    }
}
