package com.crossword.activity;

import java.io.File;

import com.crossword.Crossword;
import com.crossword.R;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class PeferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference btClearCache = (Preference)findPreference("clear_cache");
		btClearCache.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				clearCache();
				return true;
			}
		});
	}
	
	protected void clearCache() {
	    File directoryToScan = new File(Crossword.GRID_DIRECTORY); 
	    File files[] = directoryToScan.listFiles();
	    for (File file: files)
	    	file.delete();
		Toast.makeText(this, "clear...", Toast.LENGTH_SHORT).show();
	}

}
