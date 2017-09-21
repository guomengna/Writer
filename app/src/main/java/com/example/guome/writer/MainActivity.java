package com.example.guome.writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.guome.writer.Activity.PersonInformationActivity;

import static com.example.guome.writer.R.styleable.View;

public class MainActivity extends Activity {

    ImageButton touxiang;
    Button zhuanlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        touxiang=(ImageButton) findViewById(R.id.touxiang);
        zhuanlan=(Button)findViewById(R.id.zhuanlan);
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, PersonInformationActivity.class);
                startActivity(intent);
            }
        });
        zhuanlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"点击了专栏按钮",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
