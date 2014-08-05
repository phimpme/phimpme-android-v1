package com.phimpme.phimpme;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenStreetMapActivity extends ActionBarActivity {

    private MapView mMapView;
    private Uri imageUri;
    private DefaultResourceProxyImpl mResourceProxy = new DefaultResourceProxyImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_street_map);
        init_mapView();
        set_thumbnails();
    }

    private void init_mapView() {
        mMapView = (MapView) findViewById(R.id.myOSMmapview);
        MapController mController = mMapView.getController();

        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);

        LatLng latLng = getCurrentLocation();
        GeoPoint center = new GeoPoint(latLng.latitude, latLng.longitude);
        mController.setCenter(center);
        mController.setZoom(13);
    }

    public LatLng getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(
                locationManager.getBestProvider(MapActivity.getCriteria(), true));
        if (location == null) {
            return new LatLng(0.0, 0.0);
        } else {
            return new LatLng(location.getLatitude(), location.getLongitude());
        }
    }

    private void set_thumbnails() {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        final ArrayList<String> paths = new ArrayList<String>();

        Cursor cursor = OpenStreetMapActivity.this.getContentResolver().query(
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
                    OverlayItem item = new OverlayItem(cursor.getString(2), null,
                            new GeoPoint(latitude, longtitude));
                    item.setMarker(new BitmapDrawable(icon));
                    items.add(item);
                    paths.add(cursor.getString(3));
                }
            }
            cursor.moveToNext();
        }
        cursor.close();

        ItemizedOverlayWithFocus mLocationOverlay = new ItemizedOverlayWithFocus<OverlayItem>(
                items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index,
                                                     final OverlayItem item) {
                        try {
                            imageUri = new FileOperations().
                                    fileChannelCopy(OpenStreetMapActivity.this, Uri.parse(paths.get(index)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent toPreviewIntent = new Intent(OpenStreetMapActivity.this, PreviewActivity.class);
                        toPreviewIntent.putExtra("imageUri", imageUri);
                        startActivity(toPreviewIntent);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index,
                                                   final OverlayItem item) {
                        return false;
                    }
                }, mResourceProxy
        );
        mLocationOverlay.setFocusItemsOnTap(true);
        mLocationOverlay.setFocusedItem(0);
        mMapView.getOverlays().add(mLocationOverlay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.open_street_map, menu);
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
