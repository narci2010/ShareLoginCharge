package com.android.frame.third;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.android.frame.third.library.login.ILoginListener;
import com.android.frame.third.library.login.LoginAuthBean;
import com.android.frame.third.library.login.LoginUtils;

/**
 * Created by wangjian on 2017/7/13.
 */

public class LoginActivity extends Activity{
    private AlertDialog mDialog;
    private ILoginListener listener = new ILoginListener() {
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
                    mDialog.cancel();
                }
            });
        }

        @Override
        public void onComplete(final LoginAuthBean auth) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, auth.accessToken, Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onError(final String error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onCancel() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDialog = new AlertDialog.Builder(this).setTitle("加载中。。。")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info).create();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LoginUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        listener = null;
    }

    public void loginWX(View v){
        LoginUtils.loginWX(this, listener);
    }

    public void loginWB(View v){
        LoginUtils.loginWeiBo(this, listener);
    }

    public void loginQQ(View v){
        LoginUtils.loginQQ(this, listener);
    }

    public void loginBD(View v){
        LoginUtils.loginBaidu(this, listener);
    }
}
