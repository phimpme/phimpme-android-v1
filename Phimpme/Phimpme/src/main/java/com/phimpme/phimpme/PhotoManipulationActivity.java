package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class PhotoManipulationActivity extends Activity {

    private static final int EDIT_IMAGE_ACTIVITY_REQUEST_CODE = 400;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUri = (Uri) getIntent().getExtras().get("imageUri"); // Make sure the Extra is put in
        Intent intent = new Intent(Intent.ACTION_EDIT, imageUri);
        intent.setDataAndType(imageUri, "image/*");
        startActivityForResult(intent, EDIT_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            Intent intent = new Intent();
            intent.putExtra("imageUri", data.getData());
            setResult(EDIT_IMAGE_ACTIVITY_REQUEST_CODE, intent);
        }
        finish();
    }
}
