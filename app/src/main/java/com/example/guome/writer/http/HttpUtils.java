package com.example.guome.writer.http;

import com.example.guome.writer.JavaBean.Easy;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guomn on 2018/6/22.
 */

public class HttpUtils {
    public static String getAllEasysJson(){
        String url="http://192.168.130.126/easymanagement/getAllEasys";
        return get(url);
    }

    /**
     * 获得全部文章 * 解析json数组
     *
     * @return
     */
    public static List<Easy> getAllEasys(){
        String url="http://192.168.130.126/easymanagement/getAllEasys";
        String json=get(url);
        //解析json数组
        List<Easy> easyList=new ArrayList<>();
        try{
            JSONObject jsonObject=new JSONObject(json);
            JSONArray getEasy_returns = jsonObject.getJSONArray("getEasy_returns");
            String re=jsonObject.getString("result");
//            JSONObject result=jsonObject.getJSONObject("result");
//            String re=result.toString();
            int r=Integer.parseInt(re);
            if(r==1) {
                //JSONArray jsonArray=new JSONArray(json);
                for (int i = 0; i < getEasy_returns.length(); i++) {
                    //JSONObject jsonObject=jsonArray.getJSONObject(i);
                    //if(getEasy_returns!=null){
                    int id = jsonObject.optInt("id");
                    String title = jsonObject.optString("title");
                    String content = jsonObject.optString("content");
                    String createData = jsonObject.optString("createData");
                    String updateData = jsonObject.optString("updateData");
                    String author = jsonObject.optString("author");
                    //封装成Easy对象
                    Easy easy = new Easy();
                    easy.setContent(content);
                    easy.setTitle(title);
                    easy.setId(id);
                    easy.setCreatedAt(createData);
                    easy.setUpdatedAt(updateData);
                    easy.setAuthor(author);
                    System.out.println("easy title is" + title + " and content is " + content);
                    easyList.add(easy);
                    }
                }
//            }
            return easyList;
        }catch(JSONException e){
            e.printStackTrace();
            return easyList;
        }

    }

    private static String get(String url) {
        HttpGet request = null;
        try {
        // 获得HttpGet对象
            request = new HttpGet(url);
        } catch (IllegalArgumentException e) {
        // TODO: handle exception
            e.printStackTrace();
        } catch (Exception e) {
        // TODO: handle exception
            e.printStackTrace();
        }
        if (request == null) {
            return null;
        }
        String result = null;
        try {
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (ClientProtocolException e) {
        // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
        // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }
}
