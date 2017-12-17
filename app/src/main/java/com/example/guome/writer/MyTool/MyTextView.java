package com.example.guome.writer.MyTool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by guome on 2017/12/17.
 */

public class MyTextView extends TextView {
        private Paint mPaint1, mPaint2;
        private Canvas canvas;

        public MyTextView(Context context)
        {
            super(context);
        }

        public MyTextView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
//            mPaint1 = new Paint();
//            mPaint1.setColor(getResources().getColor(android.R.color.holo_blue_light));
//            mPaint1.setStyle(Paint.Style.FILL);
            mPaint2 = new Paint();
            mPaint2.setColor(Color.YELLOW);
            mPaint2.setStyle(Paint.Style.FILL);
            canvas = new Canvas();
        }

        /**
         * 修改TextView需要重写onDraw()
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas)
        {
            //绘制外层矩形
//            canvas.drawRect(
//                    0,
//                    0,
//                    getMeasuredWidth(),
//                    getMeasuredHeight(),
//                    mPaint1);
            //绘制内层矩形
            canvas.drawRect(
                    10,
                    10,
                    getMeasuredWidth() - 10,
                    getMeasuredHeight() - 10,
                    mPaint2);
            canvas.save();
            //绘制文字前平移10像素
            canvas.translate(10, 0);
            //在回调父类方法前，实现自己的逻辑，对TextView来说即是在绘制文本内容前
            //父类完成的方法，即绘制文本
            super.onDraw(canvas);
            //在回调父类方法后，实现自己的逻辑，对TextView来说即是在绘制文本内容后
            canvas.restore();
        }
}

