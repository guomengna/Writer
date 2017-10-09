package com.example.guome.writer.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.example.guome.writer.R;

/**
 * Created by guome on 2017/10/9.
 */

public class AddNewEasyActivity extends Activity{
    private EditText addneweasy;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addneweasy_layout);
        addneweasy=findViewById(R.id.addneweasy_edittext);

    }
}
