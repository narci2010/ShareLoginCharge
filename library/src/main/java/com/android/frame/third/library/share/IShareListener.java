package com.android.frame.third.library.share;

/**
 * Created by wangjian on 2017/7/14.
 */

public abstract class IShareListener {
    private int platform;

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public abstract void onStart();//分享开始
    public abstract void onEnd();//分享结束，与成功或失败无关
    public abstract void onCancel();
    public abstract void onError(String errMsg);
    public abstract void onSuccess();

    public final void cancel(){
        onCancel();
        onEnd();
    }
    public final void error(String errMsg){
        onError(errMsg);
        onEnd();
    }
    public final void success(){
        onSuccess();
        onEnd();
    }
}
