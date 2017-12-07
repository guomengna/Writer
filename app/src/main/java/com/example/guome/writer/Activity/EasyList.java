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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
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
    private List<Easy> easy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easylist_layout);

        easyListView = (ListView) findViewById(R.id.listView);
        back=findViewById(R.id.backa);
        back.setOnClickListener(this);
        queryEasy();
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
            //将item添加到data中
            data.add(item);
        }
        //创建SimpleAdapter适配器将数据绑定到query_catelog显示控件上，做好能够放在for循环的外边
        productAdapter = new SimpleAdapter(this, data, R.layout.easy_catalog,
                new String[]{"content"},
                new int[]{R.id.content});
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
    //查询不到内容,有空指针错误
    public void queryEasy(){
        Toast.makeText(EasyList.this,"这是查询方法",Toast.LENGTH_SHORT).show();
        BmobQuery<Easy> query = new BmobQuery<Easy>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.addWhereContains("content", "好");
        query.setLimit(50);
        Toast.makeText(EasyList.this,"即将进入查询执行",Toast.LENGTH_SHORT).show();
        //执行查询方法
        query.findObjects(new FindListener<Easy>() {
            @Override
            public void done(List<Easy> object, BmobException e) {
                if(e==null){
                    Toast.makeText(EasyList.this,"查询成功，共"+object.size()+"条数据",Toast.LENGTH_SHORT).show();
                    for (Easy easy : object) {
                        //获得数据的objectId信息
                        easy.getObjectId();
                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        easy.getCreatedAt();
                        //获得内容信息
                        easy.getContent();
                    }
                    DeleteSingleton.getInstance().setEasyList(easy);
                    show(easy);
                }else{
                    Toast.makeText(EasyList.this,"查询失败",Toast.LENGTH_SHORT).show();
                    Toast.makeText(EasyList.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
