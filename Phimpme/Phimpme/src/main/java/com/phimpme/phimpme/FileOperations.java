package com.phimpme.phimpme;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by sensing on 14-6-14.
 */
public class FileOperations {

	public static final int MEDIA_TYPE_IMAGE = 1;

	public Uri fileChannelCopy(Activity activity, Uri source) {
		Uri target = CaptureActivity.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		FileInputStream fi;
		FileOutputStream fo;
		FileChannel in;
		FileChannel out;
		Cursor cursor = activity.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[]{MediaStore.Images.Media.DATA},
				MediaStore.Images.Media._ID + "=" + source.toString().substring(38),
				null, null);
		cursor.moveToFirst();
		try {
			fi = new FileInputStream(cursor.getString(0));
			fo = new FileOutputStream(new File(target.toString().substring(7)));
			in = fi.getChannel();//得到对应的文件通道
			out = fo.getChannel();//得到对应的文件通道
			in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
			fi.close();
			in.close();
			fo.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return target;
	}

}
