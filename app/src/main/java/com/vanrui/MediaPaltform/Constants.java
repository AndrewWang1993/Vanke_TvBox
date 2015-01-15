package com.vanrui.MediaPaltform;

/**
 * Created by wxm on 2015/1/10 in Vanke
 */

/**
 * 需要用到的一些常量
 *
 * @author wangxm-wr
 * @since 2015/1/15
 */
public final class Constants {
    private Constants() {
    }

    public static final String XMLURL = "http://121.40.186.170/shipin/test1.php";

    /**
     * 百度KEY
     */
    public static class BAIDU_APIKEY {
        public static final String AK = "aQPcpGoGa43BAa3TiZw5OsVB";
        public static final String SK = "hrWz5lPSbzh92Mxz";
    }

    /**
     * XML数据类型
     */
    public static class Media_Type {
        public static final String TAG = "TAG";
        public static final String TAG_PIC_ARRAY = "TAG_PIC_ARRAY";
        public static final String MESSAGE = "Message";
        public static final String PIC = "pic";
        public static final String VIDEO = "video";
        public static final String PICTURE = "pics";
        public static final String WEB = "web";
    }

    /**
     * Fagment 类
     */
    public static class Extra {
        public static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";
    }
}
