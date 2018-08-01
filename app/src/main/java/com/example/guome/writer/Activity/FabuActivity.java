package com.example.guome.writer.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.guome.writer.R;

/**
 * Created by guome on 2018/8/1.
 */

public class FabuActivity extends Activity implements View.OnClickListener{
    private com.github.clans.fab.FloatingActionButton fabuButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floatbutton_layout);
        fabuButton=findViewById(R.id.fabu_button);
        fabuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fabu_button:
                Toast.makeText(FabuActivity.this,"点击了发布按钮",Toast.LENGTH_SHORT).show();
        }
    }
}
