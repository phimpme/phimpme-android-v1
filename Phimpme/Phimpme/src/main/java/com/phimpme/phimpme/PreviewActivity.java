package com.phimpme.phimpme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class PreviewActivity extends ActionBarActivity {

	private static final int EDIT_IMAGE_ACTIVITY_REQUEST_CODE = 400;
	ImageView preview;
	Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		preview = (ImageView) findViewById(R.id.previewActivityImageView);
		imageUri = (Uri) getIntent().getExtras().get("imageUri");
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			preview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
		} catch (IOException e) {
			Toast.makeText(this, "Error occurred while loading the image.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (!Configuration.ENABLE_PHOTO_MANIPULATION) {
			menu.removeItem(R.id.action_manipulation);
		}
		getMenuInflater().inflate(R.menu.preview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_upload) {
			assert (imageUri != null);
			Intent intent = new Intent();
			intent.setClass(this, UploadActivity.class);
			intent.putExtra("imageUri", imageUri);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_manipulation) {
			Intent intent = new Intent(this, PhotoManipulationActivity.class);
			intent.putExtra("imageUri", imageUri);
			intent.setType("image/*");
			startActivityForResult(intent, EDIT_IMAGE_ACTIVITY_REQUEST_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == EDIT_IMAGE_ACTIVITY_REQUEST_CODE) {
			// imageUri would be null if photo editing is cancelled
			imageUri = (Uri) data.getExtras().get("imageUri");
		}
	}
}
