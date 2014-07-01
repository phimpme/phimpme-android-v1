package com.phimpme.phimpme;

import android.content.Context;
import android.os.AsyncTask;

import com.drupal.Common;
import com.drupal.HttpMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ShareToDrupal {
    private Context context;
    private AccountInfo accountInfo;
    private String imagePath;
    private String drupalURL;

    public ShareToDrupal(Context context, AccountInfo accountInfo, String imagePath) {
        assert (context != null);
        assert (accountInfo != null);
        assert (imagePath != null && !imagePath.isEmpty());

        this.context = context;
        this.accountInfo = accountInfo;
        this.imagePath = imagePath;
        drupalURL = Configuration.DRUPAL_ROOT_URL;
    }

    public void uploadPhoto() {
        new DrupalLoginTask().execute();
        new DrupalUploadTask().execute();
    }

    class DrupalUploadTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String sResponse = "";

            // Parameters to send through.
            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("title", "jieshuyidanghahahahaha");
            Params.put("request_type", "image_upload");

            // Perform request.
            try {
                System.out.println("Upload prepare " + drupalURL);
                sResponse = HttpMultipartRequest.execute(ShareToDrupal.this.context, drupalURL, Params, Common.SEND_COOKIE, imagePath, "image");
            } catch (IOException e) {
                System.out.println("Incorrect response from drupapp:\n" + sResponse);
            }
            return sResponse;
        }

        protected void onPostExecute(String sResponse) {
            System.out.println("Response: " + sResponse);
            try {
                JSONObject response = new JSONObject(sResponse);
                int result = (Integer) response.get("result");
                if (result == Common.SUCCESS) {
                    System.out.println("Upload success");
                } else if (result < Common.JSON_PARSE_ERROR) {
                    System.out.println("Upload error");
                }
            } catch (JSONException e) {
                System.out.println("JSON error while uploading");
                e.printStackTrace();
            }
        }
    }

    class DrupalLoginTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String sResponse = "";

            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("request_type", "authenticate");
            Params.put("drupapp_username", accountInfo.getUserName());
            Params.put("drupapp_password", accountInfo.getPassWord());
            try {
                sResponse = HttpMultipartRequest.execute(context, drupalURL, Params, Common.SAVE_COOKIE, "", "");
            } catch (IOException e) {
                System.out.println("Incorrect response from drupapp:\n" + sResponse);
            }
            return sResponse;
        }

        protected void onPostExecute(String sResponse) {
            System.out.println("Response: " + sResponse);
            try {
                JSONObject response = new JSONObject(sResponse);
                int result = (Integer) response.get("result");
                if (result == Common.SUCCESS) {
                    System.out.println("Login success");
                } else if (result < Common.JSON_PARSE_ERROR) {
                    System.out.println("Login error");
                }
            } catch (JSONException e) {
                System.out.println("JSON error while logging in");
                e.printStackTrace();
            }
        }
    }
}
