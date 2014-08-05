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

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.io.IOException;

public class GeographicalLocationActivity extends ActionBarActivity {
    GoogleMap mMap;
    MapView mMapView;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Configuration.ENABLE_GOOGLEMAP) {
            googleMapInit();
        } else {
            openStreetMapInit();
        }
    }

    private void googleMapInit() {
        setContentView(R.layout.activity_googlemapgpsmanager);
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

    private void openStreetMapInit() {
        setContentView(R.layout.activity_openstreetmapgpsmanager);
        mMapView = (MapView) findViewById(R.id.OSMGPSmanager);
        MapController mController = mMapView.getController();

        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);

        imageUri = (Uri) getIntent().getExtras().get("imageUri");
        try {
            String degreeGPS = new ConvertLatlng().convertToDegreeForm(imageUri.getPath());
            String[] position = degreeGPS.split(";");
            GeoPoint center = new GeoPoint(Double.parseDouble(position[0]), Double.parseDouble(position[1]));
            mController.setCenter(center);
            mController.setZoom(13);
        } catch (IOException e) {
            e.printStackTrace();
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
            Intent toUploadActivity = new Intent();

            if (Configuration.ENABLE_GOOGLEMAP) {
                CameraPosition position = mMap.getCameraPosition();
                toUploadActivity.putExtra("latitude", position.target.latitude);
                toUploadActivity.putExtra("longitude", position.target.longitude);
                try {
                    new ConvertLatlng().saveSexagesimalBack(imageUri, position.target.latitude, position.target.longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(GeographicalLocationActivity.this, position.target.latitude + " " + position.target.longitude, Toast.LENGTH_LONG).show();
            } else {
                GeoPoint geoPoint = mMapView.getMapCenter();
                double latitude = geoPoint.getLatitudeE6() / 1E6, longitude = geoPoint.getLongitudeE6() / 1E6;
                toUploadActivity.putExtra("latitude", latitude);
                toUploadActivity.putExtra("longitude", longitude);
                try {
                    new ConvertLatlng().saveSexagesimalBack(imageUri, latitude, longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(GeographicalLocationActivity.this, latitude + " " + longitude, Toast.LENGTH_LONG).show();
            }

            toUploadActivity.putExtra("imageUri", imageUri);
            toUploadActivity.setClass(GeographicalLocationActivity.this, UploadActivity.class);
            startActivity(toUploadActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
