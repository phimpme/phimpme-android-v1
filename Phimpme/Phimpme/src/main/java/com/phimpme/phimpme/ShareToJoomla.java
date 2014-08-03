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
            final String username = accountInfo.getUserName();
	        final String password = accountInfo.getPassWord();
            JoooidRpc joooidRpc = JoooidRpc.getInstance(joomlaURL, null, username, password, User.JOOMLA_16);
            try {
                final File imageFile = new File(imagePath);
                String res = joooidRpc.uploadFile(username, password, imageFile, Configuration.JOOMLA_DIR);
	            String imageUrl = res.replaceAll("^<value><string>(.+)</string></value>$", "$1");
                // TODO fulltext -> image
                try {
                    final String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String[] position = new ConvertLatlng().convertToDegreeForm(imagePath).split(";");
	                // TODO: Add description text to the end of below
	                String[] introduction = new String[]{"<img src=\"" + imageUrl + "\" />"};
	                String content = "";
	                if(Configuration.JOOMLA_SHOW_MAP_IN_POST)
	                    content += "{mosmap lat='" + position[0] + "'|lon='" + position[1] + "'}";
                    joooidRpc.newPost(username,
                            password,
                            Configuration.JOOMLA_CATEGORY,
                            imageFile.getName(),
                            imageFile.getName(),
                            introduction,
                            content,
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
