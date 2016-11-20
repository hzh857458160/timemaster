package com.dac.timemaster.setting;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dac.timemaster.R;

import java.util.List;

/**
 * Created by GeniusHe on 2016/11/6.
 */

public class UsageSetActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView tv_title;
    private TextView tv_usageset;
    private TextView tv_step1;
    private TextView tv_step2;
    private ImageView image_usageset;
    private ImageView image_step1;
    private ImageView image_step2;
    private Button btn_Guide;
    private Context context;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usage_setting);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_usageset= (TextView) findViewById(R.id.tv_usageset);
        tv_step1= (TextView) findViewById(R.id.tv_step1);
        tv_step2= (TextView) findViewById(R.id.tv_step2);
        image_usageset= (ImageView) findViewById(R.id.image_usageset);
        image_step1= (ImageView) findViewById(R.id.image_step1);
        image_step2= (ImageView) findViewById(R.id.image_step2);
        btn_Guide= (Button) findViewById(R.id.btn_guide);



        /**
         * 按钮指向“有权查看使用权限的应用”这个选项
         * 如果有则该按钮指向设置区
         */
        if (isNoOption()) {
            btn_Guide.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                }
            });

        }else{
            Toast.makeText(this,"对不起，您的安卓版本无需设置该选项",Toast.LENGTH_SHORT).show();
            btn_Guide.setVisibility(View.INVISIBLE);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 判断当前设备中有没有“有权查看使用权限的应用”这个选项
     *
     * @return void
     */
    private boolean isNoOption() {
        PackageManager packageManager = getApplicationContext()
                .getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @SuppressLint("NewApi")
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        return !(queryUsageStats == null || queryUsageStats.isEmpty());
    }






}
