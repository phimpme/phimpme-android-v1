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
import java.io.IOException;

public class UploadActivity extends ActionBarActivity {
	private Button AndroidSharingListButton;
	private Button bluetoothButton;
	private Button drupalButton;
	private Button wordPressButton;
    private Button joomlaButton;
	private ImageView preview;
	private TextView descriptionEditText;
	private TextView nfcTextView;
	private Uri imageUri;
	private String imageDescription;
	private Switch locationSwitch;
	private Button locationButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		find_views();

		// Automatically update imageDescription with descriptionEditText
		descriptionEditText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				imageDescription = descriptionEditText.getText().toString();
			}
		});

		enable_functions_or_hide_views();
	}

	private void find_views() {
		bluetoothButton = (Button) findViewById(R.id.bluetoothButton);
		AndroidSharingListButton = (Button) findViewById(R.id.otherButton);
		drupalButton = (Button) findViewById(R.id.drupalButton);
		wordPressButton = (Button) findViewById(R.id.wordPressButton);
        joomlaButton = (Button) findViewById(R.id.joomlaButton);
		preview = (ImageView) findViewById(R.id.imageView);
		descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
		nfcTextView = (TextView) findViewById(R.id.nfcTextView);
		imageUri = (Uri) getIntent().getExtras().get("imageUri");
		locationSwitch = (Switch) findViewById(R.id.locationSwitch);
		locationButton = (Button) findViewById(R.id.locationButton);
	}

	private void enable_functions_or_hide_views() {
		if (Configuration.ENABLE_NFC) {
			if(!enable_nfc()) {
				nfcTextView.setText("NFC not supported");
			};
		} else {
			nfcTextView.setVisibility(View.GONE);
		}

		if (Configuration.ENABLE_PHOTO_LOCATION_MODIFICATION) {
			enable_location_modification();
		} else {
			locationSwitch.setVisibility(View.GONE);
			locationButton.setVisibility(View.GONE);
		}

		// TODO: Init locationSwitch (visible if GPS data is available)

		try {
			preview.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (Configuration.ENABLE_ANDROID_SHARING) {
			enable_android_sharing();
		} else {
			AndroidSharingListButton.setVisibility(View.GONE);
		}

		if (Configuration.ENABLE_BLUETOOTH) {
			enable_bluetooth();
		} else {
			bluetoothButton.setVisibility(View.GONE);
		}

		if (Configuration.ENABLE_SHARING_TO_DRUPAL) {
			enable_drupal();
		} else {
			drupalButton.setVisibility(View.GONE);
		}

        if (Configuration.ENABLE_SHARING_TO_JOOMLA) {
            enable_joomla();
        } else {
            joomlaButton.setVisibility(View.GONE);
        }

		if (Configuration.ENABLE_SHARING_TO_WORDPRESS) {
			enable_wordpress();
		} else {
			wordPressButton.setVisibility(View.GONE);
		}
	}

	private boolean enable_nfc() {
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
			return true;
		} else {
			return false;
		}
	}

	private void enable_android_sharing() {
		AndroidSharingListButton.setOnClickListener(new Button.OnClickListener() {
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
		drupalButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (imageUri != null) {
					AccountInfo drupalAccount = AccountInfo.getSavedAccountInfo(UploadActivity.this, "drupal");
					if (drupalAccount.getAccountCategory() == null) {
						AccountInfo.createAndSaveAccountInfo(UploadActivity.this, "drupal");
					} else {
						new ShareToDrupalActivity(UploadActivity.this, drupalAccount, imageUri.getPath(), imageDescription).uploadPhoto();
					}
				}
			}
		});
	}

    private void enable_joomla() {
        joomlaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    AccountInfo joomlaAccount = AccountInfo.getSavedAccountInfo(UploadActivity.this, "joomla");
                    if (joomlaAccount.getAccountCategory() == null) {
                        AccountInfo.createAndSaveAccountInfo(UploadActivity.this, "joomla");
                    } else {
                        new ShareToJoomlaActivity(joomlaAccount, imageUri.getPath()).uploadPhoto(descriptionEditText.getText().toString());
                    }
                }
            }
        });
    }

	private void enable_wordpress() {
		wordPressButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (imageUri != null) {
					AccountInfo wordpressAccount = AccountInfo.getSavedAccountInfo(UploadActivity.this, "wordPress");
					if (wordpressAccount.getAccountCategory() == null) {
						AccountInfo.createAndSaveAccountInfo(UploadActivity.this, "wordPress");
					} else {
						new ShareToWordPressActivity(UploadActivity.this, wordpressAccount, imageUri.getPath()).uploadPhoto();
					}
				}
			}
		});
	}

	private void enable_location_modification() {
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
}
