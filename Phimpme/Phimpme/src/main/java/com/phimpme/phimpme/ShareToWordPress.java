package com.phimpme.phimpme;

import android.content.Context;
import android.os.AsyncTask;

import org.wordpress.android.models.MediaFile;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ShareToWordPress {
	protected Context context;
	protected AccountInfo accountInfo;
	protected String imagePath;

	public ShareToWordPress(Context context, AccountInfo accountInfo, String imagePath) {
		assert (context != null);
		assert (accountInfo != null);
		assert (imagePath != null && !imagePath.isEmpty());
		this.context = context;
		this.accountInfo = accountInfo;
		this.imagePath = imagePath;
	}

	public void uploadPhoto() {
		new WordPressUploadProgress().execute();
	}

	private class WordPressUploadProgress extends AsyncTask<Integer, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Integer... params) {
			String userName = accountInfo.getUserName();
			String passWord = accountInfo.getPassWord();
			String userUrl = Configuration.WORDPRESS_ROOT_URL;

			assert (userName != null && !userName.isEmpty());
			assert (passWord != null && !passWord.isEmpty());
			assert (userUrl != null && !userUrl.isEmpty());
			assert (imagePath != null && !imagePath.isEmpty());

			XMLRPCClient client = new XMLRPCClient(userUrl, "", "");

			//create temp file for media upload
			String tempFileName = "wp-" + System.currentTimeMillis();
			try {
				ShareToWordPress.this.context.openFileOutput(tempFileName, Context.MODE_PRIVATE);
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
				imageUploadResult = (Map<?, ?>) client.callUploadFile("wp.uploadFile", imageUploadParams, ShareToWordPress.this.context.getFileStreamPath(tempFileName));
			} catch (final XMLRPCException e) {
				e.printStackTrace();
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
			return true;
		}
	}
}
