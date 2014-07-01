package com.phimpme.phimpme;

import android.content.Context;
import android.os.AsyncTask;

import com.drupal.Common;
import com.drupal.HttpMultipartRequest;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sensing on 14-7-1.
 */
public class ShareToDrupal {

    protected AccountInfo accountInfo;
    protected Context context;

    public ShareToDrupal(Context context, AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        this.context = context;
    }

    public void uploadPhoto() {
        new DrupalLoginTask().execute();
        new DrupalUploadTask().execute();
    }

    /**
     * Upload task.
     */
    class DrupalUploadTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            System.out.println("doInBackGround.");
            String sResponse = "";

            // Parameters to send through.
            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("title", "jieshuyidanghahahahaha");
            Params.put("request_type", "image_upload");

            // Perform request.
            try {
                System.out.println("Upload prepare " + ShareToDrupal.this.accountInfo.getUserUrl());
                sResponse = HttpMultipartRequest.execute(ShareToDrupal.this.context, ShareToDrupal.this.accountInfo.getUserUrl(), Params, Common.SEND_COOKIE, ShareToDrupal.this.accountInfo.getImagePath(), "image");
                System.out.println("1   " + sResponse);
            }
            catch (IOException e) {
                System.out.println("IOException");
            }
            System.out.println(sResponse);
            return sResponse;
        }

        protected void onPostExecute(String sResponse) {
            drupappParseResponse(sResponse);
            int result = 0;
            if (result == Common.SUCCESS) {
                System.out.println("upload success");
            }
            else if (result < Common.JSON_PARSE_ERROR) {
                System.out.println("upload error");
            }
        }
    }

    /**
     * Login task.
     */
    class DrupalLoginTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String sResponse = "";

            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("request_type", "authenticate");
            Params.put("drupapp_username", ShareToDrupal.this.accountInfo.getUserName());
            Params.put("drupapp_password", ShareToDrupal.this.accountInfo.getPassWord());
            try {
                sResponse = HttpMultipartRequest.execute(ShareToDrupal.this.context, ShareToDrupal.this.accountInfo.getUserUrl(), Params, Common.SAVE_COOKIE, "", "");
            }
            catch (IOException e) {
            }
            System.out.println("Login return " + sResponse);
            return sResponse;
        }

        protected void onPostExecute(String sResponse) {
            drupappParseResponse(sResponse);
            int result = 0;
            if (result == Common.SUCCESS) {
                System.out.println("login success");
            }
            else if (result < Common.JSON_PARSE_ERROR) {
                System.out.println("login error");
            }
        }
    }

    public void drupappParseResponse(String sResponse){
        System.out.println("onPostExecute.");
        // Parse response.
        try {
            JSONObject jObject = new JSONObject(sResponse);
        }
        catch (JSONException e1) {
            System.out.println("eeeeeeeeeeeeeeee JSONException");
        }
        catch (ParseException e1) {
            System.out.println("eeeeeeeeeeeeeeee ParseException");
        }
    }
}
