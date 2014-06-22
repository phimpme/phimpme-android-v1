package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import org.wordpress.android.AddCatagory;
import org.wordpress.android.models.MediaFile;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sensing on 14-6-22.
 */
public class ShareToWordPress {
    private Activity activity;
    private String userName = "yzq";
    private String passWord = "wwsjJX2";
    private String userUrl = "yuzhiqiang.org";

    public ShareToWordPress(Activity activity) {
        this.activity = activity;
    }

    public boolean uploadImage(Uri imageUri) {
        Context ctx = this.activity.getApplicationContext();

        String content = "";
        int featuredImageID = -1;
        MediaFile mediaFile = new MediaFile();
        AddCatagory add = new AddCatagory();
        String url = this.userUrl;
        add.getCategories(url, this.userName, this.passWord);

        String[] theCategories;
        theCategories = new String[1];
        theCategories[0] = "phimpme mobile";

        XMLRPCClient client = new XMLRPCClient(url, "", "");
        // create temp file for media upload
        String tempFileName = "wp-"
                + System.currentTimeMillis();
        try {
            ctx.openFileOutput(tempFileName,
                    Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File tempFile = ctx.getFileStreamPath(tempFileName);
        mediaFile.setFilePath(imageUri.getPath());

        Map<String, Object> m = new HashMap<String, Object>();
        File jpeg = new File(imageUri.getPath());
        String filename = jpeg.getName();
        m.put("name", filename);
        m.put("type", "image/jpeg");
        m.put("bits", mediaFile);
        m.put("overwrite", true);

        Object[] params1 = {1, this.userName, this.passWord, m};
        HashMap<?, ?> contentHash = new HashMap<Object, Object>();

        try {
            Object result1 = (Object) client.callUploadFile(
                    "wp.uploadFile", params1, tempFile);
            contentHash = (HashMap<?, ?>) result1;
        } catch (XMLRPCException e) {
            e.printStackTrace();
        }

        String resultURL = contentHash.get("url").toString();
        try {
            if (contentHash.get("id") != null) {
                featuredImageID = Integer.parseInt(contentHash
                        .get("id").toString());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String alignmentCSS = "class=\"" + "alignnone" + "\" ";
        if (resultURL != null) {
            content = content + "<a href=\"" + resultURL
                    + "\"><img title=\"" + mediaFile.getTitle()
                    + "\" " + alignmentCSS
                    + "alt=\"image\" src=\"" + resultURL
                    + "\" /></a>";

        }

        Map<String, Object> contentStruct = new HashMap<String, Object>();
        contentStruct.put("wp_post_format", "standard");
        contentStruct.put("post_type", "post");
        contentStruct.put("title", "");
        contentStruct.put("wp_password", "");
        contentStruct.put("description", content);
        contentStruct.put("mt_keywords", "");
        contentStruct.put("categories", theCategories);
        contentStruct.put("mt_excerpt", "");
        contentStruct.put("post_status", "publish");

        if (featuredImageID != -1)
            contentStruct.put("wp_post_thumbnail",
                    featuredImageID);

        Object[] params2;
        params2 = new Object[]{1, this.userName, this.passWord,
                contentStruct, false};
        XMLRPCClient client1 = new XMLRPCClient(this.userUrl, "", "");

        try {
            Object result3 = null;
            result3 = (Object) client1.call(
                    "metaWeblog.newPost", params2);
        } catch (final XMLRPCException e) {
            e.printStackTrace();
        }
        return true;

    }
}
