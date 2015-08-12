package com.guiyu.longcondemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 15-8-12.
 */
public class StaticHttpDemo extends Activity {

    private HttpURLConnection conn;
    private URL url;
    private InputStream is;
    private EditText et;
    private TextView tv;
    private String name;
    private Timer mTimer;
    private TimerTask mTimerTask;
//http://192.168.5.107:8080/send?id=1111&msg=er

    /*private static final AsyncHttpClient client = new AsyncHttpClient();
    private static final String BASE_URL = "http://192.168.5.107:8080/send/";
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
*/






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /*client.get("", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });*/
        String urlstr="http://192.168.5.107:8080/send?id=1111&msg=-_-";
        getDataFromNet(urlstr);
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
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.connet_btn:
////                if(iscon())
////                    //new Thread(runnable).start();
////                    getDataFromNet("http://192.168.5.107:8080/send?id=1111&msg="+et.getText().toString());
//                break;
//        }
//    }
}
