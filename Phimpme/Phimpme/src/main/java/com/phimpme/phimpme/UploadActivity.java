package com.phimpme.phimpme;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
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

import com.drupal.Common;
import com.drupal.HttpMultipartRequest;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import static com.phimpme.phimpme.Configuration.ENABLE_ANDROID_SHARING;
import static com.phimpme.phimpme.Configuration.ENABLE_BLUETOOTH;
import static com.phimpme.phimpme.Configuration.ENABLE_NFC;
import static com.phimpme.phimpme.Configuration.ENABLE_PHOTO_LOCATION_MODIFICATION;
import static com.phimpme.phimpme.Configuration.ENABLE_SHARING_TO_DRUPAL;
import static com.phimpme.phimpme.Configuration.ENABLE_SHARING_TO_WEIBO;
import static com.phimpme.phimpme.Configuration.ENABLE_SHARING_TO_WORDPRESS;

public class UploadActivity extends ActionBarActivity {

    private String ddduserName = "test";
    private String dddpassWord = "test";
    private String ddduserUrl = "http://www.yuzhiqiang.org/drupal/drupapp";


    private Button otherButton;
    private Button bluetoothButton;
    private Button sinaWeiboButton;
    private Button durpalButton;
    private Button wordPressButton;
    private ImageView preview;
    private TextView descriptionEditText;
    private TextView nfcTextView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bluetoothButton = (Button) findViewById(R.id.bluetoothButton);
        otherButton = (Button) findViewById(R.id.otherButton);
        durpalButton = (Button) findViewById(R.id.drupalButton);
        wordPressButton = (Button) findViewById(R.id.wordPressButton);
        sinaWeiboButton = (Button) findViewById(R.id.sinaWeiboButton);
        preview = (ImageView) findViewById(R.id.imageView);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        nfcTextView = (TextView) findViewById(R.id.nfcTextView);
        imageUri = (Uri) getIntent().getExtras().get("imageUri");

        // NFC
        if (ENABLE_NFC) {
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

        if (ENABLE_PHOTO_LOCATION_MODIFICATION) {
            init_location_modification();
        }

        // Initialize
        // TODO: Init locationSwitch (visible if GPS data is available)

        try {
            preview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ENABLE_ANDROID_SHARING) {
            enable_android_sharing();
        } else {
            otherButton.setVisibility(View.GONE);
        }

        if (ENABLE_BLUETOOTH) {
            enable_bluetooth();
        } else {
            bluetoothButton.setVisibility(View.GONE);
        }

        if (ENABLE_SHARING_TO_WEIBO) {
            enable_weibo();
        } else {
            sinaWeiboButton.setVisibility(View.GONE);
        }

        if (ENABLE_SHARING_TO_DRUPAL) {
            enable_drupal();
        } else {
            wordPressButton.setVisibility(View.GONE);
        }

        if (ENABLE_SHARING_TO_WORDPRESS) {
            enable_wordpress();
        } else {
            wordPressButton.setVisibility(View.GONE);
        }
    }

    private void enable_android_sharing() {
        otherButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent uploadPhotoIntent = new Intent(Intent.ACTION_SEND);
                uploadPhotoIntent.setType("image/*");
                uploadPhotoIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                uploadPhotoIntent.putExtra(Intent.EXTRA_TEXT, descriptionEditText.getText().toString());
                startActivity(Intent.createChooser(uploadPhotoIntent, "Share Image To:"));
            }
        });
    }

    private void enable_bluetooth() {
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
    }

    private void enable_drupal() {
        durpalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    new drupappLoginTask().execute();
                    new drupappUploadTask().execute();
                }
            }
        });
    }

    private void enable_wordpress() {
        wordPressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    AccountInfo wordPress = AccountInfo.getSavedAccountInfo(UploadActivity.this, "wordPress");
                    if (wordPress.getAccountCategory() == null) {
                        AccountInfo.saveAccountInfo(UploadActivity.this, "wordPress");
                    }else {
                        Bundle data = new Bundle();
                        wordPress.setImagePath(imageUri.getPath());
                        data.putSerializable("account", wordPress);
                        Intent intent = new Intent(UploadActivity.this, UploadProgress.class);
                        intent.putExtras(data);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    private void enable_weibo() {
        sinaWeiboButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (imageUri != null) {
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

    private void init_location_modification() {
        final Switch locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        final Button locationButton = (Button) findViewById(R.id.locationButton);

        locationSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                locationButton.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        locationButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(UploadActivity.this, GPSManagerActivity.class);
                intent.putExtra("imageUri", imageUri);
                startActivity(intent);
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









    /**
     * Upload task.
     */
    class drupappUploadTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            System.out.println("doInBackGround.");
            String sResponse = "";

            // Parameters to send through.
            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("title", "jieshuhahahahahahaha");
            Params.put("request_type", "image_upload");

            // Perform request.
            try {
                System.out.println("Upload prepare " + ddduserUrl);
                sResponse = HttpMultipartRequest.execute(UploadActivity.this, ddduserUrl, Params, Common.SEND_COOKIE, imageUri.getPath(), "image");
                System.out.println("1   " + sResponse);
            }
            catch (IOException e) {
                System.out.println("IOException");
            }
            System.out.println(sResponse);
            return sResponse;
        }

        protected void onPostExecute(String sResponse) {
            drupappParseResponse(sResponse);
            int result = 0;
            if (result == Common.SUCCESS) {
                System.out.println("upload success");
            }
            else if (result < Common.JSON_PARSE_ERROR) {
                System.out.println("upload error");
            }
        }
    }

    /**
     * Login task.
     */
    class drupappLoginTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String sResponse = "";

            HashMap<String, String> Params = new HashMap<String, String>();
            Params.put("request_type", "authenticate");
            Params.put("drupapp_username", ddduserName);
            Params.put("drupapp_password", dddpassWord);
            try {
                sResponse = HttpMultipartRequest.execute(UploadActivity.this, ddduserUrl, Params, Common.SAVE_COOKIE, "", "");
            }
            catch (IOException e) {
            }
            System.out.println("Login return " + sResponse);
            return sResponse;
        }

        protected void onPostExecute(String sResponse) {
            drupappParseResponse(sResponse);
            int result = 0;
            if (result == Common.SUCCESS) {
                System.out.println("login success");
            }
            else if (result < Common.JSON_PARSE_ERROR) {
                System.out.println("login error");
            }
        }
    }

    public void drupappParseResponse(String sResponse){
        System.out.println("onPostExecute.");
        // Parse response.
        try {
            JSONObject jObject = new JSONObject(sResponse);
        }
        catch (JSONException e1) {
            System.out.println("eeeeeeeeeeeeeeee JSONException");
        }
        catch (ParseException e1) {
            System.out.println("eeeeeeeeeeeeeeee ParseException");
        }
    }
}
