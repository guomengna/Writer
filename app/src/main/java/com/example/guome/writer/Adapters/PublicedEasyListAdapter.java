package com.example.guome.writer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.JavaBean.PublicedEasy;
import com.example.guome.writer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guome on 2018/8/2.
 */

public class PublicedEasyListAdapter extends BaseAdapter {
    public static List<PublicedEasy> publicedEasyList = new ArrayList<PublicedEasy>();
    private Context context;
    private List<PublicedEasyListAdapter.EasyItemViewHolder> holders = new ArrayList<PublicedEasyListAdapter.EasyItemViewHolder>();
    private PublicedEasyListAdapter.EasyItemViewHolder holder;

    public PublicedEasyListAdapter(Context context, List<PublicedEasy> list){
        super();
        this.context = context;
        PublicedEasyListAdapter.publicedEasyList = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return publicedEasyList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return publicedEasyList.get(position);
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
        PublicedEasy publicedEasy = publicedEasyList.get(position);
        if (convertView == null) {
            holder = new PublicedEasyListAdapter.EasyItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.publiced_easy_catalog, new LinearLayout(context));
            // 实例化组件
            // 单个listview各属性添加值
            holder.title = (TextView) convertView
                    .findViewById(R.id.title_easy);
            holder.content = (TextView) convertView
                    .findViewById(R.id.content);

            holders.add(holder);
            convertView.setTag(holder);
        } else {
            holder = (PublicedEasyListAdapter.EasyItemViewHolder) convertView.getTag();
        }
        holder.title.setText(publicedEasy.getTitle());
        holder.content.setText(publicedEasy.getContent());


        return convertView;
    }

    static class EasyItemViewHolder {
        TextView title;
        TextView content;

    }
    public TextView getHolder(){
        return null;
    }
}
