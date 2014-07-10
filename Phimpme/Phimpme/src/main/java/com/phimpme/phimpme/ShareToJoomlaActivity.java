package com.phimpme.phimpme;

import android.os.AsyncTask;

import com.joooid.android.model.User;
import com.joooid.android.xmlrpc.JoooidRpc;
import com.joooid.android.xmlrpc.XMLRPCException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareToJoomlaActivity {
	private String imagePath; // The path of the uploading photo
	private AccountInfo accountInfo; // The user account of Joomla
	private String joomlaURL; // The URL of the target Joomla website

	public ShareToJoomlaActivity(AccountInfo accountInfo, String imagePath) {
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
			JoooidRpc joooidRpc = new JoooidRpc(joomlaURL, accountInfo.getUserName(), accountInfo.getPassWord()).getInstance(null, User.JOOMLA_16);
			try {
				final File imageFile = new File(imagePath);
				joooidRpc.uploadFile(imageFile, "phimpme");
				final String currentDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
				joooidRpc.newPost("2", imageFile.getName(), "alias", params, "fulltext", 1, 1, true, currentDate);
			} catch (XMLRPCException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
