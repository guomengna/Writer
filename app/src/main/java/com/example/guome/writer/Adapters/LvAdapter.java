package com.example.guome.writer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.R;

import java.util.List;

/**
 * Created by guome on 2018/2/25.
 */

public class LvAdapter extends BaseAdapter {
    private Context context;
    private List<Easy> localEasyList;

    private LayoutInflater inflater;
    //默认选择项
    private int selectItem = 0;
    public LvAdapter(List<Easy> localEasyList, Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.localEasyList=localEasyList;
    }
    public void setLocalEasyList(List<Easy> localEasyList) {
        this.localEasyList = localEasyList;
    }

    @Override
    public int getCount() {
        return localEasyList.size();
    }

    @Override
    public Object getItem(int position) {
        return localEasyList.get(position);
    }

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Viewholder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.easy_catalog, null);
            vh = new Viewholder();
            vh.tv_content = (TextView) convertView.findViewById(R.id.content);
            vh.tv_updatedTime = (TextView) convertView.findViewById(R.id.updated_time);
            vh.tv_titleEeasy = (TextView) convertView.findViewById(R.id.title_easy);
            convertView.setTag(vh);
        }else {
            vh = (Viewholder)convertView.getTag();
        }
        vh.tv_content.setText(localEasyList.get(position).getContent());
        vh.tv_updatedTime.setText(localEasyList.get(position).getUpdateData());
        vh.tv_titleEeasy.setText(localEasyList.get(position).getTitle());

        return convertView;
//          TextView tv = new TextView(context.getApplicationContext());
//          tv.setText(list.get(position));
//          return tv;
    }

    public static class Viewholder {
        TextView tv_content,tv_updatedTime,tv_titleEeasy;
    }

}
