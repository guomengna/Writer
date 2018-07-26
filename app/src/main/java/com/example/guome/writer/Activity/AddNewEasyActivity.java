package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.JavaBean.User;
import com.example.guome.writer.MyTool.SQLiteHelper;
import com.example.guome.writer.R;
import com.example.guome.writer.app.MyApplication;
import com.example.guome.writer.server.WebServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.guome.writer.http.HttpUtils.getAllEasysJson;

/**
 * Created by guome on 2017/10/9.
 * 图片上传先注释掉，并不能获取和上传图片
 */

/**
 * 只要不是“相机”中的照片就可以显示，为什么？？？
 */

public class AddNewEasyActivity extends Activity implements Button.OnClickListener{
    private final int PICK_PIC = 1;
    private EditText addneweasy;//编辑区域
    private TextView submit;//提交按钮
    private String content;//文章内容
    private String title;//文章题目
    private String createData;//文章创建时间
    private String updateData;//文章更新时间
    private String author;//文章创建作者
    private ImageButton back;//返回按钮
    private int mImgViewWidth;
    private float mInsertedImgWidth;
    private ViewTreeObserver vto;
    private ImageButton choose_pic_btn;
    private ImageButton cancel_btn;
    ProgressBar progressBar;
    private ProgressDialog progressDialog;//进度显示框
    private String uplaodImg;
    private Uri uri;
    private SQLiteHelper helper=new SQLiteHelper(this);
    private Handler handler=new Handler();
    private User currentLoginUser=new User();
    private String currentUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addneweasy_layout);
        addneweasy=findViewById(R.id.addneweasy_edittext);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back=findViewById(R.id.fanhui);
        back.setOnClickListener(this);
        choose_pic_btn=findViewById(R.id.choose_pic_btn);
        cancel_btn=findViewById(R.id.cancel_btn);
        choose_pic_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        // 获取保存的用户名和密码
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if (sharedPreferences != null) {
            currentUsername=sharedPreferences.getString("username", "");
            Toast.makeText(AddNewEasyActivity.this,"currentLoginUser's name="+currentUsername,Toast.LENGTH_SHORT);
        }
        vto = addneweasy.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addneweasy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mImgViewWidth = addneweasy.getWidth();
                mInsertedImgWidth = mImgViewWidth * 0.8f;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.submit:
                //调用提交方法,首先提交到本地数据库，再提交成功则跳转，否则显示失败，重新提交
                //跳转到text view界面，不再可编辑
                submit();
