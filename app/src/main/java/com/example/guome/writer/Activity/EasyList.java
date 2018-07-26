package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.guome.writer.Adapters.EasyListAdapter;
import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.MyTool.DeleteSingleton;
import com.example.guome.writer.R;
import com.example.guome.writer.interf.PullDownFreshInterface;
import com.example.guome.writer.interf.PullUpFreshInterface;
import com.example.guome.writer.server.WebServer;
import com.example.guome.writer.view.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
import okhttp3.Call;
import okhttp3.Response;

import static com.example.guome.writer.http.HttpUtils.getAllEasys;
import static com.example.guome.writer.http.HttpUtils.getAllEasysJson;

/**
 * Created by guome on 2017/12/6.
 * 查询文章列表类，将所有的文章查询出来，按照更新时间从大到小排列
 */

public class EasyList extends Activity implements Button.OnClickListener, PullDownFreshInterface, PullUpFreshInterface {
    private ListView easyListView;
    private Button back;
    private SimpleAdapter productAdapter;
    private List<Easy> easy=new ArrayList<Easy>();
    private Handler handler=new Handler();
    List<Easy> easyList1=new ArrayList<Easy>();
    List<Easy> easyListByAuthor=new ArrayList<>();
    private EasyListAdapter easyListAdapter;
    private PullToRefreshView pullRefreshBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easylist_layout);
        //增加访问web的权限，不推荐使用
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        easyListView = (ListView) findViewById(R.id.listView);
        back=findViewById(R.id.backa);
        back.setOnClickListener(this);
        pullRefreshBar=(PullToRefreshView)findViewById(R.id.refreshBar);
        pullRefreshBar.PullDownParent = this;
        pullRefreshBar.PullUpParent = this;
        //使用bmob作为后台的查询方法
//        queryByTime();
//        easyListAdapter = new EasyListAdapter(getApplication(),easyList1);
        //将adapter添加到listview中
        easyListView.setAdapter(easyListAdapter);
        //访问网络，获取nana的所有文章
//        try {
//            //获取所有文章
//            WebServer.getWebServer().getAllEasys(getAllEasyCallBack);
//            //根据作者名称获取该作者的所有文章
////            WebServer.getWebServer().getEasysByAuthor("nana",getEasyByAuthorCallBack);
//            Toast.makeText(EasyList.this,"This is EasyList.class,try to get easy by nana",Toast.LENGTH_LONG).show();
//        } catch (android.net.ParseException e) {
//            e.printStackTrace();
//        }
        initData();
        //
        easyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
