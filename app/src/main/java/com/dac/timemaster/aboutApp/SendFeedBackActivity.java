package com.dac.timemaster.aboutApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dac.timemaster.R;

public class SendFeedBackActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText edit_feedBackText;
    private TextView tv_cellphone;
    private EditText edit_cellphone;
    private RelativeLayout mPublish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_tl);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        edit_feedBackText = (EditText) findViewById(R.id.edit_feedBackText);
        tv_cellphone= (TextView) findViewById(R.id.tv_cellphone);
        edit_cellphone= (EditText) findViewById(R.id.edit_cellphone);
        mPublish= (RelativeLayout) findViewById(R.id.publish);

        edit_feedBackText.setHint("  有什么好的建议或者是意见可以告诉我们");

        mToolbar.setTitle("用户反馈");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPublish.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                try{
                    String textFeedBack = edit_feedBackText.getText().toString();
                    String textCellphone = edit_cellphone.getText().toString();

                    EmailSend email = EmailSend.getInstance();
                    email.sendMail("用户反馈  "+textCellphone,textFeedBack);
                    Toast.makeText(SendFeedBackActivity.this,
                            "反馈已发送到邮箱，谢谢您对我们的关心与支持",Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    Log.e("sendMail",e.toString());
                    e.printStackTrace();
                }
                finish();
            }
        });

    }


}
