package com.android.frame.third.library.qutils;

import com.android.frame.third.library.login.ILoginListener;
import com.android.frame.third.library.login.LoginAuthBean;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangjian on 2017/7/10.
 */
public class LoginQQListener implements IUiListener {
    private ILoginListener mListener;

    public LoginQQListener(ILoginListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onComplete(Object o) {
        try {
            JSONObject json = (JSONObject) o;
            LoginAuthBean bean = new LoginAuthBean(LoginAuthBean.LOGIN_QQ);
            bean.openId = json.getString(Constants.PARAM_OPEN_ID);
            bean.accessToken = json.getString(Constants.PARAM_ACCESS_TOKEN);
            bean.expire = json.getLong(Constants.PARAM_EXPIRES_IN);
            mListener.onComplete(bean);
        } catch (JSONException e) {
            e.printStackTrace();
            onError(new UiError(0, "解析错误", ""));
        }
        mListener.onEnd();
    }

    @Override
    public void onError(UiError uiError) {
        mListener.onError(uiError.errorMessage);
        mListener.onEnd();
    }

    @Override
    public void onCancel() {
        mListener.onCancel();
        mListener.onEnd();
    }

}
