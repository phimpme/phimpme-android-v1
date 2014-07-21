package com.phimpme.phimpme;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.joooid.android.model.User;
import com.joooid.android.xmlrpc.JoooidRpc;
import com.joooid.android.xmlrpc.XMLRPCException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareToJoomla {
    private Activity activity;
    private String imagePath; // The path of the uploading photo
    private AccountInfo accountInfo; // The user account of Joomla
    private String joomlaURL; // The URL of the target Joomla website

    public ShareToJoomla(Activity activity, AccountInfo accountInfo, String imagePath) {
        this.activity = activity;
        this.accountInfo = accountInfo;
        this.imagePath = imagePath;
        this.joomlaURL = Configuration.JOOMLA_ROOT_URL;
    }

    public void uploadPhoto(String imageDescription) {
        new JoomlaUploadProgress().execute(imageDescription);
    }

    private class JoomlaUploadProgress extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String userName = accountInfo.getUserName(), passWord = accountInfo.getPassWord();

            JoooidRpc joooidRpc = new JoooidRpc(joomlaURL, userName, passWord).
                    getInstance(joomlaURL, null, accountInfo.getUserName(), accountInfo.getPassWord(), User.JOOMLA_16);
            try {
                final File imageFile = new File(imagePath);
                joooidRpc.uploadFile(userName, passWord, imageFile, Configuration.JOOMLA_DIR);
                // TODO fulltext -> image
                try {
                    final String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String[] position = new ConvertLatlng().convertToDegreeForm(imagePath).split(";");
                    System.out.println("aaaaaaaaaaaaaaa " + currentDate);
                    joooidRpc.newPost(userName,
                            passWord,
                            Configuration.JOOMLA_CATEGORY,
                            imageFile.getName(),
                            imageFile.getName(),
                            params,
                            "{mosmap lat='" + position[0] + "'|lon='" + position[1] + "'}",
                            1, 1, true, currentDate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ShareToJoomla.this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShareToJoomla.this.activity.getApplicationContext(), "Upload succeed.", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (XMLRPCException e) {
                ShareToJoomla.this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShareToJoomla.this.activity.getApplicationContext(), "Upload failed.", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }
            return null;
        }
    }
}
