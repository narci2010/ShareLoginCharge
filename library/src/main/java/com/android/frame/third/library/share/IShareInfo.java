package com.android.frame.third.library.share;

import com.android.frame.third.library.utils.UnProguard;
import com.tencent.connect.share.QQShare;

/**
 * Created by wangjian on 2017/7/14.
 */

public class IShareInfo implements UnProguard {
    public static final int PLATFORM_WX = 1;//微信
    public static final int PLATFORM_WC = 2;//朋友圈
    public static final int PLATFORM_QQ = 3;//q
    public static final int PLATFORM_QZ = 4;//q空间
    public static final int PLATFORM_WB = 5;//微博

    public static final int TYPE_DEFAULT = 1;//图文
    public static final int TYPE_IMAGE = 2;//图片

    private int type = TYPE_DEFAULT;
    private String targetUrl;//分享要跳转的url
    private String title;//分享的标题, QQ最长30个字符
    private String summary;//分享的消息摘要，QQ最长40个字
    /**
     * //分享图片的url或本地路径.
     * 注：qq分享纯图片时只能是本地图片地址,
     * 微信、朋友圈只能是本地图片(如果传的是网络图片，框架中会下载，存放在应用内存中，目前仅支持网络和sdcard中图片)
     * */
    private String imageUrl;
    private String appName;//默认为空，是打开手q时返回按钮的自定义展示
    private int extInt;

    /**
     * 分享类型
     * 目前有两种，TYPE_DEFAULT,TYPE_IMAGE
     * 默认为TYPE_DEFAULT
     * */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getExtInt() {
        return extInt;
    }

    public void setExtInt(int extInt) {
        this.extInt = extInt;
    }

//    /**
//     * 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
//     QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
//     QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
//     * */
//    public int getExtInt(){
//        return QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;
//    }
}
