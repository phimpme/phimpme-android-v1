package com.phimpme.phimpme;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;


public class SinaWeibo {
    private Bitmap image;
    private String imageDescribe;
    private Context applicationContext;

    public SinaWeibo(Bitmap image, String imageDescribe, Context applicationContext) {
        this.image = image;
        this.imageDescribe = imageDescribe;
        this.applicationContext = applicationContext;
    }

    public void uploadImageToSinaWeibo() {
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(applicationContext);
        if (mAccessToken.isSessionValid()) {
            Toast.makeText(applicationContext, "开始分享照片", Toast.LENGTH_SHORT).show();
            StatusesAPI uploadImageApi = new StatusesAPI(mAccessToken);
            uploadImageApi.upload(imageDescribe, image, "", "", new SinaWBUpdateListener());
            Toast.makeText(applicationContext, "分享照片成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(applicationContext, "分享照片失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 新浪微博更新监听
    class SinaWBUpdateListener implements RequestListener {
        public void onComplete(String response) {
            Toast.makeText(applicationContext, R.string.weibosdk_send_sucess, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }

        public void onError(final WeiboException e) {
            System.out.println(e);
            Toast.makeText(applicationContext, "weibosdk_send_failed", Toast.LENGTH_LONG).show();
        }
    }
}
