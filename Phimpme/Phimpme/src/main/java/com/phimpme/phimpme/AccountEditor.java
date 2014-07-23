package com.phimpme.phimpme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountEditor extends ActionBarActivity {

    private String accountCategory;
    private String tempAccountCategory;
    private EditText userName;
    private EditText passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountCategory = getIntent().getStringExtra("accountCategory");
        tempAccountCategory = "WordPress";
        userName = (EditText) findViewById(R.id.accountEditorActivityUserName);
        passWord = (EditText) findViewById(R.id.accountEditorActivityPassword);
        setSpinner();

        Button saveAccountInfo = (Button) findViewById(R.id.accountEditorActivityButton);
        saveAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountCategory.equals("null")) {
                    accountCategory = tempAccountCategory;
                }
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
                accountInfo.commit();
                finish();
            }
        });

    }

    private void setSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.accountEditorActivityAccountCategory);
        final String[] spinnerItems = new String[]{"WordPress", "Drupal", "Joomla"};
        spinner.setAdapter(
                new ArrayAdapter<String>(
                        AccountEditor.this,
                        android.R.layout.simple_spinner_item,
                        spinnerItems)
        );
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AccountInfo accountInfo = AccountInfo.getSavedAccountInfo(AccountEditor.this, spinnerItems[position]);
                tempAccountCategory = spinnerItems[position];
                if (accountInfo.getAccountCategory() != null) {
                    userName.setText(accountInfo.getUserName());
                    passWord.setText(accountInfo.getPassWord());
                } else {
                    userName.setText("Your userName");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userName.setText("Your userName");
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
