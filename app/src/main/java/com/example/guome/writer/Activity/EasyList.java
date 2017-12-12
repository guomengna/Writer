package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.MyTool.DeleteSingleton;
import com.example.guome.writer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by guome on 2017/12/6.
 */

public class EasyList extends Activity implements Button.OnClickListener{
    private ListView easyListView;
    private Button back;
    private SimpleAdapter productAdapter;
    private List<Easy> easy=new ArrayList<Easy>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easylist_layout);
        easyListView = (ListView) findViewById(R.id.listView);
        back=findViewById(R.id.backa);
        back.setOnClickListener(this);
        //queryEasy();
        queryByTime();
        easyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Map<String, Object> data = (Map<String, Object>) listView.getItemAtPosition(position);
                String objectId = data.get("objectId").toString();
                Intent intent = new Intent(EasyList.this, EasyDetailActivity.class);
                intent.putExtra("objectId", objectId);
                DeleteSingleton.getInstance().setEasyList(easy);
                startActivity(intent);
            }
        });
        easyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(EasyList.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView listView = (ListView) parent;
                        Map<String, Object> data = (Map<String, Object>) listView.getItemAtPosition(position);
                        String objectId = data.get("objectId").toString();
                        delete(objectId);
                        //创建SimpleAdapter适配器将数据绑定到query_catelog显示控件上，做好能够放在for循环的外边
                        easy.remove(position);
                        show(easy);
                    }
                });
                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backa:
                finish();
                break;
        }
    }

    public void show(List<Easy> easys){
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();

        for(Iterator iterator = easys.iterator(); iterator.hasNext();) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            Easy easy = (Easy) iterator.next();
            item.put("content", easy.getContent());
            item.put("objectId", easy.getObjectId());
            item.put("updatedAt", easy.getUpdatedAt());
            item.put("title", easy.getTitle());
            //将item添加到data中
            data.add(item);
        }
        //创建SimpleAdapter适配器将数据绑定到query_catelog显示控件上，做好能够放在for循环的外边
        productAdapter = new SimpleAdapter(this, data, R.layout.easy_catalog,
                new String[]{"content","title","updatedAt"},
                new int[]{R.id.content,R.id.title_easy,R.id.updated_time});
        //实现列表的显示
        easyListView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    public void delete(String objectId){
        Easy easys=new Easy();
        easys.setObjectId(objectId);
        easys.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        String type=input_type.getText().toString();
//        queryByType(type);
        if(productAdapter!=null){
            productAdapter.notifyDataSetChanged();
        }
        easy= DeleteSingleton.getInstance().getEasyList();
        if(easy!=null){
            show(easy);
        }
    }
    //查询不到内容,查询结果为0条，原因是模糊查询不能使用
    //理想方式应该是全部查出来，用时间，如方法queryByTime()所示
    public void queryEasy(){
        Toast.makeText(EasyList.this,"这是查询方法",Toast.LENGTH_SHORT).show();
        BmobQuery<Easy> query = new BmobQuery<Easy>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.addWhereEqualTo("content", "好");
        query.setLimit(50);
        Toast.makeText(EasyList.this,"即将进入查询执行",Toast.LENGTH_SHORT).show();
        //执行查询方法
        query.findObjects(new FindListener<Easy>() {
            @Override
            public void done(List<Easy> object, BmobException e) {
                if(e==null){
                    Toast.makeText(EasyList.this,"查询成功，共"+object.size()+"条数据",Toast.LENGTH_SHORT).show();
                    //临时list用于存放查询到的文章
                    List<Easy> easyTemp=new ArrayList<Easy>();
                    for (Easy singleEasy : object) {
                        //获得数据的objectId信息
                        singleEasy.getObjectId();
                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        singleEasy.getCreatedAt();
                        //获得内容信息
                        singleEasy.getContent();
                        easyTemp.add(singleEasy);
                    }
                    //将临时list的值赋给easy，用于显示出来
                    easy=easyTemp;
                    if(easy!=null){
                        Toast.makeText(EasyList.this,"easy+"+easy.size(),Toast.LENGTH_SHORT).show();
                        DeleteSingleton.getInstance().setEasyList(easy);
                        show(easy);
                    }
                }else{
                    Toast.makeText(EasyList.this,"查询失败",Toast.LENGTH_SHORT).show();
                    Toast.makeText(EasyList.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //使用时间的复合查询,到当前是时间为止的所有的数据都查询到
    public void queryByTime(){
        BmobQuery<Easy> query = new BmobQuery<Easy>();
        List<BmobQuery<Easy>> and = new ArrayList<BmobQuery<Easy>>();
        //大于00：00：00
        BmobQuery<Easy> q1 = new BmobQuery<Easy>();
        String start = "2015-05-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
        and.add(q1);
        //小于23：59：59
        BmobQuery<Easy> q2 = new BmobQuery<Easy>();
        //hh表示12小时制，HH表示24小时制
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sDateFormat.format(new java.util.Date());
        String end = currentTime;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1  = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.setLimit(50);
        query.findObjects(new FindListener<Easy>() {
            @Override
            public void done(List<Easy> object, BmobException e) {
                if(e==null){
                    Toast.makeText(EasyList.this,"查询成功，共"+object.size()+"条数据",Toast.LENGTH_SHORT).show();
                    //临时list用于存放查询到的文章
                    List<Easy> easyTemp=new ArrayList<Easy>();
                    for (Easy singleEasy : object) {
                        //获得数据的objectId信息
                        singleEasy.getObjectId();
                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        singleEasy.getCreatedAt();
                        singleEasy.getUpdatedAt();
                        singleEasy.getTitle();
                        //获得内容信息
                        singleEasy.getContent();
                        easyTemp.add(singleEasy);
                    }
                    //将临时list的值赋给easy，用于显示出来
                    easy=easyTemp;
                    if(easy!=null){
                        Toast.makeText(EasyList.this,"easy+"+easy.size(),Toast.LENGTH_SHORT).show();
                        DeleteSingleton.getInstance().setEasyList(easy);
                        show(easy);
                    }
                }else{
                    Toast.makeText(EasyList.this,"查询失败",Toast.LENGTH_SHORT).show();
                    Toast.makeText(EasyList.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
