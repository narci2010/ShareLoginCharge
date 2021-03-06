package com.android.frame.third.library;

/**
 * Created by wangjian on 2017/7/10.
 */

public class AppConstants {
    public static String QQ_APPID = "";
    public static String QQ_AUTH_SCOPE = "all";

    public static String WCHAT_APP_ID = "";
    public static String WCHAT_APP_SECRET = "";
    public static String WCHAT_APP_GET_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
//    public static String WCHAT_APP_GET_PACKAGE = "market://details?id=com.tencent.mm";

    /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static String WEIBO_APP_KEY      = "";
    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     *
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static String WEIBO_REDIRECT_URL = "";
    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static String WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    //百度相关常量
    public static String BAIDU_APP_KEY = "";

    public static void initWeChat(String appId, String appSecret){
        WCHAT_APP_ID = appId;
        WCHAT_APP_SECRET = appSecret;
    }

    public static void initQQ(String appId){
        QQ_APPID = appId;
    }

    public static void initWeibo(String appKey){
        WEIBO_APP_KEY = appKey;
    }

    public static void initBaiDu(String appKey){
        BAIDU_APP_KEY = appKey;
    }
}
