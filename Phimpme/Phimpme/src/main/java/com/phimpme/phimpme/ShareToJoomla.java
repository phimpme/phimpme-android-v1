package com.phimpme.phimpme;

import android.os.AsyncTask;

import com.joooid.android.model.User;
import com.joooid.android.xmlrpc.JoooidRpc;
import com.joooid.android.xmlrpc.XMLRPCException;

import java.io.File;

/**
 * Created by sensing on 14-7-8.
 */
public class ShareToJoomla {

    private String imagePath;

    public void uploadPhoto(String imagePath) {
        this.imagePath = imagePath;
        new JoomlaUploadProgress().execute();
    }

    private class JoomlaUploadProgress extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            JoooidRpc joooidRpc = new JoooidRpc("http://www.yuzhiqiang.org/joomla", "test", "test").getInstance("http://www.yuzhiqiang.org/joomla", null, "test", "test", User.JOOMLA_16);
            try {
                joooidRpc.uploadFile("test", "test", new File(ShareToJoomla.this.imagePath), "phimpme");
                joooidRpc.newPost("test", "test", "2", "title", "alias", "introtext", "fulltext", 1, 1, true, "currentDate");
            } catch (XMLRPCException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
