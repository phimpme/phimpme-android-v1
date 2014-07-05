package com.phimpme.phimpme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PhotoManipulationActivity extends Activity {

	private static final int EDIT_IMAGE_ACTIVITY_REQUEST_CODE = 400;
	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Call the built-in photo editor in Gallery
		imageUri = (Uri) getIntent().getExtras().get("imageUri");
		assert (imageUri != null);
		Intent intent = new Intent(Intent.ACTION_EDIT, imageUri);
		intent.setDataAndType(imageUri, "image/*");
		startActivityForResult(intent, EDIT_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_IMAGE_ACTIVITY_REQUEST_CODE) {
			// Get the return image from Gallery photo editor
			if (data != null && data.getData() != null) {
				imageUri = data.getData();
			}
			assert (imageUri != null);
			Intent intent = new Intent();
			intent.putExtra("imageUri", imageUri);
			setResult(EDIT_IMAGE_ACTIVITY_REQUEST_CODE, intent);
		}
		finish();
	}
}
