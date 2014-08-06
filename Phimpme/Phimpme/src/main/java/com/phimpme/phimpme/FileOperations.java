package com.phimpme.phimpme;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sensing on 14-6-14.
 */
public class FileOperations {

    public static final int MEDIA_TYPE_IMAGE = 1;

    public Uri fileChannelCopy(Activity activity, Uri source) throws IOException {
        Uri target = CaptureActivity.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        FileChannel inFileChannel;
        FileChannel outFileChannel;

        if (source.toString().contains("content")) {
            Cursor cursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media.DATA},
                    MediaStore.Images.Media._ID + "=" + source.toString().substring(38),
                    null, null);
            cursor.moveToFirst();
            fileInputStream = new FileInputStream(cursor.getString(0));
            cursor.close();
        } else {
            fileInputStream = new FileInputStream(source.toString());
        }
        fileOutputStream = new FileOutputStream(new File(target.toString().substring(7)));
        inFileChannel = fileInputStream.getChannel();//得到对应的文件通道
        outFileChannel = fileOutputStream.getChannel();//得到对应的文件通道
        inFileChannel.transferTo(0, inFileChannel.size(), outFileChannel);//连接两个通道，并且从in通道读取，然后写入out通道
        fileInputStream.close();
        inFileChannel.close();
        fileOutputStream.close();
        outFileChannel.close();
        return target;
    }

    public void modifyImageName(String userName, String imagePath) {
        File image = new File(imagePath);
        image.renameTo(
                new File(image.getPath()
                        + File.separator
                        + userName
                        + "_"
                        + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                ));
    }
}