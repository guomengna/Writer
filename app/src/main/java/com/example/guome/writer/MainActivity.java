package com.example.guome.writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.guome.writer.Activity.PersonInformationActivity;
import com.example.guome.writer.Image.CircleImageView;
import static com.example.guome.writer.R.styleable.View;

public class MainActivity extends Activity {
    CircleImageView touxiang;
    Button zhuanlan;
    ImageView bianxie,tongbu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        touxiang= findViewById(R.id.touxiang);
        zhuanlan=findViewById(R.id.zhuanlan);
        bianxie=findViewById(R.id.bianxie);
        tongbu=findViewById(R.id.touxiang);
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
        bianxie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"点击了编写按钮",Toast.LENGTH_SHORT).show();
            }
        });
        tongbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"点击了同步按钮",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
