package com.android.frame.third.library.qutils;

import com.android.frame.third.library.share.IShareListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by wangjian on 2017/7/14.
 */

public class ShareQQListener implements IUiListener{
    private IShareListener mListener;

    public ShareQQListener(IShareListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onComplete(Object o) {
        if(mListener != null){
            mListener.success();
        }
    }

    @Override
    public void onError(UiError uiError) {
        if(mListener != null){
            mListener.error(uiError.errorMessage);
        }
    }

    @Override
    public void onCancel() {
        if(mListener != null){
            mListener.cancel();
        }
    }
}
