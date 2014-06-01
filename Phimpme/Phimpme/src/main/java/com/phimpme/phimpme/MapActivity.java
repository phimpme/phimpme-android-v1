package com.phimpme.phimpme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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

    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init_map();
    }

    private void init_map() {
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        } else if (mMap != null) {
            // The Map is verified. It is now safe to manipulate the map.
            // Animate to user location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(getCriteria(), true));
            CameraPosition cameraPosition = new CameraPosition.Builder().
                    target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(13)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            Cursor cursor = MapActivity.this.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media.LATITUDE,
                            MediaStore.Images.Media.LONGITUDE,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media._ID},
                    null, null, null
            );
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), Uri.parse("content://media/external/images/media/" + cursor.getString(3)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(image != null) {
                    Bitmap icon = Bitmap.createScaledBitmap(image, 75, 100, false);
                    image.recycle();
                    double latitude = cursor.getDouble(0);
                    double longtitude = cursor.getDouble(1);
                    if (!(latitude == 0.0 && longtitude == 0.0)) {
                        LatLng latLng = new LatLng(latitude, longtitude);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(cursor.getString(2))
                                .snippet(cursor.getString(3))
                                .icon(BitmapDescriptorFactory.fromBitmap(icon)));
                    }
                }
                cursor.moveToNext();
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent toPreviewIntent = new Intent(Intent.ACTION_EDIT, Uri.parse("content://media/external/images/media/" + marker.getSnippet()));
                startActivity(toPreviewIntent);
                return true;
            }
        });
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


    public Criteria getCriteria() {
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);          //设置查询精度
        c.setSpeedRequired(false);                      //设置是否要求速度
        c.setCostAllowed(false);                        //设置是否允许产生费用
        c.setBearingRequired(false);                    //设置是否需要得到方向
        c.setAltitudeRequired(false);                   //设置是否需要得到海拔高度
        c.setPowerRequirement(Criteria.POWER_LOW);      //设置允许的电池消耗级别
        return c;
    }
}
