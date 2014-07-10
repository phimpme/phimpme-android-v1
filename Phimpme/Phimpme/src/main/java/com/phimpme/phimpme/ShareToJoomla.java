package com.phimpme.phimpme;

import android.os.AsyncTask;

import com.joooid.android.model.User;
import com.joooid.android.xmlrpc.JoooidRpc;
import com.joooid.android.xmlrpc.XMLRPCException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sensing on 14-7-8.
 */
public class ShareToJoomla {
    private String imagePath;
    private AccountInfo accountInfo;

    public ShareToJoomla(AccountInfo accountInfo, String imagePath) {
        this.accountInfo = accountInfo;
        this.imagePath = imagePath;
    }

    public void uploadPhoto(String imageDescription) {
        new JoomlaUploadProgress().execute(imageDescription);
    }

    private class JoomlaUploadProgress extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            JoooidRpc joooidRpc =
                    new JoooidRpc(Configuration.JOOMLA_ROOT_URL,
                            ShareToJoomla.this.accountInfo.getUserName(),
                            ShareToJoomla.this.accountInfo.getPassWord())
                            .getInstance(null, User.JOOMLA_16);
            try {
                joooidRpc.uploadFile(new File(ShareToJoomla.this.imagePath), "phimpme");
                joooidRpc.newPost("2", new File(ShareToJoomla.this.imagePath).getName(), "alias", params, "fulltext", 1, 1, true, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
            } catch (XMLRPCException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
