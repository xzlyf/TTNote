package com.xz.ttnote.constant;

import android.Manifest;

import com.xz.ttnote.R;


public class Local {

    //硬件信息
    public static int width;//屏幕尺寸
    public static int height;//屏幕尺寸
    public static String storage;//存储位置
    public static String cache;//缓存位置



    //待申请权限列表
    public static final String[] permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
//            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //知乎接口
    public static final String GET_DAIYL_NEWS = "https://news-at.zhihu.com/api/4/news/latest";//日报api
    public static final String GET_DAIYL_CONTENT = "https://news-at.zhihu.com/api/4/news/";//日报内容
    public static final String GET_DAIYL_DETAIL = "https://news-at.zhihu.com/api/4/story-extra/";//日报细节
    public static final String GET_CONTENT_COMMENT = "https://news-at.zhihu.com/api/4/story/";//文章评论
    public static final String GET_CONTENT_COMMENT_TAIL = "/short-comments";//文章评论_尾部

}
