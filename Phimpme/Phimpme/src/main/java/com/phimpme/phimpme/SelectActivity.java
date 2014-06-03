package com.phimpme.phimpme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectActivity extends ActionBarActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static final int EDIT_IMAGE_ACTIVITY_REQUEST_CODE = 400;
    Uri imageUri;
    private Uri fileUri;
    private Intent captureIntent;
    private Intent chooseIntent;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
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
        switch (requestCode) {
            case CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Enter PreviewActivity
                    Toast.makeText(this, "Now in EditActivity.", Toast.LENGTH_LONG).show();
                    imageUri = data.getData();
                    if (imageUri != null) {
                        startActivityForResult(new Intent(Intent.ACTION_EDIT, imageUri), EDIT_IMAGE_ACTIVITY_REQUEST_CODE);
                    } else {
                        Toast.makeText(this, "ImageUri is null.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case EDIT_IMAGE_ACTIVITY_REQUEST_CODE: {
                // Enter PreviewActivity
                Toast.makeText(this, "Now in PreviewActivity.", Toast.LENGTH_LONG).show();
                Intent toPreviewIntent = new Intent();
                toPreviewIntent.setClass(SelectActivity.this, PreviewActivity.class);
                toPreviewIntent.putExtra("imageUri", imageUri);
                startActivity(toPreviewIntent);
                break;
            }
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to uri specified in the Intent
                    if (fileUri != null) {
                        Toast.makeText(this, "Image saved to:\n" + fileUri, Toast.LENGTH_LONG).show();
                        startActivityForResult(captureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
            default:
                Toast.makeText(this, "Sorry, you got no feedback.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Button choosePhotos = (Button) findViewById(R.id.selectActivityChoosePhotoButton);
        Button capturePhotos = (Button) findViewById(R.id.selectActivityCapturePhotoButton);

        choosePhotos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                // Call Gallery of Android
                chooseIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(chooseIntent, CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        capturePhotos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                // create Intent to take a picture and return control to the calling application
                captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(captureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
