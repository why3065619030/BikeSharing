package com.example.bikesharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikesharing.bean.WeatherBean;
import com.example.bikesharing.bean.bike;
import com.example.bikesharing.notuse.index;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class borrowBike extends AppCompatActivity {
    private Handler myHandler;
    private Thread MyThread;
    private TextView textView,statu,temp,speed,longitude,latitude,username;
    private Button start,borrow_start,borrow_close,huanche;
    private static boolean ok= false;
    private static String str = "null";
    public List<bike> data;
    private static int mutex = 1;
    private static String id,user;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borro_bike);
        id = getIntent().getStringExtra("name");
        //首先将用户名确定
        user = getIntent().getStringExtra("username");
        username = findViewById(R.id.b_userId);
        username.setText(id);
        init();
    }

    public void init(){
        data = new ArrayList<>();
        textView  = findViewById(R.id.borrow_dianliang);
        statu = findViewById(R.id.borrow_statu);
        temp = findViewById(R.id.borrow_wendu);
        speed = findViewById(R.id.speed);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        start = findViewById(R.id.startBorrow);
        borrow_start = findViewById(R.id.borrow_start);
        borrow_close = findViewById(R.id.borrow_close);
        huanche = findViewById(R.id.huanche);

        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                Bundle bundle = msg.getData();      //


//                    int pid = android.os.Process.myPid();
//                    String command = "kill -9 "+ pid;
//
//                    try {
//                        Runtime.getRuntime().exec(command);
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }


                    statu.setText(bundle.getString("1"));
                    textView.setText(bundle.getString("2")+"%");        //电量，低于80会有预警
                    Float temprory = Float.parseFloat(bundle.getString("2"));
                    if(temprory <= 15 && mutex == 1){
                        mutex--;
                        vibrator = (Vibrator) borrowBike.this.getSystemService(borrowBike.VIBRATOR_SERVICE);
                        long[] patter = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000,1000, 1000};
                        vibrator.vibrate(patter, 0);
                        new AlertDialog.Builder(borrowBike.this)
                                .setTitle("注意")
                                .setMessage("电量不足，请更换车辆！")
                                .setPositiveButton("确定",  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        vibrator.cancel();

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();
                    }
                    if(temprory > 15 && mutex == 0){
                        mutex = 1;          //可能换电池了，此时必须保证下一次还能震动
                    }
                    temp.setText(bundle.getString("3")+"℃");
                    speed.setText(bundle.getString("4"));
                    longitude.setText(bundle.getString("5"));
                    latitude.setText(bundle.getString("6"));
                }


        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok = true;
                if(MyThread == null){
                    MyThread = new myThread();
                    MyThread.start();
                }
                if(!MyThread.isAlive()){
                    MyThread.start();
                }
            }
        });
        borrow_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getstartOrclose("start");
            }
        });
        borrow_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getstartOrclose("close");
            }
        });
        huanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getstartOrclose("huanche");
            }
        });
    }

    class myThread extends Thread{
        public void run(){
            super.run();
            while(ok){
                try {
                    myThread.sleep(2000);       //每次隔两秒向后台请求数据
                    getInfo("backPower");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getInfo(String itt) {
        String userId = id;
        //get 请求
        Log.e("调用了",userId);
        String login_check_url = constant.IP+itt+"?userId="+userId;

        Log.e("url",login_check_url);
        okhttp3.Callback callback = new okhttp3.Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                String responseData = "Error!";
                Log.e("刷新","Failure");
                Log.e("LoginActivity",responseData);
                str = "error";
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(itt.equals("backPower")){
                    String responseData = response.body().string();
                    Log.e("LoginActivity","success");
                    Log.e("LoginActivity",responseData);
                    Gson gson = new Gson();
                    Type t = new TypeToken<List<bike>>(){}.getType();
                    data = gson.fromJson(responseData, t);
                    Log.e("当前温度",data.get(0).getBikeId());
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();

                    bike b = data.get(0);

                    String st = b.getStatu();
                    if(st.equals("0")){
                        Log.e("click","close");
                        bundle.putString("1","关");
                    }
                    else{
                        Log.e("click","open");
                        bundle.putString("1","开");
                    }
                    bundle.putString("2",b.getVoltage());
                    bundle.putString("3",b.getTemperature());
                    bundle.putString("4",b.getSpeed());

                    WeatherBean b1 = new WeatherBean(Float.parseFloat(StringUtils.substringBefore(b.getSpeed(),"r")), Integer.toString(index.TimeStart)+"s");
                    index.TimeStart += 2;
                    index.data.add(b1);
                    Log.e("历史记录的长度",Integer.toString(index.data.size()));

                    bundle.putString("5",b.getLongitude());
                    bundle.putString("6",b.getLatitude());

                    //用来更新板子发来的数据
                    msg.setData(bundle);        //吧bundle添加到msg之中
                    myHandler.sendMessage(msg);
                }
                if(itt.equals("start"))     {

                }
                if(itt.equals("close")){

                }
//                Log.e("res",data.get(0).getRFID());           测试可以打印出来
            }
        };
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(login_check_url)
                .build();
        //发送请求
        client.newCall(request).enqueue(callback);
    }

    private void getstartOrclose(String itt) {
        String userId = id;
        //get 请求
        Log.e("调用了",userId);
        String login_check_url = constant.IP+itt+"?userId="+userId;
        Log.e("url",login_check_url);
        okhttp3.Callback callback = new okhttp3.Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                String responseData = "Error!";
                Log.e("LoginActivity","Failure");
                Log.e("LoginActivity",responseData);
                str = "error";
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("get",responseData);

                if(itt.equals("start")&&responseData.equals("hasStart"))     {
//                    Toast.makeText(borrowBike.this, "已成功打开", Toast.LENGTH_SHORT).show();
                }
                if(itt.equals("close")&&responseData.equals("hasClose")){
//                    Toast.makeText(borrowBike.this, "已成功关闭", Toast.LENGTH_SHORT).show();
                }
//                Log.e("res",data.get(0).getRFID());           测试可以打印出来
            }
        };
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(login_check_url)
                .build();
        //发送请求
        client.newCall(request).enqueue(callback);
    }
}