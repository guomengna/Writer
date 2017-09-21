package com.example.guome.writer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.guome.writer.MainActivity;
import com.example.guome.writer.R;

/**
 * Created by guome on 2017/9/19.
 */

public class PersonInformationActivity extends Activity{
    Button fanhui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_informtion_layout);

        fanhui=(Button) findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PersonInformationActivity.this,"返回键被按了",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
