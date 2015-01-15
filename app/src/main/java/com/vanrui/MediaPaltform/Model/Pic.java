package com.vanrui.MediaPaltform.Model;
/**
 * Created by wangxm-wr on 2015/1/8 in Vanke.
 */

/**
 * Pic Bean
 * @author wangxm-wr
 * @since 2015/1/15
 */
public class Pic {
    String PicUrl;
    String Name;
    String Id;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "City{" +
                "PicUrl='" + PicUrl + '\'' +
                ", Name='" + Name + '\'' +
                ", Id='" + Id + '\'' +
                '}';
    }
}
