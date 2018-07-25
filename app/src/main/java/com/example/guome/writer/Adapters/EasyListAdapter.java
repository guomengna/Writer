package com.example.guome.writer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.R;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guome on 2018/7/25.
 */

public class EasyListAdapter extends BaseAdapter {
//    private Context context;
//    private List<Easy> easyList;
//
//    private LayoutInflater inflater;
//    //默认选择项
//    private int selectItem = 0;
//    public EasyListAdapter(List<Easy> easyList, Context context){
//        inflater = LayoutInflater.from(context);
//        this.context = context;
//        this.easyList=easyList;
//    }
//    public void setEasyList(List<Easy> localEasyList) {
//        this.easyList = localEasyList;
//    }
//
//    @Override
//    public int getCount() {
//        return easyList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return easyList.get(position);
//    }
//
//    public  void setSelectItem(int selectItem) {
//        this.selectItem = selectItem;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        Viewholder vh;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.easy_catalog, null);
//            vh = new Viewholder();
//            vh.tv_content = (TextView) convertView.findViewById(R.id.content);
//            vh.tv_updatedTime = (TextView) convertView.findViewById(R.id.updated_time);
//            vh.tv_titleEeasy = (TextView) convertView.findViewById(R.id.title_easy);
//            convertView.setTag(vh);
//        }else {
//            vh = (Viewholder)convertView.getTag();
//        }
//        vh.tv_content.setText(easyList.get(position).getContent());
//        vh.tv_updatedTime.setText(easyList.get(position).getUpdatedAt());
//        vh.tv_titleEeasy.setText(easyList.get(position).getTitle());
//
//        return convertView;
////          TextView tv = new TextView(context.getApplicationContext());
////          tv.setText(list.get(position));
////          return tv;
//    }
//
//    public static class Viewholder {
//        TextView tv_content,tv_updatedTime,tv_titleEeasy;
//    }
    public static List<Easy> easyList = new ArrayList<Easy>();
    private Context context;
    private List<EasyItemViewHolder> holders = new ArrayList<EasyItemViewHolder>();
    private EasyItemViewHolder holder;

    public EasyListAdapter(Context context, List<Easy> list){
        super();
        this.context = context;
        EasyListAdapter.easyList = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return easyList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return easyList.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder = null;
        Easy easy = easyList.get(position);
        if (convertView == null) {
            holder = new EasyItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.easy_catalog, new LinearLayout(context));
            // 实例化组件
            // 单个listview各属性添加值
            holder.title = (TextView) convertView
                    .findViewById(R.id.title_easy);// 用户名
            holder.updateDate = (TextView) convertView
                    .findViewById(R.id.updated_time);// 日期
            holder.content = (TextView) convertView
                    .findViewById(R.id.content);// 本周完成工作

            holders.add(holder);
            convertView.setTag(holder);
        } else {
            holder = (EasyItemViewHolder) convertView.getTag();
        }
        holder.title.setText(easy.getTitle());
        holder.content.setText(easy.getContent());
        holder.updateDate.setText(easy.getUpdateData());

        return convertView;
    }

    static class EasyItemViewHolder {
        TextView title;
        TextView updateDate;
        TextView content;

    }
    public TextView getHolder(){
        return null;
    }

}
