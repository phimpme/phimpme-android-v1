package com.phimpme.phimpme;

import android.os.AsyncTask;
import com.joooid.android.model.User;
import com.joooid.android.xmlrpc.JoooidRpc;
import com.joooid.android.xmlrpc.XMLRPCException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareToJoomlaActivity {
    private String imagePath;
    private AccountInfo accountInfo;

    public ShareToJoomlaActivity(AccountInfo accountInfo, String imagePath) {
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
                            ShareToJoomlaActivity.this.accountInfo.getUserName(),
                            ShareToJoomlaActivity.this.accountInfo.getPassWord())
                            .getInstance(null, User.JOOMLA_16);
            try {
                joooidRpc.uploadFile(new File(ShareToJoomlaActivity.this.imagePath), "phimpme");
                joooidRpc.newPost("2", new File(ShareToJoomlaActivity.this.imagePath).getName(), "alias", params, "fulltext", 1, 1, true, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
            } catch (XMLRPCException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
