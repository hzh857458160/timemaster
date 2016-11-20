package com.dac.timemaster.aboutApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dac.timemaster.R;

/**
 * Created by 凯齐 on 2016/11/7.
 */

public class AboutActivity extends AppCompatActivity{
    private ListView aListView;
    private Toolbar aToolbar;
    String[] aTitle = {"关于开发者","用户反馈"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        aListView= (ListView) findViewById(R.id.about_list);
        aToolbar= (Toolbar) findViewById(R.id.about_toolbar);

        aToolbar.setTitle("关于");
        setSupportActionBar(aToolbar);
        aToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        aListView.setAdapter(new SettingAdapter(this, aTitle));
        aListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(AboutActivity.this,DeveloperActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(AboutActivity.this,SendFeedBackActivity.class));
                        break;

                }
            }
        });
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
            return aTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return aTitle[position];
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
