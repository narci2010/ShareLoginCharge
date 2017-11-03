package com.android.frame.third.library.weutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by wangjian on 2017/7/12.
 */

public class WXEntryBaseActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = WeChatUtils.getApi(this);
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(final BaseReq baseReq) {
    }

    @Override
    public void onResp(final BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH){//登录
            WeChatUtils.onLoginResult(baseResp);
        } else if(baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){//分享
            WeChatUtils.onShareResult(baseResp);
        } else if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){//支付
            WeChatUtils.onChargeResult(baseResp);
        }
        this.finish();
    }
}
