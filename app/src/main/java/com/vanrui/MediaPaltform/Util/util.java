package com.vanrui.MediaPaltform.Util;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.vanrui.MediaPaltform.Constants;
import com.vanrui.MediaPaltform.Model.Meg;
import com.vanrui.MediaPaltform.Model.Pic;
import com.vanrui.MediaPaltform.Parser.MediaParser;
import com.vanrui.MediaPaltform.R;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by wxm on 2015/1/8 in PC
 */

/**
 * 工具类
 * @author wangxm-wr
 * @since 2015/1/15
 */
public class util {
    public util() {
    }

    /**
     * 返回二次解析的结果
     * @param arrary 解析到的xml数据
     * @return HashMap{video:{desc,imageurl,videourl}或者HashMap{pics:{desc,imageurl1,imageurl2,...}}或者HashMap{web:{desc,imageurl,weburl}}
     */
    public static ArrayList<HashMap<String,String[]>> process(ArrayList<Object> arrary) {
        ArrayList<HashMap<String,String[]>> arrayListHashMap=new ArrayList<HashMap<String, String[]>>();
        HashMap<String,String[]> hashMap;
        int lenght = arrary.size();
        for (int i = 0; i < lenght; i++) {
            ArrayList<Object> content;
            content = (ArrayList<Object>)arrary.get(i);
            Meg meg=(Meg)content.get(0);
            ArrayList<Pic> picArrary=(ArrayList<Pic>)content.get(1);
            hashMap=new HashMap<String, String[]>();
            if(meg.getType().equals(Constants.Media_Type.VIDEO)){
                String []strs={meg.getDescri(),picArrary.get(0).getPicUrl(),meg.getVideoUrl(),};
                hashMap.put(Constants.Media_Type.VIDEO,strs);
            }else if(meg.getType().equals(Constants.Media_Type.PIC)){
                int picLength=picArrary.size()+1;
                String strs[]=new String[picLength];
                strs[0]=meg.getDescri();
                for(int j=1;j<picLength;j++){
                    Pic pic=picArrary.get(j-1);
                    strs[j]=pic.getPicUrl();
                }
                hashMap.put(Constants.Media_Type.PICTURE,strs);
            }else if(meg.getType().equals(Constants.Media_Type.WEB)){                    /// {web:{desc,picurl,videourl}}
                String []strs={meg.getDescri(),picArrary.get(0).getPicUrl(),meg.getWebUrl(),};
                hashMap.put(Constants.Media_Type.WEB,strs);
            }
            arrayListHashMap.add(hashMap);
        }
        return arrayListHashMap;
    }

    /**
     * 获取描述文字列表或者图片URL列表
     * @param arrayListHash 第二步解析到的XML结果
     * @param type 需要的类型 Constants.Media_Type.VIDEO Constants.Media_Type.PICTURE Constants.Media_Type.WEB
     * @param descOrPic 是否需要的是描述列表
     * @return 文字列表或者图片URL列表
     */
    public static String[] getGridDescOrPic(ArrayList<HashMap<String, String[]>> arrayListHash, String type, boolean descOrPic) {
        int len = arrayListHash.size();
        Vector<String> vector = new Vector();
        for (int i = 0; i < len; i++) {
            HashMap<String, String[]> hashMap = arrayListHash.get(i);
            if (hashMap.containsKey(type)) {
                if (descOrPic) {
                    vector.add(hashMap.get(type)[0]);
                } else {
                    vector.add(hashMap.get(type)[1]);
                }
            }
        }
        String[] strings = new String[vector.size()];
        vector.toArray(strings);
        return strings;
    }


    /**
     * 获取二级图片数组
     * @param arrayListHash 第二步解析到的XML结果
     * @param position 图片位置
     * @return 二级图片数组
     */
    public static String[] getFlipPics(ArrayList<HashMap<String, String[]>> arrayListHash, int position) {
        int len = arrayListHash.size();
        String type = Constants.Media_Type.PICTURE;
        Vector<String> vector = new Vector<String>();
        String []strings = new String[0];
        for (int i = 0; i < len; i++) {
            HashMap<String, String[]> hashMap = arrayListHash.get(i);
            if (hashMap.containsKey(type) && --position == 0) {
                int picLen=hashMap.get(type).length;
                for (int j=1;j<picLen;j++){
                    vector.add(0,hashMap.get(type)[j]);
                }
                strings=new String[picLen-1];
                Collections.reverse(vector);
                vector.toArray(strings);
            }
        }
        return strings;
    }

    /**
     * 获取视频或者网址的URL
     * @param arrayListHash  第二步解析到的XML结果
     * @param type 类型  Constants.Media_Type.VIDEO Constants.Media_Type.WEB
     * @param position 位置
     * @return 频或者网址的URL
     */
    public static String getUrls(ArrayList<HashMap<String, String[]>> arrayListHash,String type, int position) {
        int len = arrayListHash.size();
        String content = null;
        for (int i = 0; i < len; i++) {
            HashMap<String, String[]> hashMap = arrayListHash.get(i);
            if (hashMap.containsKey(type) && --position == 0) {
                   content=hashMap.get(type)[2];
            }
        }
        return content;
    }

    /**
     * 获取输入流
     * @param url
     * @return
     */
    public static InputStream getInput(String url) {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL(url);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            inStream = httpConnection.getInputStream();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inStream;
    }

    /**
     *  dip 转 px
     * @param context
     * @param dpValue
     * @return 实际象素大小
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