//                finish();

                break;
            case R.id.fanhui:
                //保存到本地，之后退出
                finish();
                break;
            case R.id.choose_pic_btn:
                Intent intent1;
                //添加图片的主要代码
                intent1 = new Intent();
                //设定类型为image
                intent1.setType("image/*");
                //设置action
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                //选中相片后返回本Activity
                startActivityForResult(intent1, 1);
                break;
        }
    }
    public void submit(){
        Toast.makeText(AddNewEasyActivity.this,"点击了完成按钮",Toast.LENGTH_SHORT).show();
        progressDialog = new ProgressDialog(AddNewEasyActivity.this);
        progressDialog.show();
        //收集文章的数据
        content=addneweasy.getText().toString();
        if(content.length()>5){
            //检测是否有换行，若有且换行时字符数量少于5则截止到换行为止
            String titleTemp=content.substring(0,5);
            for(int i=0;i<titleTemp.length();i++){
                if(titleTemp.substring(i,i+1).equals("\n")){
                    Toast.makeText(AddNewEasyActivity.this,"第"+i+"个是换行字符",Toast.LENGTH_SHORT).show();
                    title=content.substring(0,i);
                    Toast.makeText(AddNewEasyActivity.this,"title是"+title,Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    title=titleTemp;
                }
            }
        }else{
            title=content;
        }
        createData=getCurrentTime().toString();
        updateData=getCurrentTime().toString();

        //获取当前登录的作者名
        author=currentUsername;
//        //封装进Easy对象中
//        Easy easy=new Easy();
//        easy.setContent(content);
//        easy.setTitle(title);
//        easy.setCreateData(getCurrentTime().toString());
//        easy.setUpdateData(getCurrentTime().toString());
//        easy.setAuthor("nana");
        try {
            WebServer.getWebServer().addEasy(title,content,author,createData,updateData,addEasyCallBack);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        easy.save(new SaveListener<String>() {
//            @Override
//            public void done(String objectId,BmobException e) {
//                if(e==null){
//                    progressDialog.dismiss();
//                    Toast.makeText(AddNewEasyActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
//                    //提交图片的方法，放在文章上传成功之中
//                    //图片路径
//                    /*
//                    String picPath = getRealFilePath(AddNewEasyActivity.this,uri);
//                    String picstr="storage/1EF9-1702/DCIM/Camera/IMG_20171028_175422.jpg";
//                    File file =new File(picPath);
//                    BmobFile bmobFile = new BmobFile(file);//停在这一步，为什么？？？
//                    bmobFile.uploadblock(new UploadFileListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if(e==null){
//                                Toast.makeText(AddNewEasyActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(AddNewEasyActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
//                                helper.insert(title,content);
//                            }
//                        }
//                        @Override
//                        public void onProgress(Integer value) {
//                            // 返回的上传进度（百分比）
//                        }
//                    });
//                    */
//                }else{
//                    progressDialog.dismiss();
//                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                    Toast.makeText(AddNewEasyActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
//                    helper.insert(title,content);
//                }
//            }
//        });
    }
    okhttp3.Callback addEasyCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                 @Override
                 public void run() {
                     progressDialog.dismiss();
//                     finish();
                     //关闭之前保存在本地

                     Toast.makeText(AddNewEasyActivity.this, "插入失败", Toast.LENGTH_SHORT).show();
                 }
             }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    System.out.print("插入成功");
                }else{
                    System.out.print("插入失败");
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    finish();
                    Intent easyListIntent=new Intent();
                    easyListIntent.setClass(AddNewEasyActivity.this,EasyList.class);
                    startActivity(easyListIntent);
                    Toast.makeText(AddNewEasyActivity.this,
                            "插入成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    /**
     * 获取当前时间方法
     * @return
     */
    public String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime=df.format(new Date());
        return currentTime;
    }
    //选择照片或是相机
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //取得数据
            uri = data.getData();
            Toast.makeText(AddNewEasyActivity.this,getRealFilePath(AddNewEasyActivity.this,uri),Toast.LENGTH_SHORT).show();
            ContentResolver cr = AddNewEasyActivity.this.getContentResolver();
            Bitmap bitmap = null;
            Bundle extras = null;
            //如果是选择照片
            if(requestCode == 1){
                try {
                    //将对象存入Bitmap中
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            int imgWidth = bitmap.getWidth();
            int imgHeight = bitmap.getHeight();
            double partion = imgWidth*1.0/imgHeight;
            double sqrtLength = Math.sqrt(partion*partion + 1);
            //新的缩略图大小
            double newImgW = 960*(partion / sqrtLength);
            double newImgH = 960*(1 / sqrtLength);
            float scaleW = (float) (newImgW/imgWidth);
            float scaleH = (float) (newImgH/imgHeight);
            Matrix mx = new Matrix();
            //对原图片进行缩放
            mx.postScale(scaleW, scaleH);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
            final ImageSpan imageSpan = new ImageSpan(this,bitmap);
            getRealFilePath(AddNewEasyActivity.this,uri);
            SpannableString spannableString = new SpannableString(getRealFilePath(AddNewEasyActivity.this,uri));
            spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
            //光标移到下一行
            addneweasy.append("\n\n");
            Editable editable = addneweasy.getEditableText();
            int selectionIndex = addneweasy.getSelectionStart();
            spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
            //将图片添加进EditText中
            editable.insert(selectionIndex, spannableString);
            //添加图片后自动空出1行
            addneweasy.append("\n\n");
            addneweasy.setSelection(addneweasy.getText().toString().length());//光标移到最后
        }
    }

    //把图片显示出来
    private void insertIntoEditText(SpannableString ss) {
        // 先获取Edittext中原有的内容
        Editable et = addneweasy.getText();
        int start = addneweasy.getSelectionStart();
        // 设置ss要添加的位置
        et.insert(start, ss);
        // 把et添加到Edittext中
        addneweasy.setText(et);
        // 设置Edittext光标在最后显示
        addneweasy.setSelection(start + ss.length());
    }

    /**
     * EditText中可以接收的图片(要转化为SpannableString)
     *
     * @param pic
     * @param uri
     * @return SpannableString
     */
    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();
        // 只对大尺寸图片进行下面的压缩，小尺寸图片使用原图
        if (imgWidth >= mInsertedImgWidth) {
            float scale = (float) mInsertedImgWidth / imgWidth;
            Matrix mx = new Matrix();
            mx.setScale(scale, scale);
            pic = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, mx, true);
        }
        String smile = uri.getPath();
        SpannableString ss = new SpannableString(smile);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, smile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    private Bitmap getOriginalBitmap(Uri photoUri) {
        if (photoUri == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            ContentResolver conReslv = getContentResolver();
            // 得到选择图片的Bitmap对象
            uplaodImg = getUrlFromUri(this,photoUri);
            //bitmap = MediaStore.Images.Media.getBitmap(conReslv, uplaodImg);
            bitmap = decodeSampledBitmapFromFile(uplaodImg,200,200);//btimap为空？？
        } catch (Exception e) {
        }
        return bitmap;
    }
    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize);
    }
    //显示缩略图
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
        //如果inSampleSize是2的倍数，也就说这个src已经是我们想要的缩略图了，直接返回即可。
        if (inSampleSize % 2 == 0) {
            return src;
        }
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    public String getUrlFromUri(final Context context, final Uri uri) {
        Log.d("DDD", "正在执行getUrlFromUri()方法");
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    /** 通过uri转换成bitmip */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        Log.d("DDD", "正在执行getBitmapFromUri()方法");
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    mContext.getContentResolver(), uri);
            Log.d("DDD", "bitmap方法"+bitmap.toString());
            return bitmap;
        } catch (Exception e) {
            Log.d("DDD", "Exception方法");
            e.printStackTrace();
            return null;
        }
    }


}
