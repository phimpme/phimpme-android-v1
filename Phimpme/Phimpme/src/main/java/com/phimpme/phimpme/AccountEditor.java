package com.phimpme.phimpme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AccountEditor extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_editor);
        final String accountCategory = getIntent().getStringExtra("accountCategory");
        TextView textView = (TextView) findViewById(R.id.accountEditorActivityAccountCategory);
        textView.setText(accountCategory);
        Button saveAccountInfo = (Button) findViewById(R.id.accountEditorActivityButton);
        saveAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userName = (EditText) findViewById(R.id.accountEditorActivityUserName);
                EditText passWord = (EditText) findViewById(R.id.accountEditorActivityPassword);
                SharedPreferences.Editor accountInfo =
                        getSharedPreferences(getIntent().getStringExtra("accountCategory"), MODE_PRIVATE).edit();
                accountInfo.putString(accountCategory, accountCategory);
                accountInfo.putString("userName", userName.getText().toString());
                accountInfo.putString("passWord", passWord.getText().toString());
                if (accountCategory.equals("wordPress")) {
                    accountInfo.putString("userUrl", "http://www.yuzhiqiang.org/xmlrpc.php");
                } else if (accountCategory.equals("drupal")) {
                    accountInfo.putString("userUrl", "http://www.yuzhiqiang.org/drupal/drupapp");
                } else if (accountCategory.equals("joomla")) {
                    accountInfo.putString("userUrl", "http://www.yuzhiqiang.org/joomla");
                }
                accountInfo.commit();
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
        }
        return super.onOptionsItemSelected(item);
    }
}
