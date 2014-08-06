package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.drupal.Common;
import com.drupal.HttpMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ShareToDrupal {
    private Activity activity;
    private Context context;
    private AccountInfo accountInfo;
    private String imagePath;
    private String imageTitle;
    private String drupalURL;

    private boolean uploadResult;

    public ShareToDrupal(Activity activity, AccountInfo accountInfo, String imagePath, String imageTitle) {
        assert (context != null);
        assert (accountInfo != null);
        assert (imageTitle != null);
        assert (imagePath != null && !imagePath.isEmpty());

        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.accountInfo = accountInfo;
        this.imagePath = imagePath;
        this.imageTitle = imageTitle;
        drupalURL = Configuration.DRUPAL_ROOT_URL;
    }

    public void uploadPhoto() {
        uploadResult = true;
        new DrupalLoginTask().execute();
        new DrupalUploadTask().execute();
    }

    private void uploadResultToast() {
        ShareToDrupal.this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uploadResult) {
                    Toast.makeText(ShareToDrupal.this.context, "Upload succeed.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ShareToDrupal.this.context, "Upload failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class DrupalUploadTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String sResponse = "";
            new FileOperations().modifyImageName(accountInfo.getUserName(), imagePath);
            // Parameters to send through.
            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("title", imageTitle);
            Params.put("request_type", "image_upload");

            // Perform request.
            try {
                System.out.println("Upload prepare " + drupalURL);
                sResponse = HttpMultipartRequest.execute(ShareToDrupal.this.context, drupalURL, Params, Common.SEND_COOKIE, imagePath, "image");
            } catch (IOException e) {
                uploadResult = false;
                uploadResultToast();
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
                    uploadResult = false;
                    System.out.println("Upload error");
                }
            } catch (JSONException e) {
                uploadResult = false;
                System.out.println("JSON error while uploading");
                uploadResultToast();
                e.printStackTrace();
            }
            uploadResultToast();
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
                uploadResult = false;
                uploadResultToast();
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
                    uploadResult = false;
                    System.out.println("Login error");
                }
            } catch (JSONException e) {
                System.out.println("JSON error while logging in");
                uploadResult = false;
                uploadResultToast();
                e.printStackTrace();
            }
        }
    }
}
