package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.drupal.Common;
import com.drupal.HttpMultipartRequest;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wordpress.android.models.MediaFile;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadProgress extends Activity {
    protected AccountInfo accountInfo;
    private ConnectivityManager mSystemService;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSystemService =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        accountInfo = (AccountInfo) getIntent().getSerializableExtra("account");
        new UploadProgressAsyncTask().execute(0);
    }

    private class UploadProgressAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            String accountCategory = accountInfo.getAccountCategory();
            if ("wordPress".equals(accountCategory)) {
                if (mSystemService.getActiveNetworkInfo() == null) {
                    return false;
                } else {
                    String userName = accountInfo.getUserName();
                    String passWord = accountInfo.getPassWord();
                    String userUrl = accountInfo.getUserUrl();
                    String imagePath = accountInfo.getImagePath();
                    XMLRPCClient client = new XMLRPCClient(userUrl, "", "");

                    //create temp file for media upload
                    String tempFileName = "wp-" + System.currentTimeMillis();
                    try {
                        UploadProgress.this.openFileOutput(tempFileName, Context.MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    }

                    MediaFile mediaFile = new MediaFile();
                    mediaFile.setFilePath(imagePath);
                    Map<String, Object> imageProperties = new HashMap<String, Object>();
                    imageProperties.put("name", new File(imagePath).getName());
                    imageProperties.put("type", "image/jpeg");
                    imageProperties.put("bits", mediaFile);
                    imageProperties.put("overwrite", true);

                    Object[] imageUploadParams = {1, userName, passWord, imageProperties};
                    Map<?, ?> imageUploadResult;
                    try {
                        imageUploadResult = (Map<?, ?>) client.callUploadFile("wp.uploadFile", imageUploadParams, UploadProgress.this.getFileStreamPath(tempFileName));
                    } catch (final XMLRPCException e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        return false;
                    }
                    assert (imageUploadResult.get("url") != null);
                    String imageuploadResultURL = imageUploadResult.get("url").toString();
                    int featuredImageID = -1;
                    try {
                        if (imageUploadResult.get("id") != null) {
                            featuredImageID = Integer.parseInt(imageUploadResult.get("id").toString());
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return false;
                    }
                    String articleUploadAlignmentCSS = "class=\"" + "alignnone" + "\" ";
                    String content = "";
                    if (imageuploadResultURL != null) {
                        content += "<a href=\""
                                + imageuploadResultURL
                                + "\"><img title=\""
                                + mediaFile.getTitle() + "\" "
                                + articleUploadAlignmentCSS
                                + "alt=\"image\" src=\""
                                + imageuploadResultURL + "\" /></a>";

                    }
                    Map<String, Object> contentStruct = new HashMap<String, Object>();
                    contentStruct.put("wp_post_format", "standard");
                    contentStruct.put("post_type", "post");
                    contentStruct.put("title", "");
                    contentStruct.put("wp_password", "");
                    contentStruct.put("description", content);
                    contentStruct.put("mt_keywords", "");
                    contentStruct.put("categories", new String[]{"phimpme mobile"});
                    contentStruct.put("mt_excerpt", "");
                    contentStruct.put("post_status", "publish");
                    if (featuredImageID != -1) {
                        contentStruct.put("wp_post_thumbnail", featuredImageID);
                    }

                    Object[] articleUploadParams = new Object[]{1, userName, passWord, contentStruct, false};
                    try {
                        client.call("metaWeblog.newPost", articleUploadParams);
                    } catch (final XMLRPCException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return true;
        }
    }
}