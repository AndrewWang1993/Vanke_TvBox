
package com.vanrui.MediaPaltform.Parser;
import com.vanrui.MediaPaltform.Constants;
import com.vanrui.MediaPaltform.Model.Meg;
import com.vanrui.MediaPaltform.Model.Pic;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wangxm-wr on 2015/1/8 in Vanke.
 */

/**
 * XML 解析类
 * @author wangxm-wr
 * @since 2015/1/12
 */
public class MediaParser {

    /**
     * 初步解析XML文件 返回ArrayList
     * @param parser 网络上获取到的xmlpullparser
     * @return Object {Meg,ArrayList}
     * @exception XmlPullParserException
     */
    public static ArrayList<Object> ParseXml(XmlPullParser parser){
        ArrayList<Object> arrarys = new ArrayList<Object>();
        ArrayList<Object> content =new ArrayList<Object>();
        ArrayList<Pic> picArrary = new ArrayList<Pic>();
        Meg meg = null;
        String type;
        String videoUrl;
        String webUrl;
        String descri;
        Pic pic = null;
        String picUrl;
        String name;
        String id;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        String tagNameStart = parser.getName();
                        if (tagNameStart.equalsIgnoreCase(Constants.Media_Type.MESSAGE)) {
                            content =new ArrayList<Object>();
                            meg = new Meg();
                            parser.next();
                            parser.next();
                            type=parser.nextText();
                            parser.next();
                            parser.next();
                            videoUrl = parser.nextText();
                            parser.next();
                            parser.next();
                            webUrl = parser.nextText();
                            parser.next();
                            parser.next();
                            descri = parser.nextText();
                            meg.setType(type);
                            meg.setVideoUrl(videoUrl);
                            meg.setWebUrl(webUrl);
                            meg.setDescri(descri);
                        } else if (tagNameStart.equalsIgnoreCase(Constants.Media_Type.PICTURE)) {
                            picArrary = new ArrayList<Pic>();
                        } else if (tagNameStart.equalsIgnoreCase(Constants.Media_Type.PIC)) {
                            parser.next();
                            parser.next();
                            picUrl = parser.nextText();
                            parser.next();
                            parser.next();
                            name = parser.nextText();
                            parser.next();
                            parser.next();
                            id = parser.nextText();
                            pic = new Pic();
                            pic.setPicUrl(picUrl);
                            pic.setName(name);
                            pic.setId(id);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String tagNameEnd = parser.getName();

                        if (tagNameEnd.equalsIgnoreCase(Constants.Media_Type.PIC)) {
                            picArrary.add(pic);
                        } else if (tagNameEnd.equalsIgnoreCase(Constants.Media_Type.PICTURE)) {
                        } else if (tagNameEnd.equalsIgnoreCase(Constants.Media_Type.MESSAGE)) {
                            content.add(meg);
                            content.add(picArrary);
                            arrarys.add(content);
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arrarys;
    }

}
