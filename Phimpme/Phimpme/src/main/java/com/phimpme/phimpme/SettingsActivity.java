package com.phimpme.phimpme;

import android.content.Intent;
import android.net.Uri;
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
		if(!Configuration.ENABLE_DONATE) {
			Preference preferenceDonate = findPreference("pref_key_donate");
			getPreferenceScreen().removePreference(preferenceDonate);
		}
	}

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

		if (key.equals("pref_key_account_wordpress")) {
			Intent intent = new Intent(this, AccountEditor.class);
			intent.putExtra("accountCategory", "WordPress");
			startActivity(intent);
		} else if (key.equals("pref_key_account_drupal")) {
			Intent intent = new Intent(this, AccountEditor.class);
			intent.putExtra("accountCategory", "Drupal");
			startActivity(intent);
		} else if (key.equals("pref_key_account_joomla")) {
			Intent intent = new Intent(this, AccountEditor.class);
			intent.putExtra("accountCategory", "Joomla");
			startActivity(intent);
		} else if(key.equals("pref_key_donate")) {
			Uri uri = Uri.parse(Configuration.DONATE_URL);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
