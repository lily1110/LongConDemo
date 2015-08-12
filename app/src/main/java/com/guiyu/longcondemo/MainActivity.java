package com.guiyu.longcondemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements View.OnClickListener{

    Button connet_btn;

    private HttpURLConnection conn;
    private URL url;
    private InputStream is;
    private EditText et;
    private TextView tv;
    private String name;
    private Timer mTimer;
    private TimerTask mTimerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());*/

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initWidget();
        /*mTimer = new Timer();
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
//                Log.d("AndroidTimerDemo", "timer");
                Calendar cal = Calendar.getInstance();
                tv.setText(cal.toString());
//                getDataFromNet("http://192.168.5.107:8080/send?id=1111&msg=_");
            }
        };

        mTimer.schedule(mTimerTask, 1000, 1000);*/
//        new Thread(runnable).start();

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this,StaticHttpDemo.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,intent,0);
        //设置任务执行计划
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, cal.getTimeInMillis(), 5*1000, pi);//从firstTime才开始执行，每隔5秒再执行

    }

    private void initWidget() {
        connet_btn = (Button)findViewById(R.id.connet_btn);
        connet_btn.setOnClickListener(this);
        et = (EditText)findViewById(R.id.et);
        tv = (TextView)findViewById(R.id.tv);
    }
    private boolean iscon() {
        boolean iscon = false;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info  = cm.getActiveNetworkInfo();
        if(info!=null) {
            showToast("连网正常  "+info.getTypeName());
            iscon = true;
        }
        else {
            showToast("未连网");
            iscon = false;
        }
        return iscon;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connet_btn:
                if(iscon())
                    //new Thread(runnable).start();
                   getDataFromNet("http://192.168.5.107:8080/send?id=1111&msg=_"+et.getText().toString());
                break;
         /*   case R.id.toStatic_btn:
                Intent intent = new Intent(this,StaticHttpDemo.class);
                startActivity(intent);
                break;*/
        }
    }

    private void showToast(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    private void getDataFromNet(String urlstr) {
        try {
                url =  new URL(urlstr);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String str = null;
                StringBuffer sb = new StringBuffer();
            while((str=br.readLine())!= null) {
                sb.append(str);
                }
            if(sb!=null&&sb.length()>0) {
                tv.setText(sb.toString());
                System.out.println(sb.toString());
//                getDataFromNet(urlstr);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果-->" + val);
        }
    };

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            getDataFromNet("http://192.168.5.107:8080/send?id=1111&msg="+et.getText().toString());
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value","请求结果");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
