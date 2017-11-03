package com.android.frame.third;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.android.frame.third.library.charge.ChargeUtils;
import com.android.frame.third.library.charge.IChargeListener;
import com.android.frame.third.library.weutils.WeChatUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangjian on 2017/7/18.
 */

public class ChargeActivity extends Activity{
    private AlertDialog mDialog;

    private IChargeListener mChargeListener = new IChargeListener() {
        @Override
        public void onStart() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.show();
                }
            });
        }

        @Override
        public void onEnd() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.dismiss();
                }
            });
        }

        @Override
        public void onCancel() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChargeActivity.this, "取消", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onError(final int errCode, final String errMsg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChargeActivity.this, errMsg, Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onSuccess() {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        mDialog = new AlertDialog.Builder(this).setTitle("充值中。。。")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info).create();
    }

    public void chargeWX(View view) {
        try {
            String re = "{\"timestamp\":1500358359,\"sign\":\"02F7C7A4402C54E3815DC7566B41C569\",\"partnerid\":\"1263550001\",\"noncestr\":\"FRPUPQQYWMWKNQ8HF6XCOO7\",\"prepayid\":\"wx2017071814123964fb2b03c30032873965\",\"msgCode\":1,\"package\":\"Sign=WXPay\",\"appid\":\"wx993da66f4b191b23\"}";
            JSONObject order = new JSONObject(re);
            WeChatUtils.charge(this, order, mChargeListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void chargeZFB(View view) {
//        String ali = "partner=\"2088611205672770\"&out_trade_no=\"7ab3aa25ce394b0ba1b22a0c5940424a\"&subject=\"199353169充值6.0元\"&body=\"199353169充值6.0元\"&total_fee=\"6.0\"&notify_url=\"https%3A%2F%2Fwww.xiu8.com%2Fproxy%2Fmoney%2Frecharge%2FalipayNotify\"&service=\"mobile.securitypay.pay\"&_input_charset=\"UTF-8\"&payment_type=\"1\"&seller_id=\"2088611205672770\"&it_b_pay=\"1h\"&sign=\"KTIDCcm5NEPqT0I8ips0Qo0nFcbm3G0zin4SFvsK82RNjeE%2BHZBFOiOAA84baGbS3cR4SNhamUNI22479%2BKQ4tw3bNR1NQ1ro6OmUrElfDDeWNvkHIqgICVCDVpH%2FkCDUug0eRT%2BVRorWejWKyyVSvCPJTDe9GPU76FdKDJtB5o%3D\"&sign_type=\"RSA\"";
        String ali = "partner=\"2088611205672770\"&out_trade_no=\"04b43137953547d586dc7faaac80a1aa\"&subject=\"184193102充值6.0元\"&body=\"184193102充值6.0元\"&total_fee=\"6.0\"&notify_url=\"https%3A%2F%2Fwww.xiu8.com%2Fproxy%2Fmoney%2Frecharge%2FalipayNotify\"&service=\"mobile.securitypay.pay\"&_input_charset=\"UTF-8\"&payment_type=\"1\"&seller_id=\"2088611205672770\"&it_b_pay=\"1h\"&sign=\"CDy6YoONBlKVsnzdhevY8CfvlapBjR63lIYylHPAo3i7M5i0XUaUT6a8tLAeLXznRZiV9BwsN6KodHnc69Qlr2z6rmEHXZSdk4zIkdFViFNMRoqzctZtErdb9aisqaD%2Bk18xR98N3i0Rk6Ty38fJn7xq39uhVq2U5st2ql9N7dg%3D\"&sign_type=\"RSA\"";
        ali = ali.replaceAll("\\/", "");
        ChargeUtils.chargeAli(this, ali, mChargeListener);
    }
}