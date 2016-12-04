package com.dac.timemaster.dailyUsingTime;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.dac.timemaster.data.CountTime;
import com.dac.timemaster.data.CountTimeDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by GeniusHe on 2016/11/13.
 */

public class WatchCatService extends Service{
    private final static int SlEEP = 1000; //线程休眠时间
    private String currentPkgName = null;  //当前运行的包名
    private String pkgName = null;
    private final String TAG = "WatchCatService";
    private Calendar cd;
    private int dayOfWeek;
    private long nowTime;
    private long lastTime;
    private int CountTime = 0;
    private CountTime countTime;
    private CountTimeDB db;
    private BroadcastReceiver receiver;
    private StringBuffer sb = new StringBuffer();
    private List<String> lockedPackNames= new ArrayList<>();
    private List<PackageInfo> packages;
    boolean flag = false;   //记录pkgName是否为系统应用，否为true

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                //开机启动服务

                if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
                    context.startService(new Intent(context,WatchCatService.class));
                    context.startService(new Intent(context,ScreenOnService.class));
                }

                //亮屏启动ScreenOnService服务并唤醒WatchCatService，息屏关闭ScreenOnService服务
                if(Intent.ACTION_SCREEN_ON.equals(action)){
                    Log.i(TAG,"亮屏");
                    context.startService(new Intent(context,ScreenOnService.class));
                    context.startService(new Intent(context,WatchCatService.class));
                }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                    Log.i(TAG,"息屏");
                    context.stopService(new Intent(context,ScreenOnService.class));
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        getApplicationContext().registerReceiver(receiver,filter);
        Log.i(TAG,"注册广播");


        db = CountTimeDB.getInstance(WatchCatService.this);
        countTime = new CountTime();

        packages = getPackageManager().getInstalledPackages(0);
        //获取所有安装的应用，并获得他们的包名
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String packName = packageInfo.packageName;
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            //判断是否为系统应用，如果不是才加入黑名单
            if (!filterApp(appInfo) && null != packName) {//系统应用为1，非系统应用为0
                lockedPackNames.add(packName);
                Log.v(TAG, "packageInfo.packageName == " + packName);

            }
        }

        cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        Log.d(TAG,"今天是周"+(dayOfWeek));
        if (dayOfWeek == 1){
            //从数据库取出过去一周的数据放进数据库用于生成图表

        }

        Log.d(TAG,setDateFomemat());
        lastTime = System.currentTimeMillis();
        //开始线程，统计应用使用时间
        new Thread(){
            public void run(){
                while (true){
                    try {
                        currentPkgName = pkgName;
                        sleep(SlEEP);

                        pkgName = getTaskPackname();
                        //判断当前运行应用是否改变
                        if(!pkgName.equals(currentPkgName) && currentPkgName != null && pkgName != null){

                            for(int i=lockedPackNames.size()-1;i>=0;i--){
                                if(currentPkgName.equals(lockedPackNames.get(i))){
                                    flag = true;
                                    Log.d(TAG,currentPkgName+" = "+lockedPackNames.get(i) + "  flag = true");
                                    break;
                                }
                            }

                            if(flag){
                                nowTime = System.currentTimeMillis();
                                CountTime = getSecondTime(lastTime,nowTime);
                                //*****************************************************************************
                                countTime.setPkgname(currentPkgName);
                                countTime.setTotaltime(CountTime);
                                countTime.setNowdate(setDateFomemat());
                                db.saveCountTime(countTime);

                                sb.append(currentPkgName+" "+setTimeFormat(CountTime)+" "+countTime.getNowdate()+"\n");
                                Log.d(TAG,sb.toString());


                            }

                            lastTime = System.currentTimeMillis();
                            flag = false;
                        }


                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

            }
        }.start();


        return super.onStartCommand(intent, flags, startId);
    }


    public String setTimeFormat(int time){
        int hour = (time/60)/60;
        int min = (time/60)%60;
        int sec = time - hour *3600 - min *60 ;
        String str_format= (hour < 10 ? "0" : "") + hour + ":" + (min < 10 ? "0" : "") + min +":" + (sec < 10 ? "0" : "") + sec;
        return str_format;
    }

    //服务死掉时需要先把数据保存进数据库
    @Override
    public void onDestroy() {
        super.onDestroy();

        //在这里将timeCount的值存入包名为currentPkgName的应用的数据库，appTime为使用应用的秒数
        //同时，将nowDate的值一起存入当前日期


        //*****************************************************************************

    }

    /**
     * get TopActivity
     * @return
     */

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private String getTaskPackname() {
        String currentApp = "CurrentNULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
            Toast.makeText(getApplicationContext(),"else",Toast.LENGTH_SHORT).show();
        }
        if ("com.dac.timemaster".equals(currentApp)) {

        }else{
            //Log.e("TAG", "Current App in foreground is: " + currentApp);
        }
        return currentApp;

    }

    /**
     * 判断某一个应用程序是不是系统的应用程序，
     * 如果是返回true，否则返回false。
     */

    //得到当前日期，格式 yyyy-mm-dd
    public String setDateFomemat() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
        String nowDate = dateFormat.format(now);
        return nowDate;
    }

    public int getSecondTime(long last,long now){
        int t = (int)(now-last)/1000;
        return t;
    }

    public boolean filterApp(ApplicationInfo info) {
        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的
        //它还是系统应用，这个就是判断这种情况的
        /*if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//判断是不是系统应用
            return true;
        } else {
            return false;
        }*/
        //系统程序
        return (info.flags & ApplicationInfo.FLAG_SYSTEM) > 0;

    }
}
