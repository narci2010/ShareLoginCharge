package com.android.frame.third.library.login;

import android.app.Activity;
import android.content.Intent;

import com.android.frame.third.library.BaiDuUtils;
import com.android.frame.third.library.qutils.QQUtils;
import com.android.frame.third.library.weiboutils.WeiboUtils;
import com.android.frame.third.library.weutils.WeChatUtils;

/**
 * Created by wangjian on 2017/7/10.
 */

public class LoginUtils {
    private static int mLoginType = 0;

    public static final void loginQQ(final Activity activity, final ILoginListener listener){
        mLoginType = LoginAuthBean.LOGIN_QQ;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setLoginType(LoginAuthBean.LOGIN_QQ);
        QQUtils.login(activity, listener);
    }

    public static void loginWX(final Activity activity, final ILoginListener listener){
        mLoginType = LoginAuthBean.LOGIN_WX;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setLoginType(LoginAuthBean.LOGIN_WX);
        WeChatUtils.login(activity, listener);
    }

    public static void loginWeiBo(final Activity activity, final ILoginListener listener){
        mLoginType = LoginAuthBean.LOGIN_WB;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setLoginType(LoginAuthBean.LOGIN_WB);
        WeiboUtils.login(activity, listener);
    }

    public static void loginBaidu(final Activity activity, final ILoginListener listener){
        mLoginType = LoginAuthBean.LOGIN_BD;
        if(listener == null){
            throw new RuntimeException("listener cannot be null");
        }
        listener.setLoginType(LoginAuthBean.LOGIN_BD);
        BaiDuUtils.login(activity, listener);
    }

    //用来接收Activity回调，在登录Activity中调用
    public static void onActivityResult(int requestCode, int resultCode, Intent data){
        if(mLoginType == LoginAuthBean.LOGIN_QQ){
            QQUtils.onActivityResult(requestCode, resultCode, data);
        } else if(mLoginType == LoginAuthBean.LOGIN_WB){
            WeiboUtils.onActivityResult(requestCode, resultCode, data);
        }
    }
}
