package com.example.bikesharing.notuse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.example.bikesharing.R;
import com.example.bikesharing.bean.WeatherBean;
import com.example.bikesharing.borrowBike;
import com.example.bikesharing.history;
import com.example.bikesharing.xunjian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class index extends AppCompatActivity {
    private Button bt1;
    private Button bt2;
    private Button bt3;
    public static List<WeatherBean> data = new ArrayList<>();
    public static int TimeStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        String id = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");
        initView();


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, borrowBike.class);
                intent.putExtra("name",id);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, xunjian.class);
                intent.putExtra("name",id);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, history.class);
//                intent.putExtra("data", (Serializable) index.instance.data );
                startActivity(intent);
            }
        });
    }

    public void initView(){
        bt1 = findViewById(R.id.borrow);
        bt2 = findViewById(R.id.find);
        bt3 = findViewById(R.id.button3);
    }
}