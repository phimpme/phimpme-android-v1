package com.phimpme.phimpme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;


public class MapActivity extends ActionBarActivity {

    private GoogleMap mMap;
    private Uri imageUri;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toast.makeText(this, "Loading the map...", Toast.LENGTH_LONG).show();
        initMap();
        setThumbnails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        }
        if (id == R.id.action_normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        }
        if (id == R.id.action_hybird) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMap() {
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapActivityMap)).getMap();
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapActivityMap))
                    .getMap();
        } else if (mMap != null) {
            // The Map is verified. It is now safe to manipulate the map.
            // Animate to user location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(getCriteria(), true));
            LatLng latLng;
            if (location == null) {
                latLng = new LatLng(0.0, 0.0);
            } else {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
            CameraPosition cameraPosition = new CameraPosition.Builder().
                    target(latLng)
                    .zoom(13)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    imageUri = new FileOperations().fileChannelCopy(MapActivity.this, Uri.parse(marker.getSnippet()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent toPreviewIntent = new Intent(MapActivity.this, PreviewActivity.class);
                toPreviewIntent.putExtra("imageUri", imageUri);
                startActivity(toPreviewIntent);
                return true;
            }
        });
    }

    private void setThumbnails() {
        Cursor cursor = MapActivity.this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.LATITUDE,
                        MediaStore.Images.Media.LONGITUDE,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA},
                null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Bitmap image = BitmapFactory.decodeFile(cursor.getString(3));
            if (image != null) {
                Bitmap icon = Bitmap.createScaledBitmap(image, 75, 100, false);
                image.recycle();
                double latitude = cursor.getDouble(0);
                double longtitude = cursor.getDouble(1);
                if (!(latitude == 0.0 && longtitude == 0.0)) {
                    LatLng latLng = new LatLng(latitude, longtitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(cursor.getString(2))
                            .snippet(cursor.getString(3))
                            .icon(BitmapDescriptorFactory.fromBitmap(icon)));
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public static Criteria getCriteria() {
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setSpeedRequired(false);
        c.setCostAllowed(false);
        c.setBearingRequired(false);
        c.setAltitudeRequired(false);
        c.setPowerRequirement(Criteria.POWER_LOW);
        return c;
    }
}
