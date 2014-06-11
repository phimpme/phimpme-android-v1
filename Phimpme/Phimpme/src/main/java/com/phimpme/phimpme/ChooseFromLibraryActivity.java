package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * The code for selecting a photo from built-in Gallery.
 * The basic structure is start system activity through intent, then get the result code.
 */

public class ChooseFromLibraryActivity extends Activity {
    private static final int CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    Uri imageUri = null;
    private Intent chooseIntent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE) {
            // Get the result of "choose image"
            if (resultCode == RESULT_OK) {
                // User chose an image
                imageUri = data.getData();
                if (imageUri != null) {
                    imageUri = fileChannelCopy(imageUri, CaptureActivity.getOutputMediaFileUri(MEDIA_TYPE_IMAGE));
                    Intent intent = new Intent(this, PreviewActivity.class);
                    intent.putExtra("imageUri", imageUri);
                    startActivity(intent);
                }
            }
            finish();
        }
    }

    private Uri fileChannelCopy(Uri source, Uri target) {
        FileInputStream fi;
        FileOutputStream fo;
        FileChannel in;
        FileChannel out;
        Cursor cursor = ChooseFromLibraryActivity.this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA},
                MediaStore.Images.Media._ID + "=" + imageUri.toString().substring(38),
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Call built-in Gallery
        chooseIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(chooseIntent, CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
}
