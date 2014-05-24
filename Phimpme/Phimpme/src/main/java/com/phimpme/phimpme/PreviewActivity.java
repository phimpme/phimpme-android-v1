package com.phimpme.phimpme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;

public class PreviewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ImageView preview = (ImageView) findViewById(R.id.previewActivityImageView);
        try {
            preview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                    (Uri) getIntent().getExtras().get("photoUri")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
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
            Uri uri = (Uri) getIntent().getExtras().get("photoUri");
            if (uri != null) {
                toUploadIntent.putExtra("photoUri", uri);
                startActivity(toUploadIntent);
            }else {
                Toast.makeText(this, "Uri is null.", Toast.LENGTH_LONG).show();
            }
            return true;
        }else if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
