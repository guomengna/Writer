package com.example.guome.writer.MyTool;

import com.example.guome.writer.JavaBean.Easy;

import java.util.List;

/**
 * Created by guome on 2017/12/7.
 */

public class DeleteSingleton { private static DeleteSingleton ourInstance = new DeleteSingleton();
    private List<Easy> easyList;

    public static DeleteSingleton getInstance() {
        return ourInstance;
    }

    private DeleteSingleton() {
    }

    public List<Easy> getEasyList() {
        return easyList;
    }

    public void setEasyList(List<Easy> goodsList) {
        this.easyList = goodsList;
    }
}
