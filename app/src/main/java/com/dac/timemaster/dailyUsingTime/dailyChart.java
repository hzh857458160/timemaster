package com.dac.timemaster.dailyUsingTime;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dac.timemaster.R;
import com.dac.timemaster.data.ClockBean;
import com.dac.timemaster.data.DatabaseUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.components.Description;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by 凯齐 on 2016/11/15.
 */

public class dailyChart extends AppCompatActivity {

    private Context context;
    private Toolbar Toolbar1;
    private BarChart daychart;
    private BarData barData;
    private XAxis xAxis;         //X坐标轴
    private YAxis yAxis;         //Y坐标轴

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ContentValues values = new ContentValues();
        Cursor cursor = DatabaseUtil.query(dailyChart.this, "clock", null, null, null, null, null, null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.perdaychart_layout);
        Toolbar1 = (Toolbar) findViewById(R.id.chart1_toolbar);

        Toolbar1.setTitle("一天使用情况");
        setSupportActionBar(Toolbar1);
        Toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        daychart = (BarChart) findViewById(R.id.daychart);
        Description des = new Description();

        //图表设置
        daychart.setDrawBarShadow(false);
        daychart.setDrawValueAboveBar(true);
        daychart.setTouchEnabled(true);//启用与图表的所有可能的触摸交互
        daychart.setDragEnabled(false); //禁用拖动（平移）图表
        daychart.setScaleEnabled(true);//启用缩放图表上的两个轴
        daychart.setDoubleTapToZoomEnabled(true);//捏缩放功能
        daychart.setPinchZoom(true);//通过在其上双击缩放图表
        des.setText("");
        daychart.setDescription(des);
        //x轴设置
        xAxis = daychart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f); // 设置字体大小

        //x轴数据
        List<String> xValues = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);
                xValues.add(cursor.getString(cursor.getColumnIndex(ClockBean.PACKAGENAME)));

            }
        }


        //y轴设置
        daychart.getAxisRight().setDrawLabels(false);//右侧是否显示Y轴数值
        daychart.getAxisRight().setEnabled(false);//是否显示最右侧竖线
        yAxis = daychart.getAxisLeft();
        yAxis.setStartAtZero(false); //设置为 true，则无论图表显示的是哪种类型的数据，该轴最小值总是0


        //y轴数据
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);
                float value = (float) cursor.getInt(cursor.getColumnIndex(ClockBean.TIME));
                yValues.add(new BarEntry(value, i));
            }
        }

        BarDataSet barDataSet = new BarDataSet(yValues, "使用时间");
        barDataSet.setColor(Color.rgb(114, 188, 223));

        //ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        //barDataSets.add(barDataSet); // add the datasets
        //barData=new BarData(xValues,barDataSet);
        barData=new BarData(barDataSet);
        daychart.setData(barData);


        daychart.animateY(8000);
    }



}
