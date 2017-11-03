package com.android.frame.third.library.share;

import android.app.Activity;
import android.content.Intent;

import com.android.frame.third.library.qutils.QQUtils;
import com.android.frame.third.library.weiboutils.WeiboUtils;
import com.android.frame.third.library.weutils.WeChatUtils;

/**
 * Created by wangjian on 2017/7/14.
 */

public class ShareUtils {
    private static int mSharePlatform;

    public static void shareQQ(IShareInfo info, Activity activity, IShareListener listener){
        mSharePlatform = IShareInfo.PLATFORM_QQ;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setPlatform(IShareInfo.PLATFORM_QQ);
        QQUtils.shareQQ(info, activity, listener);
    }

    public static void shareQZone(IShareInfo info, Activity activity, IShareListener listener){
        mSharePlatform = IShareInfo.PLATFORM_QZ;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setPlatform(IShareInfo.PLATFORM_QZ);
        QQUtils.shareQZone(info, activity, listener);
    }

    public static void shareWX(IShareInfo info, Activity activity, IShareListener listener){
        mSharePlatform = IShareInfo.PLATFORM_WX;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setPlatform(IShareInfo.PLATFORM_WX);
        WeChatUtils.shareWX(info, activity, listener);
    }

    public static void shareWC(IShareInfo info, Activity activity, IShareListener listener){
        mSharePlatform = IShareInfo.PLATFORM_WC;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setPlatform(IShareInfo.PLATFORM_WC);
        WeChatUtils.shareWC(info, activity, listener);
    }

    public static void shareWB(IShareInfo info, Activity activity, IShareListener listener){
        mSharePlatform = IShareInfo.PLATFORM_WB;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setPlatform(IShareInfo.PLATFORM_WB);
        WeiboUtils.share(info, activity, listener);
    }

    //用来接收Activity回调，在登录Activity中调用
    public static void onActivityResult(int requestCode, int resultCode, Intent data){
        if(mSharePlatform == IShareInfo.PLATFORM_QQ || mSharePlatform == IShareInfo.PLATFORM_QZ){
            QQUtils.onActivityResult(requestCode, resultCode, data);
        }
    }

    //在Activity的onNewIntent中调用
    public static void onNewIntent(Intent intent){
        WeiboUtils.onNewIntent(intent);
    }
}
