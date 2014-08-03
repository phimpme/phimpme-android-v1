package com.phimpme.phimpme;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		String key = preference.getKey();

		Intent intent = new Intent(this, AccountEditor.class);
		if (key.equals("pref_key_account_wordpress")) {
			intent.putExtra("wordpress", true);
		} else if (key.equals("pref_key_account_drupal")) {
			intent.putExtra("drupal", true);
		} else if (key.equals("pref_key_account_joomla")) {
			intent.putExtra("joomla", true);
		}
		startActivity(intent);

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}