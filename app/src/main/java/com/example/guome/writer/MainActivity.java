package com.example.guome.writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guome.writer.Activity.PersonInformationActivity;

import de.hdodenhof.circleimageview.CircleImageView;
public class MainActivity extends Activity implements Button.OnClickListener{
    CircleImageView touxiang;
    TextView zhuanlan;
    ImageView bianxie,tongbu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        touxiang= (CircleImageView) findViewById(R.id.touxiang);
        touxiang.setOnClickListener(this);
        zhuanlan=(TextView) findViewById(R.id.zhuanlan);
        zhuanlan.setOnClickListener(this);
        bianxie=(ImageView) findViewById(R.id.bianxie);
        bianxie.setOnClickListener(this);
        tongbu=(ImageView)findViewById(R.id.touxiang);
        tongbu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.touxiang:
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, PersonInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.zhuanlan:
                Toast.makeText(MainActivity.this,"点击了专栏按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bianxie:
                Toast.makeText(MainActivity.this,"点击了编写按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tongbu:
                Toast.makeText(MainActivity.this,"点击了同步按钮",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
