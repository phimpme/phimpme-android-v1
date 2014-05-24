package com.phimpme.phimpme;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends ActionBarActivity {

    /*public static List getShareApps(Context context) {
        List mApps = new ArrayList();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("audio/*");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        // Show picture
        ImageView preview = (ImageView) findViewById(R.id.uploadActivityImageView);
        try {
            preview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                    (Uri) getIntent().getExtras().get("photoUri")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Call share method of Android
        Button uploadPhotos = (Button) findViewById(R.id.uploadActivityUploadButton);
        uploadPhotos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent uploadPhotoIntent = new Intent(Intent.ACTION_SEND);
                uploadPhotoIntent.setType("image/*");
                uploadPhotoIntent.putExtra(Intent.EXTRA_STREAM, (Uri) getIntent().getExtras().get("photoUri"));
                startActivity(Intent.createChooser(uploadPhotoIntent, "Share Image To:"));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upload, menu);
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
