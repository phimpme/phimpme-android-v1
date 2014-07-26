package com.phimpme.phimpme;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Configuration.HOME_PAGE_WEBSITE) {
            setContentView(R.layout.activity_main_webview);
            WebView webView = (WebView) findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(Configuration.HOME_PAGE_WEBSITE_URL);
            webView.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        } else {
            setContentView(R.layout.activity_main_image);
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Configuration.HOME_PAGE_IMAGE_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_upload) {
            DialogFragment newFragment = new SelectDialogFragment();
            newFragment.show(getSupportFragmentManager(), "SelectDialogFragment");
            return true;
        }
        if (id == R.id.action_settings) {
            // TODO: Implement the settings part
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
