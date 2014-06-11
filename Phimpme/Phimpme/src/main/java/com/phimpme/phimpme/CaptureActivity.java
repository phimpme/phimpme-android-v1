package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CaptureActivity extends Activity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private Intent captureIntent;
    private Uri mediaFileUri;

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            // Get the result of photo capturing
            if (resultCode == RESULT_OK) {
                // Image captured and saved to uri specified in the Intent
                assert (mediaFileUri != null);
                Log.d(this.getLocalClassName(), "Image saved to: " + mediaFileUri);
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtra("imageUri", mediaFileUri);
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create Intent to take a picture and return control to the calling application
        captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mediaFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(captureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
}
