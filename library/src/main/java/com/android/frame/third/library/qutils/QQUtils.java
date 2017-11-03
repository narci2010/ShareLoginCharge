package com.android.frame.third.library.qutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.frame.third.library.AppConstants;
import com.android.frame.third.library.login.ILoginListener;
import com.android.frame.third.library.share.IShareInfo;
import com.android.frame.third.library.share.IShareListener;
import com.android.frame.third.library.utils.Util;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;

import java.lang.ref.WeakReference;

/**
 * Created by wangjian on 2017/7/10.
 */

public class QQUtils {
    private static Tencent mTencent = null;
    private static LoginQQListener mLoginListener;
    private static ShareQQListener mShareListener;

    public static void login(Activity activity, ILoginListener listener){
        listener.onStart();
        if(mTencent == null){
            mTencent = Tencent.createInstance(AppConstants.QQ_APPID, activity.getApplicationContext());
        }
        if (!mTencent.isSessionValid()){
            if(mLoginListener == null){
                mLoginListener = new LoginQQListener(listener);
            }
            mTencent.login(activity, AppConstants.QQ_AUTH_SCOPE, mLoginListener);
        }
    }

    private static void share(int extInt, final IShareInfo info, Activity activity, IShareListener listener){
        listener.onStart();
        if(mTencent == null){
            mTencent = Tencent.createInstance(AppConstants.QQ_APPID, activity.getApplicationContext());
        }
        if(!mTencent.isSessionValid()){
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, extInt);
            if(info.getType() == IShareInfo.TYPE_DEFAULT){
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, info.getTargetUrl());
                params.putString(QQShare.SHARE_TO_QQ_TITLE, info.getTitle());

                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, info.getSummary());
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, info.getImageUrl());
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  info.getAppName());
            }else if(info.getType() == IShareInfo.TYPE_IMAGE){
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, info.getImageUrl());
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, info.getAppName());
            }

            if(mShareListener == null){
                mShareListener = new ShareQQListener(listener);
            }
            mTencent.shareToQQ(activity, params, mShareListener);
        }
    }

    public static void shareQZone(final IShareInfo info, Activity activity, IShareListener listener){
        sharePrepare(QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN, info, activity, listener);
    }

    public static void shareQQ(final IShareInfo info, Activity activity, IShareListener listener){
        sharePrepare(QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE, info, activity, listener);
    }

    private static void sharePrepare(final int scene, final IShareInfo info, final Activity activity, final IShareListener listener){
        listener.onStart();
        final String imagePath = info.getImageUrl();
        if(!TextUtils.isEmpty(imagePath)){
            //判断是否是网络图片，如果是则下载
            if(imagePath.startsWith("http")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String outPath = new WeakReference<>(activity).get().getExternalFilesDir(null) + "/share.png";
                        if(Util.getImageFromNet(imagePath, outPath)){
                            info.setImageUrl(outPath);
                        } else{
                            info.setImageUrl(null);
                        }
                        share(scene, info, activity, listener);
                    }
                }).start();
            }else{
                share(scene, info, activity, listener);
            }
        }else {
            share(scene, info, activity, listener);
        }
    }

    //用来接收Activity回调，在登录Activity中调用
    public static void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mLoginListener);
        } else if(requestCode == Constants.REQUEST_QQ_SHARE  || requestCode == Constants.REQUEST_QZONE_SHARE){
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener);
        }
    }
}
