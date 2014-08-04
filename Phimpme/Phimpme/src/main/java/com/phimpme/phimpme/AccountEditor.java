package com.phimpme.phimpme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountEditor extends ActionBarActivity {

    private String accountCategory;
    private EditText userName;
    private EditText passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountCategory = getIntent().getStringExtra("accountCategory");
        userName = (EditText) findViewById(R.id.editTextUsername);
        passWord = (EditText) findViewById(R.id.editTextPassword);

        Button saveAccountInfo = (Button) findViewById(R.id.buttonSave);
        saveAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor accountInfo =
                        getSharedPreferences(accountCategory, MODE_PRIVATE).edit();
                accountInfo.putString(accountCategory, accountCategory);
                accountInfo.putString("userName", userName.getText().toString());
                accountInfo.putString("passWord", passWord.getText().toString());
                if (accountCategory.equals("WordPress")) {
                    accountInfo.putString("userUrl", "http://www.yuzhiqiang.org/xmlrpc.php");
                } else if (accountCategory.equals("Drupal")) {
                    accountInfo.putString("userUrl", "http://www.yuzhiqiang.org/drupal/drupapp");
                } else if (accountCategory.equals("Joomla")) {
                    accountInfo.putString("userUrl", "http://www.yuzhiqiang.org/joomla");
                }
                accountInfo.apply();
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_editor, menu);
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
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
