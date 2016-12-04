package com.dac.timemaster.dailyUsingTime;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dac.timemaster.R;
import com.dac.timemaster.data.CountTime;
import com.dac.timemaster.data.CountTimeDB;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by GeniusHe on 2016/11/28.
 */

public class PieChartDailyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PieChart pieChart;
    private CountTimeDB db;
    private List<CountTime> countTimeList;
    private List<PackageInfo> packages;
    private Button btnRefresh;
    private String title;
    private Map<String ,Float> pieValues = new HashMap<>();

    public static final int[] PIE_COLORS = {
            Color.rgb(181, 194, 202), Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223), Color.rgb(195, 221, 155), Color.rgb(251, 215, 191),
            Color.rgb(237, 189, 189), Color.rgb(172, 217, 243)
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        toolbar = (Toolbar) findViewById(R.id.chart1_toolbar);
        btnRefresh = (Button) findViewById(R.id.btn_refresh);

        packages = getPackageManager().getInstalledPackages(0);
        toolbar.setTitle("一天使用情况");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pieChart = (PieChart) findViewById(R.id.pieChart);
        title = "今日时间";


        db = CountTimeDB.getInstance(PieChartDailyActivity.this);
        countTimeList = db.loadCountTime();
        Log.v("PieChartDailyActivity",setDateFomemat());
        if(countTimeList.size()>0){
            for (int i = 0; i < countTimeList.size(); ++i){
                CountTime countTime = countTimeList.get(i);
                if (countTime.getPkgname()!=null ){
                    //y轴数据
                    int time = countTime.getTotaltime();
                    Float temp = new Float(time);
                    Log.d("PieChartDailyActivity","yValues add :"+ time);
                    //x轴数据
                    String appname =getAppNameFromPackageName(countTime.getPkgname());
                    Log.d("PieChartDailyActivity","xValues add :"+ appname);
                    pieValues.put(appname,temp);

                }

            }
        }else{
            Toast.makeText(PieChartDailyActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
        }

        setPieChart(pieChart,pieValues,title,true);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimeList = db.loadCountTime();
                Log.v("PieChartDailyActivity",setDateFomemat());
                if(countTimeList.size()>0){
                    for (int i = 0; i < countTimeList.size(); ++i){
                        CountTime countTime = countTimeList.get(i);
                        if (countTime.getPkgname()!=null ){
                            //y轴数据
                            int time = countTime.getTotaltime();
                            Float temp = new Float(time);
                            Log.d("PieChartDailyActivity","yValues add :"+ time);
                            //x轴数据
                            String appname =getAppNameFromPackageName(countTime.getPkgname());
                            Log.d("PieChartDailyActivity","xValues add :"+ appname);
                            pieValues.put(appname,temp);

                        }

                    }
                }else{
                    Toast.makeText(PieChartDailyActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                }
                setPieChart(pieChart,pieValues,title,true);
            }
        });



    }

    /**
     * 设置饼图样式
     *
     * @param pieChart
     * @param pieValues
     * @param title
     * @param showLegend 是否显示图例
     */
    public static void setPieChart(PieChart pieChart, Map<String, Float> pieValues, String title, boolean showLegend) {
        pieChart.setUsePercentValues(true);//设置使用百分比
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setExtraOffsets(20, 15, 20, 15);
        pieChart.setCenterText(title);//设置环中的文字
        pieChart.setCenterTextSize(22f);//设置环中文字的大小
        pieChart.setDrawCenterText(true);//设置绘制环中文字
        pieChart.setRotationAngle(120f);//设置旋转角度

        //图例设置
        Legend legend = pieChart.getLegend();
        if (showLegend) {
            legend.setEnabled(true);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        } else {
            legend.setEnabled(false);
        }

        //设置饼图数据
        setPieChartData(pieChart, pieValues);

        pieChart.animateX(1500, Easing.EasingOption.EaseInOutQuad);//数据显示动画
    }

    /**
     * 设置饼图数据源
     */
    private static void setPieChartData(PieChart pieChart, Map<String, Float> pieValues) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entries.add(new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(5f);//设置饼块选中时偏离饼图中心的距离

        dataSet.setColors(PIE_COLORS);//设置饼块的颜色
        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.BLUE);//设置连接线的颜色
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
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
    //得到当前日期，格式 yyyy-mm-dd
    public String setDateFomemat() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
        String nowDate = dateFormat.format(now);
        return nowDate;
    }
}
