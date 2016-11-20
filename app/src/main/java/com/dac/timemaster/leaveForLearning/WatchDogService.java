package com.dac.timemaster.leaveForLearning;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.dac.timemaster.data.BlackListBean;
import com.dac.timemaster.data.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created  on 2016/9/19.
 * design by Han
 */
public class WatchDogService extends Service {


    private static final String TAG = "WatchDogService";
    public static boolean WatchDogIsOpen = false;
    //
    private ActivityManager am;
    // 该List暂时替代数据库存储锁定名单
    private List<String> lockedPackNames= new ArrayList<>();
    private Intent StopActivityIntent;
    // 设置一个标志
    private boolean flag ;
    //这是白名单的应用名称
    private String tempStopProtectPackname;

    private List<PackageInfo> packages;

    private CountDownTimer myTimer;

    private int time;

    public void onCreate(){
        super.onCreate();
    }

    public void onDestroy(){
        Toast.makeText(getApplicationContext(), "Destroy", Toast.LENGTH_SHORT).show();
        WatchDogIsOpen = false;
        super.onDestroy();

    }

    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //ContentValues valuesdg = new ContentValues();
       // Cursor cursordg = DatabaseUtil.query(WatchDogService.this, "blacklist", null, null, null, null, null, null);
        WatchDogIsOpen = true;

        packages = getPackageManager().getInstalledPackages(0);
        //获取所有安装的应用，并获得他们的包名
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String packName = packageInfo.packageName;
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            //判断是否为系统应用，如果不是才加入黑名单
            if (!filterApp(appInfo) && null != packName) {//系统应用为1，非系统应用为0
                lockedPackNames.add(packName);
                Log.v("WatchDogActivity", "packageInfo.packageName == " + packName);
                //while (cursordg.moveToNext()) {
                    //for (int j = 0; j < cursordg.getCount(); j++) {
                        //cursordg.move(j);
                        //String name = cursordg.getString(cursordg.getColumnIndex(BlackListBean.PACKAGENAME));
                        //if (name.equals(packName)) {
                           // continue;
                       // } else {
                          //  valuesdg.put(BlackListBean.PACKAGENAME, packName);
                          //  DatabaseUtil.insert(WatchDogService.this, "blacklist", null, valuesdg);
                       // }
                   // }
                //} else {
                    //aluesdg.put(BlackListBean.PACKAGENAME, packName);
                   // DatabaseUtil.insert(WatchDogService.this, "blacklist", null, valuesdg);
                //}

            }
        }

       /*for (int k = 0; k < lockedPackNames.size(); k++) {
            String dname = lockedPackNames.get(k);
            for (int j = 0; j < cursordg.getCount(); j++) {
                cursordg.move(j);
                String name2 = cursordg.getString(cursordg.getColumnIndex(BlackListBean.PACKAGENAME));
                if (dname.equals(name2)) {
                    continue;
                } else {
                    DatabaseUtil.delete(WatchDogService.this, "blacklist", "packagename=?", new String[]{name2});
                }
            }

        }*/


        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        time = intent.getIntExtra("Time", 0);
        if (0 == time) {
            Log.v(TAG, "time can not <= 0");
        } else {
            myTimer = new CountDownTimer(time * 60000, 60000) {

                @Override
                public void onTick(long millisUntilFinished) {
                   //Toast.makeText(getApplicationContext(), "time = " + time, Toast.LENGTH_SHORT).show();
                    time--;

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub
                    flag = false;
                    stopSelf();
                }
            }.start();

            new Thread() {
                public void run() {
                    flag = true;
                    while (flag) {

                        //获取栈顶Activity
                        String packname = getTaskPackname();
                        String LeftTime = (time/60 < 10 ? "0" : "")
                                + time/60 + ":" + (time%60 < 10 ? "0" : "") + time%60;
                        String AppName = getAppNameFromPackageName(packname);

                        if (lockedPackNames.contains(packname)) {
                            if (packname.equals("com.dac.timemaster")) {
                                //当这个包名为白名单里的应用，则没有事情发生
                            } else {
                                // 当不是白名单，跳转至stopActivity
                                // 该intent为从该服务跳到暂停界面
                                StopActivityIntent = new Intent(WatchDogService.this, StopActivity.class);
                                StopActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                StopActivityIntent.putExtra("AppName", AppName);
                                StopActivityIntent.putExtra("LeftTime",LeftTime);
                                startActivity(StopActivityIntent);
                            }
                        }
                        //
                        SystemClock.sleep(300);
                    }

                }
            }.start();


        }

        return START_REDELIVER_INTENT;
    }


    /**
     * get TopActivity
     * @return
     */
    //@SuppressLint("NewApi")
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
    /**
     * 从包名中得到应用名
     */
    public String getAppNameFromPackageName(String packName){

        PackageManager pm=getApplicationContext().getPackageManager();

        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            if(packageInfo.packageName.equals(packName)){
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                return (pm.getApplicationLabel(appInfo)).toString() ;
            }

        }
        return packName;



    }




}