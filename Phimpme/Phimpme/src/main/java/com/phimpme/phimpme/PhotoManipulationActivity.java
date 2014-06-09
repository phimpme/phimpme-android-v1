package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PhotoManipulationActivity extends Activity {

    private static final int EDIT_IMAGE_ACTIVITY_REQUEST_CODE = 400;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUri = (Uri) getIntent().getExtras().get("imageUri"); // Make sure the Extra is put in
        Intent intent = new Intent(Intent.ACTION_EDIT, imageUri);
        startActivityForResult(intent, EDIT_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_IMAGE_ACTIVITY_REQUEST_CODE) {
            // Get the result of photo manipulation function
            finish();
        }
    }
}
