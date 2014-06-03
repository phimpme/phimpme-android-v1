package com.phimpme.phimpme;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


public class GPSManagerActivity extends ActionBarActivity {
    GoogleMap mMap;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmanager);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.GPSManagerActivityMap)).getMap();

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.GPSManagerActivityMap))
                    .getMap();
        } else if (mMap != null) {
            imageUri = (Uri) getIntent().getExtras().get("imageUri");
            String _ID = imageUri.toString().substring(38);
            Cursor cursor = GPSManagerActivity.this.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media.LATITUDE,
                            MediaStore.Images.Media.LONGITUDE},
                    MediaStore.Images.Media._ID + "=" + _ID, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CameraPosition cameraPosition = new CameraPosition.Builder().
                        target(new LatLng(cursor.getDouble(0), cursor.getDouble(1)))
                        .zoom(13)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                cursor.moveToNext();
            }
            Button updateGPS = (Button) findViewById(R.id.GPSManagerActivityButton);
            updateGPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CameraPosition positon = mMap.getCameraPosition();
                    Intent toUploadActivity = new Intent();
                    toUploadActivity.putExtra("latitude", positon.target.latitude);
                    toUploadActivity.putExtra("longitude", positon.target.longitude);
                    Toast.makeText(GPSManagerActivity.this, positon.target.latitude + " " + positon.target.longitude, Toast.LENGTH_LONG).show();
                    toUploadActivity.putExtra("imageUri", imageUri);
                    toUploadActivity.setClass(GPSManagerActivity.this, UploadActivity.class);
                    startActivity(toUploadActivity);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gpsmanager, menu);
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
