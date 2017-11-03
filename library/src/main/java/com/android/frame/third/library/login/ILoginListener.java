package com.android.frame.third.library.login;

/**
 * Created by wangjian on 2017/7/10.
 */

public abstract class ILoginListener {
    private int mLoginType;
    public static final int ERR_CODE_UNINSTALL = 1;
    public static final int ERR_CODE_CANCEL = 2;
    public abstract void onStart();//开始登录,用于取消本地提示
    public abstract void onEnd();//登录结束，用于取消本地提示,此方法要在执行完其他方法后调用
    public abstract void onComplete(LoginAuthBean auth);//登录成功
    public abstract void onError(String error);//登录失败
    public abstract void onCancel();//取消登录

    public int getLoginType(){//获取登录平台
        return mLoginType;
    }

    public void setLoginType(int loginType){
        this.mLoginType = loginType;
    }
}
