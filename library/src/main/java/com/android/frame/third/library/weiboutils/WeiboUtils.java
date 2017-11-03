package com.android.frame.third.library.weiboutils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.android.frame.third.library.AppConstants;
import com.android.frame.third.library.login.ILoginListener;
import com.android.frame.third.library.login.LoginAuthBean;
import com.android.frame.third.library.share.IShareInfo;
import com.android.frame.third.library.share.IShareListener;
import com.android.frame.third.library.utils.Util;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import java.lang.ref.WeakReference;

/**
 * Created by wangjian on 2017/7/13.
 */

public class WeiboUtils {
    private static SsoHandler mSsoHandler;
    private static WbShareHandler mShareHandler;
    private static MWbShareCallback mWbShareCallback;

    public static void login(final Activity activity, final ILoginListener listener) {
        listener.onStart();
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), AppConstants.WEIBO_APP_KEY, AppConstants.WEIBO_REDIRECT_URL, AppConstants.WEIBO_SCOPE));
        if(mSsoHandler == null){
            mSsoHandler = new SsoHandler(new WeakReference<>(activity).get());
        }
        mSsoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                if (oauth2AccessToken.isSessionValid()) {
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), oauth2AccessToken);
                    if(listener != null){
                        LoginAuthBean bean = new LoginAuthBean(LoginAuthBean.LOGIN_WB);
                        bean.openId = oauth2AccessToken.getUid();
                        bean.accessToken = oauth2AccessToken.getToken();
                        bean.expire = oauth2AccessToken.getExpiresTime();
                        listener.onComplete(bean);
                        listener.onEnd();
                    }
                } else{
                    listener.onError(oauth2AccessToken.toString());
                    listener.onEnd();
                }
            }

            @Override
            public void cancel() {
                if(listener != null){
                    listener.onCancel();
                    listener.onEnd();
                }
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                if(listener != null){
                    listener.onError(wbConnectErrorMessage.getErrorMessage());
                    listener.onEnd();
                }
            }
        });
    }

    public static void share(final IShareInfo info, final Activity activity, final IShareListener listener){
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
                        shareAction(info, activity, listener);
                    }
                }).start();
            }else{
                shareAction(info, activity, listener);
            }
        }else {
            shareAction(info, activity, listener);
        }
    }

    private static void shareAction(final IShareInfo info, Activity activity, IShareListener listener){
        if(mShareHandler == null){
            WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), AppConstants.WEIBO_APP_KEY, AppConstants.WEIBO_REDIRECT_URL, AppConstants.WEIBO_SCOPE));
            mShareHandler = new WbShareHandler(new WeakReference<>(activity).get());
            mShareHandler.registerApp();
        }
        if(mWbShareCallback == null){
            mWbShareCallback = new MWbShareCallback(listener);
        }
        WeiboMultiMessage msg = new WeiboMultiMessage();
        if(info.getType() == IShareInfo.TYPE_DEFAULT){
            msg.textObject = getTextObj(info);
        } else if(info.getType() == IShareInfo.TYPE_IMAGE){
        }
        if(Util.isImageAvailable(info.getImageUrl())){
            msg.imageObject = getImageObj(info.getImageUrl());
        }
        mShareHandler.shareMessage(msg, false);
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private static TextObject getTextObj(IShareInfo info) {
        TextObject txtObject = new TextObject();
        txtObject.text = info.getSummary() + info.getTargetUrl();
        txtObject.actionUrl = info.getTargetUrl();
        return txtObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private static ImageObject getImageObj(String imgPath) {
        ImageObject imageObject = new ImageObject();
        Bitmap  bitmap = BitmapFactory.decodeFile(imgPath);
        // 设置 Bitmap 最终压缩过的缩略图大小不得超过 1M。
        imageObject.setImageObject(Util.compressImage(bitmap, 1000 * 1024));
        return imageObject;
    }

    //用来接收Activity回调，在登录Activity中调用
    public static void onActivityResult(int requestCode, int resultCode, Intent data){
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public static void onNewIntent(Intent intent){
        if (mShareHandler != null) {
            mShareHandler.doResultIntent(intent, mWbShareCallback);
        }
    }
}
