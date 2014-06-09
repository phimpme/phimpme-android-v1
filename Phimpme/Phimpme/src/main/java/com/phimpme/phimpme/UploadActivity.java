package com.phimpme.phimpme;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class UploadActivity extends ActionBarActivity {

    private Switch locationSwitch;
    private Button locationButton;
    private Button otherButton;
    private Button bluetoothButton;
    private Button sinaWeiboButton;
    private ImageView preview;
    private TextView descriptionEditText;
    private TextView nfcTextView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        locationButton = (Button) findViewById(R.id.locationButton);
        bluetoothButton = (Button) findViewById(R.id.bluetoothButton);
        otherButton = (Button) findViewById(R.id.otherButton);
        sinaWeiboButton = (Button) findViewById(R.id.sinaWeiboButton);
        preview = (ImageView) findViewById(R.id.imageView);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        nfcTextView = (TextView) findViewById(R.id.nfcTextView);
        imageUri = (Uri) getIntent().getExtras().get("imageUri");

        // NFC
        if(Configuration.ENABLE_NFC) {
            nfcTextView.setVisibility(View.VISIBLE);
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (nfcAdapter != null) {
                // NFC is available on this device
                nfcAdapter.setBeamPushUris(null, this);
                nfcAdapter.setBeamPushUrisCallback(new NfcAdapter.CreateBeamUrisCallback() {
                    @Override
                    public Uri[] createBeamUris(NfcEvent event) {
                        Uri[] nfcPushUris = new Uri[1];
                        nfcPushUris[0] = imageUri;
                        return nfcPushUris;
                    }
                }, this);
            }
        } else {
            nfcTextView.setVisibility(View.GONE);
        }


        // Initialize
        // TODO: Init locationSwitch (visible if GPS data is available)

        try {
            preview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        locationSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                locationButton.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        // Set listeners
        locationButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(UploadActivity.this, GPSManagerActivity.class);
                intent.putExtra("imageUri", imageUri);
                startActivity(intent);
            }
        });

        // Call share method of Android
        otherButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent uploadPhotoIntent = new Intent(Intent.ACTION_SEND);
                uploadPhotoIntent.setType("image/*");
                uploadPhotoIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                uploadPhotoIntent.putExtra(Intent.EXTRA_TEXT, descriptionEditText.getText().toString());
                startActivity(Intent.createChooser(uploadPhotoIntent, "Share Image To:"));
            }
        });

        bluetoothButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadPhotoIntent = new Intent(Intent.ACTION_SEND);
                uploadPhotoIntent.setType("image/*");
                uploadPhotoIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                uploadPhotoIntent.putExtra(Intent.EXTRA_TEXT, descriptionEditText.getText().toString());
                uploadPhotoIntent.setPackage("com.android.bluetooth");
                startActivity(Intent.createChooser(uploadPhotoIntent, "Share Image To:"));
            }
        });

        sinaWeiboButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Uri uri = (Uri) getIntent().getExtras().get("photoUri");
                if (uri != null) {
                    Toast.makeText(UploadActivity.this, "Uploading to ShareToSinaWeibo.", Toast.LENGTH_LONG).show();
                    try {
                        new ShareToSinaWeibo(
                                MediaStore.Images.Media.getBitmap(
                                        UploadActivity.this.getContentResolver(), imageUri),
                                descriptionEditText.getText().toString(),
                                getApplicationContext()
                        ).uploadImageToSinaWeibo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(UploadActivity.this, "ImageUri is null.", Toast.LENGTH_LONG).show();
                }
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
        if (id == R.id.action_send) {
            // TODO: Add sending function
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
