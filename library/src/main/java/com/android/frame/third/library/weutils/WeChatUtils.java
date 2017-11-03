package com.android.frame.third.library.weutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.frame.third.library.AppConstants;
import com.android.frame.third.library.charge.IChargeListener;
import com.android.frame.third.library.login.ILoginListener;
import com.android.frame.third.library.login.LoginAuthBean;
import com.android.frame.third.library.share.IShareInfo;
import com.android.frame.third.library.share.IShareListener;
import com.android.frame.third.library.utils.HttpRequest;
import com.android.frame.third.library.utils.Util;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wangjian on 2017/7/12.
 */

public class WeChatUtils{
    private static final int THUMB_SIZE = 150;
    private static ILoginListener mLoginListener;
    private static IShareListener mShareListener;
    private static IChargeListener mChargeListener;

    public static void login(Activity activity, ILoginListener listener){
        if(!isWXAvailable(activity.getApplicationContext())){
            listener.onError("请先安装微信");
            return;
        }
        mLoginListener = listener;
        mLoginListener.onStart();
        getApi(activity.getApplicationContext()).registerApp(AppConstants.WCHAT_APP_ID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        getApi(activity.getApplicationContext()).sendReq(req);
    }

    public static IWXAPI getApi(Context context){
        return WXAPIFactory.createWXAPI(context, AppConstants.WCHAT_APP_ID, false);
    }

    public static void onLoginResult(final BaseResp baseResp){
        if(baseResp.errCode == BaseResp.ErrCode.ERR_OK){//成功
            HashMap<String,String> params = new HashMap<>();
            params.put("appid", AppConstants.WCHAT_APP_ID);
            params.put("secret", AppConstants.WCHAT_APP_SECRET);
            params.put("code", ((SendAuth.Resp) baseResp).code);
            params.put("grant_type", "authorization_code");
            HttpRequest.post(AppConstants.WCHAT_APP_GET_ACCESSTOKEN, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if(mLoginListener != null){
                        mLoginListener.onError(e.getMessage());
                        mLoginListener.onEnd();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(mLoginListener != null){
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            LoginAuthBean bean = new LoginAuthBean(LoginAuthBean.LOGIN_WX);
                            bean.openId = json.getString(Constants.PARAM_OPEN_ID);
                            bean.accessToken = json.getString(Constants.PARAM_ACCESS_TOKEN);
                            bean.expire = json.getLong(Constants.PARAM_EXPIRES_IN);
                            mLoginListener.onComplete(bean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mLoginListener.onError(e.getMessage());
                        }
                        mLoginListener.onEnd();
                    }
                }
            });
        } else if(baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){//取消
            if(mLoginListener != null){
                mLoginListener.onCancel();
                mLoginListener.onEnd();
            }
        } else{//失败
            if(mLoginListener != null){
                mLoginListener.onError(baseResp.errStr);
                mLoginListener.onEnd();
            }
        }
    }

    public static void shareWX(final IShareInfo info, Activity activity, IShareListener listener){
        sharePrepare(SendMessageToWX.Req.WXSceneSession, info, activity, listener);
    }

    public static void shareWC(final IShareInfo info, Activity activity, IShareListener listener){
        sharePrepare(SendMessageToWX.Req.WXSceneTimeline, info, activity, listener);
    }

    /**
     * 微信分享只能是本地图片，如果是网络图片需要下载到本地，分享为图片类型时，可以不设置缩略图
     * */
    private static void sharePrepare(final int scene, final IShareInfo info, final Activity activity, final IShareListener listener){
        if(!isWXAvailable(activity.getApplicationContext())){
            listener.onError("请先安装微信");
            return;
        }
        mShareListener = listener;
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

    private static void share(int scene, final IShareInfo info, Activity activity, IShareListener listener){
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());//用于唯一标识一个请求
        req.scene = scene;
        final String imagePath = info.getImageUrl();
        WXMediaMessage msg = new WXMediaMessage();
        if(info.getType() == IShareInfo.TYPE_DEFAULT){
            WXWebpageObject webPage = new WXWebpageObject();
            webPage.webpageUrl = info.getTargetUrl();
            msg.title = info.getTitle();
            if(scene == SendMessageToWX.Req.WXSceneTimeline){
                msg.title = info.getSummary();//朋友圈中的title使用描述信息
            }
            msg.description = info.getSummary();
            msg.mediaObject = webPage;
        } else if(info.getType() == IShareInfo.TYPE_IMAGE){
            WXImageObject imgObj = new WXImageObject();
            imgObj.setImagePath(imagePath);
            msg.mediaObject = imgObj;
        }
        if(Util.isImageAvailable(imagePath)){
            msg.thumbData = getThumbData(info.getImageUrl());
        }
        req.message = msg;
        getApi(activity.getApplicationContext()).registerApp(AppConstants.WCHAT_APP_ID);
        getApi(activity.getApplicationContext()).sendReq(req);
        listener.onEnd();//微信点击留在微信后，不会再有成功回调，所以在调用微信后调用onEnd()方法，如果需要在成功之后做某些操作(http://blog.csdn.net/chniccs/article/details/50617354)
    }

    private static byte[] getThumbData(String imagePath){//调用此方法前先判断图片是否可用
        try {
            Bitmap tmp = Util.extractThumbNail(imagePath, THUMB_SIZE, THUMB_SIZE, true);
            Bitmap bmp = Util.compressImage(tmp, WXMediaMessage.THUMB_LENGTH_LIMIT);
            tmp.recycle();
            return Util.bmpToByteArray(bmp, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void onShareResult(final BaseResp baseResp){
        if(baseResp.errCode == BaseResp.ErrCode.ERR_OK){//成功
            if(mShareListener != null){
                mShareListener.success();
            }
        } else if(baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){//取消
            if(mShareListener != null){
                mShareListener.cancel();
            }
        } else{//失败
            if(mShareListener != null){
                mShareListener.error("分享失败");//errMsg为null
            }
        }
    }

    public static void charge(final Activity activity, final JSONObject order, final IChargeListener listener){
        if(listener == null){
            throw new RuntimeException("IChargeListener 不能为空");
        }
        if(!isWXAvailable(activity.getApplicationContext())){
            listener.onError(0, "请先安装微信");
            return;
        }
        mChargeListener = listener;
        listener.onStart();
        try {
            PayReq req = new PayReq();
            req.appId = order.getString("appid");
            req.partnerId = order.getString("partnerid");
            req.prepayId = order.getString("prepayid");
            req.packageValue = order.getString("package");
            req.nonceStr = order.getString("noncestr");
            req.timeStamp = order.getString("timestamp");
            req.sign = order.getString("sign");
            req.extData = "app data";
            getApi(activity.getApplicationContext()).registerApp(AppConstants.WCHAT_APP_ID);
            getApi(activity.getApplicationContext()).sendReq(req);
        }catch (Exception e){
            e.printStackTrace();
            if(mChargeListener != null){
                mChargeListener.error(0, e.getMessage());
            }
        }
    }

    public static void onChargeResult(final BaseResp baseResp){
        if(mChargeListener == null){
            return;
        }
        if(baseResp.errCode == BaseResp.ErrCode.ERR_OK){//成功
            mChargeListener.success();
        } else if(baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){//取消
            mChargeListener.cancel();
        } else{//失败
            mChargeListener.error(baseResp.errCode, "充值失败");//errMsg为null
        }
    }

    public static boolean isWXAvailable(Context context){
        return getApi(context).isWXAppInstalled() && getApi(context).isWXAppSupportAPI();
    }
}
