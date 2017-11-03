package com.android.frame.third;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.frame.third.library.AppConstants;
import com.android.frame.third.library.login.ILoginListener;
import com.android.frame.third.library.login.LoginAuthBean;
import com.android.frame.third.library.login.LoginUtils;
import com.android.frame.third.library.weutils.WeChatUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void share(View v){
        startActivity(new Intent(this, ShareActivity.class));
    }

    public void charge(View v){
        startActivity(new Intent(this, ChargeActivity.class));
    }
}
