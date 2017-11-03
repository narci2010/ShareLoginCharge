package com.android.frame.third.library.utils;

import android.util.Log;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by wangjian on 2017/7/13.
 */

public class HttpRequest {

    private static final long DEFAULT_TIMEOUT = 60;

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    public static void post(String url, HashMap<String, String> params, Callback calback){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        builder.build().newCall(getPostRequest(url, params)).enqueue(calback);
    }

    /**
     * okHttp post同步请求
     * @param url  接口地址
     * @param paramsMap   请求参数
     */
    private static Request getPostRequest(String url, HashMap<String, String> paramsMap) {
        try {
            FormBody.Builder body=new FormBody.Builder();
            Set<Map.Entry<String, String>> entrySet = paramsMap.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                body.add(key,paramsMap.get(key));
            }
            Request request = new Request.Builder().url(url).post(body.build()).build();
            return request;
        } catch (Exception e) {
            Log.e("HttpRequest", e.toString());
        }
        return null;
    }
}
