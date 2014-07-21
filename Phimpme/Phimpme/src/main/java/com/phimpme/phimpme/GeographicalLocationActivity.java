package com.phimpme.phimpme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class GeographicalLocationActivity extends ActionBarActivity {
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
            try {
                String degreeGPS = new ConvertLatlng().convertToDegreeForm(imageUri.getPath());
                String[] position = degreeGPS.split(";");
                CameraPosition cameraPosition = new CameraPosition.Builder().
                        target(new LatLng(Double.parseDouble(position[0]), Double.parseDouble(position[1])))
                        .zoom(13)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        if (id == R.id.action_ok) {
            CameraPosition position = mMap.getCameraPosition();
            Intent toUploadActivity = new Intent();
            toUploadActivity.putExtra("latitude", position.target.latitude);
            toUploadActivity.putExtra("longitude", position.target.longitude);
            try {
                new ConvertLatlng().saveSexagesimalBack(imageUri, position.target.latitude, position.target.longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(GeographicalLocationActivity.this, position.target.latitude + " " + position.target.longitude, Toast.LENGTH_LONG).show();
            toUploadActivity.putExtra("imageUri", imageUri);
            toUploadActivity.setClass(GeographicalLocationActivity.this, UploadActivity.class);
            startActivity(toUploadActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
