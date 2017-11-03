package com.android.frame.third.library.aliutils;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;
import com.android.frame.third.library.charge.IChargeListener;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by wangjian on 2017/7/13.
 */

public class AliUtils {
    public static final String ALIPAY_CHARGE_SUCCESS = "9000";
    public static final String ALIPAY_CHARGE_CANCEL = "6001";

    public static void charge(final Activity activity, final String order, final IChargeListener listener){
        if(listener == null){
            throw new RuntimeException("IChargeListener 不能为空");
        }
        listener.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask aliPay = new PayTask(new WeakReference<>(activity).get());
                final String orderInfo = order.replaceAll("\\/", "");
                Map<String, String> result = aliPay.payV2(orderInfo, true);
                PayResult payResult = new PayResult(result);
                final String statusCode = payResult.getResultStatus();
                if (ALIPAY_CHARGE_SUCCESS.equals(statusCode)) {
                    if(listener != null){
                        listener.success();
                    }
                } else if(ALIPAY_CHARGE_CANCEL.equals(statusCode)) {
                    if(listener != null){
                        listener.cancel();
                    }
                } else {
                    if(listener != null){
                        listener.error(Integer.valueOf(statusCode), payResult.getMemo());
                    }
                }
            }
        }).start();
    }
}
