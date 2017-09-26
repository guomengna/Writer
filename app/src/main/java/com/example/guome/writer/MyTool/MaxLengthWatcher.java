package com.example.guome.writer.MyTool;

/**
 * Created by guome on 2017/9/26.
 */

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by guomengna on 2016/6/9.
 *
 * 限定输入的最大位数
 */
public class MaxLengthWatcher implements TextWatcher{

    private int maxlength=0;
    private EditText editText;

    public MaxLengthWatcher(int maxlength, EditText editText) {
        this.maxlength = maxlength;
        this.editText = editText;
    }

    public void afterTextChanged(Editable arg0) {
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
    }
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        Editable editable = editText.getText();
        int len = editable.length();
        if(len > maxlength)
        {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0,maxlength);
            editText.setText(newStr);
            editable = editText.getText();
            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if(selEndIndex > newLen)
            {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
        }
    }
}

