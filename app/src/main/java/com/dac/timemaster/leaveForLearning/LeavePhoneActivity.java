package com.dac.timemaster.leaveForLearning;



import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dac.timemaster.R;
import com.dac.timemaster.aboutApp.AboutActivity;
import com.dac.timemaster.dailyUsingTime.PieChartDailyActivity;
import com.dac.timemaster.dailyUsingTime.ScreenOnService;
import com.dac.timemaster.dailyUsingTime.WatchCatService;
import com.dac.timemaster.data.DatabaseHelper;
import com.dac.timemaster.setting.SettingActivity;


public class LeavePhoneActivity extends AppCompatActivity implements OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {


    private static final String USAGE_STATS_PERMISSION = "android.permission.PACKAGE_USAGE_STATS";
    private static final String SELF_PACKAGE_NAME ="com.dac.timemaster";
    private Button buttonStart;
    private MusicProgressBar musicProgressBar;
    private int time = 0;
    private Intent watchDogIntent;
    private long mCurrentTime;
    private Intent watchCatService;
    private Intent screenOnService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper dbHelper=new DatabaseHelper(LeavePhoneActivity.this);
        SQLiteDatabase dbWriter=dbHelper.getWritableDatabase();
        buttonStart = (Button) findViewById(R.id.btn_start);
        musicProgressBar= (MusicProgressBar) findViewById(R.id.musicProgressBar);
        musicProgressBar.setMax(6 * 60);

        /**
         * 启动WatchCatService、ScreenOnService服务
         * */

        watchCatService = new Intent(this, WatchCatService.class);
        screenOnService = new Intent(this, ScreenOnService.class);
        startService(watchCatService);
        startService(screenOnService);

        /**
         * 圆圈拖动条的拖动监听
         */
        musicProgressBar.setChangeListener(new MusicProgressBar.OnProgressChangeListener() {

            @Override
            public void onProgressChangeEnd(int duration, int progress) {
                String text = "您当前所选的时间为:"+progress/60+"小时"+progress % 60+"分钟";
                Toast.makeText(LeavePhoneActivity.this, text, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProgressChange(int duration, int progress) {
                time = progress;

            }
        });







            buttonStart.setOnClickListener(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * 返回键监听，
     * 如果侧边栏打开，就关闭侧边栏
     * 如果侧边栏是关闭的，就调用原返回键方法
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return false;
            } else {
                if ((System.currentTimeMillis() - mCurrentTime) > 2000) {
                    Toast.makeText(LeavePhoneActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mCurrentTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;//如果是后退键，则截获动作

            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 加载R.menu.main中的菜单
     * @param item
     * @return
     */
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /**
     * 将上个方法中的菜单添加点击事件
     * @param item
     * @return
     */

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /**
     * 侧边栏的选项及点击事件
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.leaveForLearning) {

           /* selectIntent=new Intent(, LeavePhoneActivity.class);
            startActivity(selectIntent);*/
        } else if (id == R.id.dailyUsingTime) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                if(getIsSetUsage()){
                    startActivity(new Intent(LeavePhoneActivity.this,PieChartDailyActivity.class));
                }else{
                    Toast.makeText(this, "请您先到设置中完成特殊设置", Toast.LENGTH_SHORT).show();
                }
            }else{
                startActivity(new Intent(LeavePhoneActivity.this,PieChartDailyActivity.class));
            }


        } else if (id == R.id.setting) {
            Intent selectIntent = new Intent(LeavePhoneActivity.this, SettingActivity.class);
            startActivity(selectIntent);
        } else if (id == R.id.appInfo) {
            startActivity(new Intent(LeavePhoneActivity.this,AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * StartService 的点击事件
     * @param v
     */

    @Override
    public void onClick(View v) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            if (getIsSetUsage() ) {
                if (0 != musicProgressBar.getProgress()){
                    startService();
                }else{
                    Toast.makeText(this, "您还没有选择时间", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "请您先到设置中完成特殊设置", Toast.LENGTH_SHORT).show();
            }
        }else{
            if (0 != musicProgressBar.getProgress()){
                startService();
            }else{
                Toast.makeText(this, "您还没有选择时间", Toast.LENGTH_SHORT).show();
            }
        }

    }


    /**
     * 把开启服务包装为一个方法
     */
    public void startService(){
        if (!WatchDogService.WatchDogIsOpen){
            watchDogIntent = new Intent(this, WatchDogService.class);
            watchDogIntent.putExtra("Time",time);
            startService(watchDogIntent);
            Toast.makeText(getApplicationContext(), "开启成功！", Toast.LENGTH_SHORT).show();
            JumpToHome();
        }else {
            Toast.makeText(getApplicationContext(), "你已经开启了应用锁功能，请结束后再试........", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 判断5.0及以上系统是否获得PACKAGE_USAGE_STATS权限
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean getIsSetUsage() {
            AppOpsManager appOps = (AppOpsManager) getApplicationContext()
                    .getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                    android.os.Process.myUid(), SELF_PACKAGE_NAME);
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;

            return granted;

    }
    //开启服务后直接跳转到桌面
    public void JumpToHome(){
        Intent intent = new Intent();
        intent.setAction  ( "android.intent.action.MAIN" );
        intent.addCategory( "android.intent.category.HOME" );
        intent.addCategory( "android.intent.category.DEFAULT" );
        intent.addCategory( "android.intent.category.MONKEY" );
        startActivity(intent);
    }





}