//                Map<String, Object> data = (Map<String, Object>) listView.getItemAtPosition(position);
//                String objectId = data.get("objectId").toString();
                Easy data=(Easy) listView.getItemAtPosition(position);
                int easyid = data.getId();
                Intent intent = new Intent(EasyList.this, EasyDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("id", easyid);
//                intent.putExtras(bundle);
                intent.putExtra("id", easyid+"");
                DeleteSingleton.getInstance().setEasyList(easy);
                startActivity(intent);
            }
        });
        //长按事件监听，长按删除
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
//                        Map<String, Object> data = (Map<String, Object>) listView.getItemAtPosition(position);
                        //获取到被点击item的Easy对象
                        Easy data=(Easy) listView.getItemAtPosition(position);
                        //获取到被点击的easy的id
                        int easyid = data.getId();
                        System.out.print("id="+easyid);
                        delete(easyid);
                        //删除之后把列表中的这一项删掉
                        easyList1.remove(position);
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
    //初始化数据，访问网络的操作放在这个方法中，从主函数中脱离
    public void initData(){
        WebServer.getWebServer().getAllEasys(getAllEasyCallBack);
        //根据作者名称获取该作者的所有文章
//            WebServer.getWebServer().getEasysByAuthor("nana",getEasyByAuthorCallBack);
//        Toast.makeText(EasyList.this,"This is EasyList.class,try to get easy by nana",Toast.LENGTH_LONG).show();
    }
    //初次获取数据
    okhttp3.Callback getAllEasyCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            List<Easy> easyList=new ArrayList<>();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                JSONArray getEasy_returns = new JSONArray(jsonObject.getString("getEasy_returns"));
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    for (int i = 0; i < getEasy_returns.length(); i++) {
                        JSONObject jsonObject1 = getEasy_returns.getJSONObject(i);
                        //JSONObject jsonObject=jsonArray.getJSONObject(i);
                        //if(getEasy_returns!=null){
                        int id = Integer.parseInt(jsonObject1.getString("id"));
                        String title = jsonObject1.getString("title");
                        String content = jsonObject1.getString("content");
                        String createData = jsonObject1.getString("createData");
                        String updateData = jsonObject1.getString("updateData");
                        String author = jsonObject1.getString("author");

                        //封装成Easy对象
                        Easy easy = new Easy();
                        easy.setContent(content);
                        easy.setTitle(title);
                        easy.setId(id);
                        easy.setCreateData(createData);
                        easy.setUpdateData(updateData);
                        easy.setAuthor(author);
                        System.out.println("easy title is" + title + " and content is " + content);
                        easyList.add(easy);
                    }
                    easyList1=easyList;
                }
            }catch(Exception e){

                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    easyListAdapter.notifyDataSetChanged();
                    //初次显示列表数据需要初始化，与下拉刷新与上拉显示是不同的。
                    easyListAdapter = new EasyListAdapter(getApplication(),easyList1);
                    //将adapter添加到listview中
                    easyListView.setAdapter(easyListAdapter);
                }
            });
        }
    };
    //根据作者的名称获取数据的回调方法，未经测试
    okhttp3.Callback getEasyByAuthorCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            List<Easy> easyList=new ArrayList<>();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                JSONArray getEasy_returns = new JSONArray(jsonObject.getString("getEasy_returns"));
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    for (int i = 0; i < getEasy_returns.length(); i++) {
                        JSONObject jsonObject1 = getEasy_returns.getJSONObject(i);
                        //JSONObject jsonObject=jsonArray.getJSONObject(i);
                        //if(getEasy_returns!=null){
                        int id = Integer.parseInt(jsonObject1.getString("id"));
                        String title = jsonObject1.getString("title");
                        String content = jsonObject1.getString("content");
                        String createData = jsonObject1.getString("createData");
                        String updateData = jsonObject1.getString("updateData");
                        String author = jsonObject1.getString("author");

                        //封装成Easy对象
                        Easy easy = new Easy();
                        easy.setContent(content);
                        easy.setTitle(title);
                        easy.setId(id);
                        easy.setCreateData(createData);
                        easy.setUpdateData(updateData);
                        easy.setAuthor(author);
                        System.out.println("easy title is" + title + " and content is " + content);
                        easyList.add(easy);
//                        Toast.makeText(EasyList.this, "easylist's size is"+ easyList.size(), Toast.LENGTH_SHORT).show();
                    }
                    easyListByAuthor=easyList;
//                    Toast.makeText(EasyList.this, "easylist's size is"+ easyList.size(), Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){

                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //更新list列表
                    easyListAdapter.notifyDataSetChanged();
                    Toast.makeText(EasyList.this, "删除成功,easyListByAuthor's size is"+easyListByAuthor.size(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backa:
                finish();
                break;
        }
    }
    //把查询到的数据显示在列表中
//    public void show(List<Easy> easys){
//        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
//
//        for(Iterator iterator = easys.iterator(); iterator.hasNext();) {
//            HashMap<String, Object> item = new HashMap<String, Object>();
//            Easy easy = (Easy) iterator.next();
//            item.put("content", easy.getContent());
//            item.put("objectId", easy.getObjectId());
//            item.put("updatedAt", easy.getUpdatedAt());
//            item.put("title", easy.getTitle());
//            //将item添加到data中
//            data.add(item);
//        }
//        //创建SimpleAdapter适配器将数据绑定到query_catelog显示控件上，做好能够放在for循环的外边
//        productAdapter = new SimpleAdapter(this, data, R.layout.easy_catalog,
//                new String[]{"content","title","updatedAt"},
//                new int[]{R.id.content,R.id.title_easy,R.id.updated_time});
//        //实现列表的显示
//        easyListView.setAdapter(productAdapter);
//        productAdapter.notifyDataSetChanged();
//    }

