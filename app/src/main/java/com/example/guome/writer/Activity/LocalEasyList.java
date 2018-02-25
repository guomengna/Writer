package com.example.guome.writer.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.MyTool.MyListView;
import com.example.guome.writer.R;
import java.util.ArrayList;
import java.util.List;
import com.example.guome.writer.Adapters.LvAdapter;

/**
 * Created by guome on 2018/2/25.
 * 为本地文章显示列表创建的适配器，实现下拉刷新
 * 已经经过测试数据的测试
 */

public class LocalEasyList extends Activity{
    //存放本地文章数据的列表
    private List<Easy> localEasyList;
    private MyListView lv;
    private LvAdapter lvadapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localeasylist_layout);
        lv = findViewById(R.id.lv);

        localEasyList=new ArrayList<Easy>();
        Easy easy1 = new Easy();
        easy1.setTitle("苹果");
        easy1.setContent("苹果红色的比较好吃");
        Easy easy2 = new Easy();
        easy2.setTitle("香蕉");
        easy2.setContent("香蕉红色的可能不能吃");
        localEasyList.add(easy1);
        localEasyList.add(easy2);

        //实例化adapter
        lvadapter = new LvAdapter(localEasyList,this);
        //将数据传入adapter
        lvadapter.setLocalEasyList(localEasyList);
        //默认第一个选中
        lvadapter.setSelectItem(0);
        //将adapter添加到listview中
        lv.setAdapter(lvadapter);

        lv.setonRefreshListener(new MyListView.OnRefreshListener(){
            @Override
            public void onRefresh() {
                new AsyncTask<Void,Void,Void>(){
                    protected Void doInBackground(Void... params){
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        if (localEasyList.size() > 0) {
                            localEasyList.removeAll(localEasyList);
                        }
                        Easy easy3 = new Easy();
                        easy3.setTitle("草莓");
                        easy3.setContent("草莓红色的比较好吃");
                        localEasyList.add(easy3);
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        lvadapter.notifyDataSetChanged();
                        lv.onRefreshComplete();
                    }
                }.execute(null,null,null);
            }
        });
    }
}
