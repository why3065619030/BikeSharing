package com.example.bikesharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.bikesharing.getLocalhost.getIPV4;
import com.example.bikesharing.notuse.index;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity {
    public static final int MSG_LOGIN_ERR = 1; //登录错误
    public static final int MSG_CONNET_ERR = 2; //网络链接错误

    private Context context;
    private EditText et_number;
    private EditText et_password;
    private Button bt_login;
    private RelativeLayout rl_register;
    private ImageView iv_weixin;
    private ImageView iv_qq;
    private ImageView iv_weibo;
    private LoginHandler login_handler;
    public static String name;
    public getIPV4 getIPV4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        InitView();
        login_handler = new LoginHandler();
        Init();
    }
    private void InitView()
    {
        getIPV4 = new getIPV4();
        et_number = (EditText)findViewById(R.id.et_number);
        et_password = (EditText)findViewById(R.id.et_password);
        bt_login = (Button)findViewById(R.id.bt_login);
        rl_register = (RelativeLayout)findViewById(R.id.rl_register);
        iv_weixin = (ImageView)findViewById(R.id.iv_weixin);
        iv_qq = (ImageView)findViewById(R.id.iv_qq);
        iv_weibo = (ImageView)findViewById(R.id.iv_weibo);
        name = "";
    }

    private void Init() {

        //设置提示的颜色
        et_number.setHintTextColor(getResources().getColor(R.color.white));
        et_password.setHintTextColor(getResources().getColor(R.color.white));
        //登录
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judge()) {
                    bt_login.setEnabled(false);//点了登录后不可以再点，避免用户乱点
                    loginInfo();
                }
            }
        });

        //注册
        rl_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "巡检人员入口", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WelcomeActivity.this, index.class));
            }
        });

        //微信
        iv_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "微信", Toast.LENGTH_SHORT).show();
            }
        });

        //qq
        iv_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "QQ", Toast.LENGTH_SHORT).show();
            }
        });

        //微博
        iv_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "微博", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**登录*/
    private void loginInfo() {
        String username = et_number.getText().toString();
        String userId = et_password.getText().toString();
        //get 请求
        String login_check_url = constant.IP+"login?username="+username+"&userId="+userId;
        Log.e("ooo",login_check_url);
        okhttp3.Callback callback = new okhttp3.Callback()
        {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("LoginActivity","onResponse");
                Log.e("LoginActivity",responseData);
                if(responseData.equals("success")) {
                    name = userId;
                    Log.e("当前id是"+userId,"当前名字是"+name);
                    Intent intent = new Intent(WelcomeActivity.this, index.class);
                    //将id一层一层往下传递
                    intent.putExtra("name",userId);
                    intent.putExtra("username",username);
                    startActivity(intent);
                    finish();
                }
                else {
                    Message msg = new Message();
                    msg.what = MSG_LOGIN_ERR;
                    login_handler.sendMessage(msg);
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                String responseData = "Error!";
                Log.e("LoginActivity","Failure");
                Log.e("LoginActivity",responseData);
                Message msg = new Message();
                msg.what = MSG_CONNET_ERR;
                login_handler.sendMessage(msg);
            }
        };
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(login_check_url)
                .build();
        //发送请求
        client.newCall(request).enqueue(callback);
    }

    //判断登录信息是否合法
    private boolean judge() {
        if (TextUtils.isEmpty(et_number.getText().toString()) ) {
            Toast.makeText(context, "用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            Toast.makeText(context, "用户ID不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     3 * 事件捕获
     4 */
    class LoginHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_LOGIN_ERR:
                    et_number.setText("");
                    et_password.setText("");
                    et_number.requestFocus();
                    new AlertDialog.Builder(WelcomeActivity.this)
                            .setTitle("注意")
                            .setMessage("用户名或密码输入不正确，请重新输入")
                            .setPositiveButton("确定",null)
                            .create()
                            .show();
                    bt_login.setEnabled(true);
                    break;
                case MSG_CONNET_ERR:
                    new AlertDialog.Builder(WelcomeActivity.this)
                            .setTitle("注意")
                            .setMessage("网络连接错误，请检查网络")
                            .setPositiveButton("确定",null)
                            .create()
                            .show();
                    break;
            }
        }
    }
}
