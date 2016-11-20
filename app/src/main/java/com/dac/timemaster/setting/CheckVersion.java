package com.dac.timemaster.setting;

/**
 * Created by GeniusHe on 2016/11/8.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

/**
 * Created by liujianfeng on 2016/8/9.
 */
public class CheckVersion {
    final static public String APP_NAME = "TimeMaster";
    //文件下载地址
    final static public String DOWNLOAD_ADDRESS = "https//127.0.0.1/server";

    public static void checkVersion(final Context context, final int flag) {
        OkHttpUtils.post()
                .url(DOWNLOAD_ADDRESS)
                .addParams("action", "check_version")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jObject = new JSONObject(response);
                            int newVersion = jObject.getInt("versionCode");
                            int appVersion = context.
                                    getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                            if(newVersion > appVersion){
                                final AlertDialog.Builder aDialog = new AlertDialog.Builder(context);
                                aDialog.setTitle("版本更新");
                                aDialog.setMessage("检测到有新版本，是否进行更新？");
                                aDialog.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final ProgressDialog pDialog = new ProgressDialog(context);
                                        pDialog.setMax(100);
                                        OkHttpUtils.get()
                                                .url(DOWNLOAD_ADDRESS)
                                                .build()
                                                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), APP_NAME) {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {
                                                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void inProgress(float progress, long total, int id) {
                                                        pDialog.setTitle("正在下载");
                                                        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                        pDialog.setProgress((int) (100 * progress));
                                                        pDialog.show();
                                                    }

                                                    @Override
                                                    public void onResponse(File response, int id) {
                                                        Logger.d(response.getAbsolutePath());
                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setDataAndType(Uri.fromFile(new File(Environment
                                                                        .getExternalStorageDirectory(), APP_NAME)),
                                                                "application/vnd.android.package-archive");
                                                        pDialog.dismiss();
                                                        context.startActivity(intent);
                                                    }
                                                });
                                    }
                                });
                                aDialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                aDialog.show();
                            }else{
                                if(flag == 0){
                                    Toast.makeText(context, "当前版本已为最新版本", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}