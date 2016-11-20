package com.dac.timemaster.aboutApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dac.timemaster.R;

/**
 * Created by GeniusHe on 2016/11/9.
 */

public class DeveloperActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView tv_developinfo;
    private TextView tv_developer;
    private TextView tv_number;
    private TextView tv_email;
    private ImageView login_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        tv_developinfo= (TextView) findViewById(R.id.tv_developinfo);
        tv_developer= (TextView) findViewById(R.id.tv_developer);
        tv_number= (TextView) findViewById(R.id.tv_number);
        tv_email= (TextView) findViewById(R.id.tv_email);
        login_logo= (ImageView) findViewById(R.id.login_logo);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);


        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
