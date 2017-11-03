package com.android.frame.third.library.charge;

import android.app.Activity;

import com.android.frame.third.library.aliutils.AliUtils;
import com.android.frame.third.library.weutils.WeChatUtils;

import org.json.JSONObject;

/**
 * Created by wangjian on 2017/7/18.
 */

public class ChargeUtils {
    public static void chargeWX(final Activity activity, final JSONObject order, final IChargeListener listener){
        WeChatUtils.charge(activity, order, listener);
    }

    public static void chargeAli(final Activity activity, final String order, final IChargeListener listener){
        AliUtils.charge(activity, order, listener);
    }
}
