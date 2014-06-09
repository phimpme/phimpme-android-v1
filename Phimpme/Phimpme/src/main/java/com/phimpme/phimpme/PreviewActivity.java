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
            Intent toUploadIntent = new Intent();
            toUploadIntent.setClass(this, UploadActivity.class);
            if (imageUri != null) {
                toUploadIntent.putExtra("imageUri", imageUri);
                startActivity(toUploadIntent);
            } else {
                Toast.makeText(this, "ImageUri is null.", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.action_manipulation) {
            Intent intent = new Intent(this, PhotoManipulationActivity.class);
            intent.putExtra("imageUri", imageUri);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
