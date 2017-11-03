package com.android.frame.third;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.android.frame.third.library.share.IShareInfo;
import com.android.frame.third.library.share.IShareListener;
import com.android.frame.third.library.share.ShareUtils;

/**
 * Created by wangjian on 2017/7/13.
 */

public class ShareActivity extends Activity{
    private AlertDialog mDialog;
    private IShareInfo mShareInfo;

    private IShareListener mShareListener = new IShareListener() {
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
        public void onCancel() {
            showToast("取消分享");
        }

        @Override
        public void onError(String errMsg) {
            showToast(errMsg);
        }

        @Override
        public void onSuccess() {
            showToast("分享成功");
        }
    };

    public void showToast(final String toast){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ShareActivity.this, toast, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mDialog = new AlertDialog.Builder(this).setTitle("分享中。。。")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info).create();
        mShareInfo = new IShareInfo();
        mShareInfo.setType(IShareInfo.TYPE_IMAGE);
        mShareInfo.setTargetUrl("https://www.xiu8.com");
        mShareInfo.setTitle("秀吧直播");
        mShareInfo.setSummary("过来跟我一起玩啊");
        mShareInfo.setImageUrl("http://pic48.nipic.com/file/20140912/7487939_223919315000_2.jpg");
//        mShareInfo.setImageUrl("/sdcard/c.png");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void shareQQ(View v){
        ShareUtils.shareQQ(mShareInfo, this, mShareListener);
    }

    public void shareQZone(View v){
        ShareUtils.shareQZone(mShareInfo, this, mShareListener);
    }

    public void shareWX(View view) {
        ShareUtils.shareWX(mShareInfo, this, mShareListener);
    }

    public void shareWC(View view) {
        ShareUtils.shareWC(mShareInfo, this, mShareListener);
    }

    public void shareWB(View view) {
        ShareUtils.shareWB(mShareInfo, this, mShareListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShareUtils.onNewIntent(intent);
    }
}
