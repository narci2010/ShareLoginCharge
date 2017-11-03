package com.android.frame.third.library.weiboutils;

import com.android.frame.third.library.share.IShareListener;
import com.sina.weibo.sdk.share.WbShareCallback;

/**
 * Created by wangjian on 2017/7/17.
 */

public class MWbShareCallback implements WbShareCallback {
    private IShareListener mShareListener;

    public MWbShareCallback(IShareListener shareListener) {
        this.mShareListener = shareListener;
    }

    @Override
    public void onWbShareSuccess() {
        if(mShareListener != null){
            mShareListener.success();
        }
    }

    @Override
    public void onWbShareCancel() {
        if(mShareListener != null){
            mShareListener.cancel();
        }
    }

    @Override
    public void onWbShareFail() {
        if(mShareListener != null){
            mShareListener.error("分享失败");
        }
    }
}
