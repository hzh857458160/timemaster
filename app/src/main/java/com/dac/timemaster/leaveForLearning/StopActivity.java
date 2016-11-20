package com.dac.timemaster.leaveForLearning;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dac.timemaster.R;


public class StopActivity extends Activity{
    private TextView tv_appname;
    private TextView tv_tips;
    private TextView tv_last;
    private TextView tv_time;

    //private ImageView  image_chouju;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        tv_appname= (TextView) findViewById(R.id.tv_appname);
        Intent intent = getIntent();
        String appName = intent.getStringExtra("AppName");
        String LeftTime = intent.getStringExtra("LeftTime");
        tv_appname.setText(appName);
        tv_tips= (TextView) findViewById(R.id.tv_tips);
        tv_last= (TextView) findViewById(R.id.tv_last);
        tv_time= (TextView) findViewById(R.id.tv_time);


        tv_time.setText(LeftTime);
        tv_tips.setText("失去了手机，你还有人生");
        try{
            Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/happytype.ttf");
            tv_appname.setTypeface(tf);
            tv_last.setTypeface(tf);
            //tv_time.setTypeface(tf);
            tv_tips.setTypeface(tf);
        }catch (Exception e){
            Log.e("typeface",e.toString());
            e.printStackTrace();
        }

        //image_chouju = (ImageView) findViewById(R.id.image_chouju);

    }
    // 屏蔽用户按后退键,使其直接返回桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent();
            intent.setAction  ( "android.intent.action.MAIN" );
            intent.addCategory( "android.intent.category.HOME" );
            intent.addCategory( "android.intent.category.DEFAULT" );
            intent.addCategory( "android.intent.category.MONKEY" );
            startActivity(intent);
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 当失去焦点时关闭该界面
     */
    protected void onStop() {
        super.onStop();
        finish();
    }


}
