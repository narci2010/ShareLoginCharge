package com.android.frame.third.library.login;

import com.android.frame.third.library.utils.UnProguard;

/**
 * Created by wangjian on 2017/7/10.
 * 认证时返回的bean
 */

public class LoginAuthBean implements UnProguard{
    public static final int LOGIN_WX = 1;//1=微信
    public static final int LOGIN_QQ = 2;//2=QQ
    public static final int LOGIN_WB = 3;//3=微博
    public static final int LOGIN_BD = 4;//4=百度

    public int    fromType;//1=微信2=QQ 3=微博 4=百度
    public String accessToken;
    public String openId;
    public long    expire;

    public LoginAuthBean(int fromType) {
        this.fromType = fromType;
    }
}
