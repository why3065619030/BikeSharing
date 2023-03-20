package com.example.bikesharing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class xunjian extends AppCompatActivity {
    private Handler myHandler;
    private Thread MyThread;
    private TextView textView,batteryId,temp,bikename;
    private Button start,xunjian_start,xunjian_close;
    private static boolean ok= false;
    private static String str = null;
    private static int mu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunjian);
        init();
    }

    public void init(){
        textView  = findViewById(R.id.dianliang);
        batteryId = findViewById(R.id.batteryId);
        temp = findViewById(R.id.wendu);
        start = findViewById(R.id.jiance);
        xunjian_start = findViewById(R.id.xunjian_start);
        xunjian_close = findViewById(R.id.xunjian_close);
        bikename = findViewById(R.id.bikeId);
        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                Bundle bundle = msg.getData();      //
                String all = bundle.getString("all");
                String[] infs = all.split("-");
                batteryId.setText(infs[0]);
                textView.setText(infs[1]);   //setText需要使用String
                temp.setText(infs[2]+"℃");
                bikename.setText(infs[3]);
            }
        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok = true;
                Log.e("start","click");
                if(MyThread == null){
                    MyThread = new myThread();
                    MyThread.start();
                }
                if(!MyThread.isAlive()){
                    MyThread.start();
                }
            }
        });
        xunjian_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("点击之前的mu值是  ",Integer.toString(mu));
                mu = 2;
            }
        });
        xunjian_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("点击之前的mu值是  ",Integer.toString(mu));
                mu = 1;
            }
        });
    }

    class myThread extends Thread{
        BufferedReader br = null;
        PrintWriter pw = null;
        private static final String SOCKET_IP = "192.168.4.1";
        private static final int SOCKET_PORT = 8086;
        public void run(){
            Socket socket = new Socket();
            super.run();
            while(ok){
                try {
                    socket.connect(new InetSocketAddress(SOCKET_IP, SOCKET_PORT), 3000);
                    InputStream in = socket.getInputStream();//从客户端生成网络输入流，用于接收来自网络的数据
                    OutputStream out = socket.getOutputStream();
//                    out.write("我是服务器，欢迎光临".getBytes());//服务器端数据发送（以字节数组形式）
                    byte[] bt = new byte[1024];//定义一个字节数组，用来存储网络数据
                    int len;//将网络数据写入字节数组
                    while((len = in.read(bt)) != 0) {
                        if(mu == 2){
                            out.write("start".getBytes());//服务器端数据发送（以字节数组形式）
                            Log.e("now发送了一次后，mu的值是：",Integer.toString(mu));
                            mu = 0;
                        }
                        if(mu == 1){
                            out.write("close".getBytes());//服务器端数据发送（以字节数组形式）
                            Log.e("now发送了一次后，mu的值是：",Integer.toString(mu));
                            mu = 0;
                        }
                        String data = new String(bt, 0 , len);//将网络数据转换为字符串数据
                        Log.e("data",data);
//                        Thread.sleep(1000);
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("all",data);
                        Log.e("str",data);             //用来更新板子发来的数据
                        msg.setData(bundle);        //吧bundle添加到msg之中
                        myHandler.sendMessage(msg);
                    }
                    br.close();
                    pw.close();
                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.e("","连接失败");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}