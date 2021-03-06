package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.IOException;


/**
 * The code for selecting a photo from built-in Gallery.
 * The basic structure is start system activity through intent, then get the result code.
 */

public class ChooseFromLibraryActivity extends Activity {
    private static final int CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
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
                    try {
                        imageUri = new FileOperations().fileChannelCopy(ChooseFromLibraryActivity.this, imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, PreviewActivity.class);
                    intent.putExtra("imageUri", imageUri);
                    startActivity(intent);
                }
            }
            finish();
        }
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