//    public void delete(String objectId){
//        Easy easys=new Easy();
//        easys.setObjectId(objectId);
//        easys.delete(new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("bmob","成功");
//                }else{
//                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                }
//            }
//        });
//    }
    //根据id删除
    public void delete(int id){
        WebServer.getWebServer().deleteEasyById(id,deleteEasyByIdCallBack);

    }
    //删除的回调函数
    okhttp3.Callback deleteEasyByIdCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    System.out.print("删除成功");
                }else{
                    System.out.print("删除失败");
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    easyListAdapter.notifyDataSetChanged();
                    Toast.makeText(EasyList.this, "删除成功"+easyListByAuthor.size(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
//        if(productAdapter!=null){
//            productAdapter.notifyDataSetChanged();
//        }
//        easy= DeleteSingleton.getInstance().getEasyList();
//        if(easy!=null){
//            show(easy);
//        }
//        queryByTime();
    }
    //查询不到内容,查询结果为0条，原因是模糊查询不能使用
    //理想方式应该是全部查出来，用时间，如方法queryByTime()所示
//    public void queryEasy(){
//        Toast.makeText(EasyList.this,"这是查询方法",Toast.LENGTH_SHORT).show();
//        BmobQuery<Easy> query = new BmobQuery<Easy>();
//        //返回50条数据，如果不加上这条语句，默认返回10条数据
//        query.addWhereEqualTo("content", "好");
//        query.setLimit(50);
//        Toast.makeText(EasyList.this,"即将进入查询执行",Toast.LENGTH_SHORT).show();
//        //执行查询方法
//        query.findObjects(new FindListener<Easy>() {
//            @Override
//            public void done(List<Easy> object, BmobException e) {
//                if(e==null){
//                    Toast.makeText(EasyList.this,"查询成功，共"+object.size()+"条数据",Toast.LENGTH_SHORT).show();
//                    //临时list用于存放查询到的文章
//                    List<Easy> easyTemp=new ArrayList<Easy>();
//                    for (Easy singleEasy : object) {
//                        //获得数据的objectId信息
//                        singleEasy.getObjectId();
//                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
//                        singleEasy.getCreatedAt();
//                        //获得内容信息
//                        singleEasy.getContent();
//                        easyTemp.add(singleEasy);
//                    }
//                    //将临时list的值赋给easy，用于显示出来
//                    easy=easyTemp;
//                    if(easy!=null){
//                        Toast.makeText(EasyList.this,"easy+"+easy.size(),Toast.LENGTH_SHORT).show();
//                        DeleteSingleton.getInstance().setEasyList(easy);
//                        show(easy);
//                    }
//                }else{
//                    Toast.makeText(EasyList.this,"查询失败",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EasyList.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
    //使用时间的复合查询,到当前是时间为止的所有的数据都查询到
//    public void queryByTime(){
//        BmobQuery<Easy> query = new BmobQuery<Easy>();
//        List<BmobQuery<Easy>> and = new ArrayList<BmobQuery<Easy>>();
//        //大于00：00：00
//        BmobQuery<Easy> q1 = new BmobQuery<Easy>();
//        String start = "2015-05-01 00:00:00";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date  = null;
//        try {
//            date = sdf.parse(start);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
//        and.add(q1);
//        //小于23：59：59
//        BmobQuery<Easy> q2 = new BmobQuery<Easy>();
//        //hh表示12小时制，HH表示24小时制
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String currentTime = sDateFormat.format(new java.util.Date());
//        String end = currentTime;
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date1  = null;
//        try {
//            date1 = sdf1.parse(end);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        q2.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date1));
//        and.add(q2);
//        //添加复合与查询
//        query.and(and);
//        query.setLimit(50);
//        //按照最后更新时间的降序排列
//        query.order("-updatedAt");
//        query.findObjects(new FindListener<Easy>() {
//            @Override
//            public void done(List<Easy> object, BmobException e) {
//                if(e==null){
////                    Toast.makeText(EasyList.this,"查询成功，共"+object.size()+"条数据",Toast.LENGTH_SHORT).show();
//                    //临时list用于存放查询到的文章
//                    List<Easy> easyTemp=new ArrayList<Easy>();
//                    for (Easy singleEasy : object) {
//                        //获得数据的objectId信息
//                        singleEasy.getObjectId();
//                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
//                        singleEasy.getCreatedAt();
//                        singleEasy.getUpdatedAt();
//                        singleEasy.getTitle();
//                        //获得内容信息
//                        singleEasy.getContent();
//                        easyTemp.add(singleEasy);
//                    }
//                    //将临时list的值赋给easy，用于显示出来
//                    easy=easyTemp;
//                    if(easy!=null){
////                        Toast.makeText(EasyList.this,"easy+"+easy.size(),Toast.LENGTH_SHORT).show();
//                        DeleteSingleton.getInstance().setEasyList(easy);
//                        show(easy);
//                        productAdapter.notifyDataSetChanged();
//                    }
//                }else{
//                    Toast.makeText(EasyList.this,"查询失败",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EasyList.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    protected void onStart() {
        super.onStart();
//        queryByTime();
    }

    /**
     * 添加下拉刷新
     */
    //下拉刷新
    @Override
    public void PullDownFresh() {

        try {
            easyList1.clear();
//            WebServer.getWebServer().getribao(companyid,userid,1,10,keyWord.getText().toString(),getAllRibaoCallBackdown);
            WebServer.getWebServer().getAllEasys(getAllEasyFlashCallBack);
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
    }
    //下拉刷新的回调函数
    okhttp3.Callback getAllEasyFlashCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            List<Easy> easyList=new ArrayList<>();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                JSONArray getEasy_returns = new JSONArray(jsonObject.getString("getEasy_returns"));
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    for (int i = 0; i < getEasy_returns.length(); i++) {
                        JSONObject jsonObject1 = getEasy_returns.getJSONObject(i);
                        //JSONObject jsonObject=jsonArray.getJSONObject(i);
                        //if(getEasy_returns!=null){
                        int id = Integer.parseInt(jsonObject1.getString("id"));
                        String title = jsonObject1.getString("title");
                        String content = jsonObject1.getString("content");
                        String createData = jsonObject1.getString("createData");
                        String updateData = jsonObject1.getString("updateData");
                        String author = jsonObject1.getString("author");

                        //封装成Easy对象
                        Easy easy = new Easy();
                        easy.setContent(content);
                        easy.setTitle(title);
                        easy.setId(id);
                        easy.setCreateData(createData);
                        easy.setUpdateData(updateData);
                        easy.setAuthor(author);
                        System.out.println("easy title is" + title + " and content is " + content);
                        easyList.add(easy);
                    }
                    easyList1=easyList;
                }
            }catch(Exception e){

                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //更新list列表
                    Toast.makeText(EasyList.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    pullRefreshBar.onHeaderRefreshComplete();
                    easyListAdapter.notifyDataSetChanged();
                    easyListAdapter = new EasyListAdapter(getApplication(),easyList1);
                    //将adapter添加到listview中
                    easyListView.setAdapter(easyListAdapter);
                }
            });
        }
    };
    /**
     * 添加上拉加载
     */
    //上拉加载
    @Override
    public void PullUpFresh() {
//        Toast.makeText(DayLogListActivity.this,keyWord.getText().toString(),Toast.LENGTH_SHORT).show();
        try {
            WebServer.getWebServer().getAllEasys(getAllEasyFlashCallBack);
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
    }
}
