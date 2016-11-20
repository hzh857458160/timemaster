package com.dac.timemaster.dailyUsingTime;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dac.timemaster.R;
import com.dac.timemaster.data.ClockBean;
import com.dac.timemaster.data.DatabaseUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;

/**
 * Created by 凯齐 on 2016/11/15.
 */

public class weeklyChart extends AppCompatActivity {

    private Context context;
    private Toolbar Toolbar2;
    private LineChart chart;
    private XAxis xAxis;         //X坐标轴
    private YAxis yAxis;         //Y坐标轴


    protected void onCreate(Bundle savedInstanceState) {
        ContentValues values=new ContentValues();
        Cursor cursor= DatabaseUtil.query(weeklyChart.this,"clock",null,null,null,null,null,null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perweekchart_layout);
        Toolbar2.setTitle("一周使用情况");
        setSupportActionBar(Toolbar2);
        Toolbar2 = (Toolbar) findViewById(R.id.chart2_toolbar);
        Toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chart= (LineChart) findViewById(R.id.weekchart);

        //图表设置
        chart.setTouchEnabled(true);//启用与图表的所有可能的触摸交互
        chart.setDragEnabled(false); //禁用拖动（平移）图表
        chart.setScaleEnabled(true);//启用缩放图表上的两个轴
        chart.setDoubleTapToZoomEnabled(true);//捏缩放功能
        chart.setPinchZoom(true);//通过在其上双击缩放图表

       Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //设置警戒值
        LimitLine line = new LimitLine(95f, "警戒值 95%");
        line.setLineWidth(2f);
        line.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        line.setTextSize(15f);
        line.setTypeface(tf);

        //x轴设置
        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setDrawAxisLine(true);
        xAxis.setTypeface(tf); // 设置字体
        xAxis.setTextSize(10f); // 设置字体大小
        //y轴设置
        yAxis = chart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//      yAxis.setAxisMaxValue(220f);
        yAxis.addLimitLine(line);
        yAxis.setTypeface(tf); // 设置字体
        yAxis.setTextSize(10f); // s设置字体大小
        yAxis.setTypeface(tf);
        yAxis.setAxisMinValue(90f);
        yAxis.setStartAtZero(false);
        yAxis.setLabelCount(5); // 设置Y轴最多显示的数据个数

        ArrayList<String> xValues = new ArrayList<>();
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++) {
                cursor.move(i);
                xValues.add(cursor.getString(cursor.getColumnIndex(ClockBean.PACKAGENAME)));
            }
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();



    }

}
