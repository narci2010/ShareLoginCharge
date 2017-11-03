package com.android.frame.third.library;

import android.app.Activity;
import android.os.Bundle;

import com.android.frame.third.library.login.ILoginListener;
import com.android.frame.third.library.login.LoginAuthBean;
import com.baidu.api.AccessTokenManager;
import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by wangjian on 2017/7/14.
 */

public class BaiDuUtils {
    private static boolean ISBaiDuForceLogin = true;
    private static boolean ISBaiDuConfirmLogin = true;

    public static void login(final Activity activity, final ILoginListener listener){
        listener.onStart();
        final Baidu baidu = new Baidu(AppConstants.BAIDU_APP_KEY, activity.getApplicationContext());
        baidu.authorize(activity, ISBaiDuForceLogin, ISBaiDuConfirmLogin, new BaiduDialog.BaiduDialogListener() {
            @Override
            public void onComplete(Bundle bundle) {
                if(listener != null){
                    final LoginAuthBean bean  = new LoginAuthBean(LoginAuthBean.LOGIN_BD);
                    AccessTokenManager atm = baidu.getAccessTokenManager();
                    bean.accessToken = atm.getAccessToken();
                    long time = getPrivateFiled(atm,"expireTime")-System.currentTimeMillis();
                    bean.expire =(int)(time/1000);
                    if (baidu != null){
                        baidu.init(activity.getApplicationContext());
                    }
                    AsyncBaiduRunner runner = new AsyncBaiduRunner(baidu);
                    runner.request(Baidu.LoggedInUser_URL, null, "POST", new AsyncBaiduRunner.RequestListener(){
                        @Override
                        public void onBaiduException(BaiduException arg0){
                            if(listener != null){
                                listener.onError(arg0.getErrorDesp());
                                listener.onEnd();
                            }
                        }
                        @Override
                        public void onComplete(String content){
                            try {
                                JSONObject jsonRoot = new JSONObject(content == null ? "" : content);
                                bean.openId =  jsonRoot.getString("uid");
                                if(listener != null){
                                    listener.onComplete(bean);
                                    listener.onEnd();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if(listener != null){
                                    listener.onError(e.getMessage());
                                    listener.onEnd();
                                }
                            }
                        }
                        @Override
                        public void onIOException(IOException arg0){
                            if(listener != null){
                                listener.onError(arg0.getMessage());
                                listener.onEnd();
                            }
                        }
                    });
                }
            }

            @Override
            public void onBaiduException(BaiduException e) {
                if(listener != null){
                    listener.onError(e.getErrorDesp());
                    listener.onEnd();
                }
            }

            @Override
            public void onError(BaiduDialogError baiduDialogError) {
                if(listener != null){
                    listener.onError(baiduDialogError.getMessage());
                    listener.onEnd();
                }
            }

            @Override
            public void onCancel() {
                if(listener != null){
                    listener.onCancel();
                    listener.onEnd();
                }
            }
        });
    }

    private static long getPrivateFiled(Object object,String attrName)
    {
        Class<?> clz = object.getClass();
        clz.getDeclaredFields();
        Field fields[] = clz.getDeclaredFields();
        String[] name = new String[fields.length];
        try {
            Field.setAccessible(fields, true);
            for (int i = 0; i < name.length; i++)
            {
                if(fields[i].getName().equals(attrName))
                {
                    return (Long)fields[i].get(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
