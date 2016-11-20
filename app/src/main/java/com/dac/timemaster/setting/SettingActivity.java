package com.dac.timemaster.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dac.timemaster.R;



/**
 * Created by GeniusHe on 2016/11/5.
 */

public class SettingActivity extends AppCompatActivity {
    private Context context;
    private ListView mListView;
    private Toolbar mToolbar;
    String[] mTitle = {"安卓5.0权限设置","检查更新"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mListView= (ListView) findViewById(R.id.setting_list);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);

        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListView.setAdapter(new SettingAdapter(this, mTitle));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(SettingActivity.this,UsageSetActivity.class));
                        break;
                    case 1:
                        CheckVersion();
                        break;
                }
            }
        });
    }

    private void CheckVersion(){
        try{
            Context context = getApplicationContext();
            ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isAvailable()){
                Toast.makeText(context,"已是最新版本，无需更新",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context,"网络不可用",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Log.v("tag",e.toString());
            e.printStackTrace();
        }


    }


    public class SettingAdapter extends BaseAdapter {

        private Context context;
        private String[] title;
        private LayoutInflater inflater;

        public SettingAdapter(Context context, String[] title){
            this.context = context;
            this.title = title;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitle[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.center_item, parent, false);
                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.left_text);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }

            holder.textView.setText(title[position]);
            holder.textView.setTextColor(Color.parseColor("#000000"));
            return convertView;
        }

        class Holder{
            TextView textView;
        }
    }
}
