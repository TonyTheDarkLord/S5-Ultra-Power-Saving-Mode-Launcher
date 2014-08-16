package com.spacewizz.powersavelauncher;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class LauncherSettings extends SherlockPreferenceActivity {

	SharedPreferences preferences;
	private OnSharedPreferenceChangeListener listener;
	public static int themeId;
	public static int gridSize;	
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		addPreferencesFromResource(R.xml.launcher_settings);		
		themeId = R.style.SpaceTheme;
		gridSize = 2;
		
		preferences = getSharedPreferences("LauncherPower",Context.MODE_PRIVATE);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		 @Override
		 public void onSharedPreferenceChanged(SharedPreferences prefs,
		 String key) {
			 changeTheme();
			 changeGrid();
		 }
		 };
		
			final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("poweronoffPref");	
			checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
		    public boolean onPreferenceChange(Preference preference, Object newValue) {
		        if (newValue.toString().equals("true")) {
	                togglePowerSaver(true);
		        } else {
	                togglePowerSaver(false);
		        }
		        return true;
		    }
		});		 
		Preference about = (Preference) findPreference("abouts");
	    about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	            public boolean onPreferenceClick(Preference preference) {
	                final Dialog dialog = new Dialog(LauncherSettings.this);
	                dialog.setContentView(R.layout.maindialog);
	                dialog.setTitle("About Launcher App Developer.");
	                dialog.setCancelable(true);
	                Button button = (Button) dialog.findViewById(R.id.Button01);
	                button.setOnClickListener(new OnClickListener() {
	                @Override
	                    public void onClick(View v) {
	                	dialog.dismiss();
	                    }
	                });    
	                dialog.show();
	                return true;
	            }
	        }
	    );
	}
	
	@SuppressWarnings("deprecation")
	@Override
	 protected void onResume() {
	 super.onResume();
	 getPreferenceScreen().getSharedPreferences()
	 .registerOnSharedPreferenceChangeListener(listener);
	 }
	@SuppressWarnings("deprecation")
	@Override
	 protected void onPause() {
	 super.onPause();
	 getPreferenceScreen().getSharedPreferences()
	 .unregisterOnSharedPreferenceChangeListener(listener);
	 }

	 private void changeTheme()
	 { 
	 preferences = PreferenceManager.getDefaultSharedPreferences(this);
	 String pref_Theme = preferences.getString("pref_Theme", "");	 
	 if (pref_Theme.trim().equalsIgnoreCase("Normal"))
	 {
	 MainActivity.scheduledRestart = true;
	 themeId = R.style.SpaceTheme; 
	 }
	 else if (pref_Theme.trim().equalsIgnoreCase("Light"))
	 {
	 MainActivity.scheduledRestart = true;
	 themeId = R.style.SpaceThemeLight;
	 }
	 else if (pref_Theme.trim().equalsIgnoreCase("Samsung"))
	 {
     MainActivity.scheduledRestart = true;
	 themeId = R.style.SpaceTheme3;
	 }
	 else if (pref_Theme.trim().equalsIgnoreCase("Red"))
	 {
     MainActivity.scheduledRestart = true;
	 themeId = R.style.SpaceThemeRed;
	 }
	 else if (pref_Theme.trim().equalsIgnoreCase("Orange"))
	 {
	 MainActivity.scheduledRestart = true;
	 themeId = R.style.SpaceThemeOrange;
	 }
	 else if (pref_Theme.trim().equalsIgnoreCase("FullScreen"))
	 {
	 MainActivity.scheduledRestart = true;
	 themeId = R.style.SpaceThemeFullScreen;
	 }	 
	 }
	 	 
	 private void changeGrid()
	 { 
	 preferences = PreferenceManager.getDefaultSharedPreferences(this);
	 String pref_Grid = preferences.getString("pref_Grid", "");	 
	 if (pref_Grid.trim().equalsIgnoreCase("2"))
	 {
	 MainActivity.scheduledRestart = true;
	 gridSize = 2; 
	 }
	 else if (pref_Grid.trim().equalsIgnoreCase("1"))
	 {
	 MainActivity.scheduledRestart = true;
	 gridSize = 1; 
	 }	 
	 else if (pref_Grid.trim().equalsIgnoreCase("3"))
	 {
	 MainActivity.scheduledRestart = true;
	 gridSize = 3; 
	 }
	 }
	 
	@SuppressWarnings("unused")
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;                    
        return true;       
	}
	
    public void togglePowerSaver(boolean status) {
    	PackageManager pm = getPackageManager(); 
        if (status == true ) {
        	pm.setComponentEnabledSetting(new ComponentName(this, MainActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        	Intent startMain = new Intent(Intent.ACTION_MAIN);
        	startMain.addCategory(Intent.CATEGORY_HOME);
        	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(startMain);        	
        } else if (status == false ) {
        	pm.setComponentEnabledSetting(new ComponentName(this, MainActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        	Intent startMain = new Intent(Intent.ACTION_MAIN);
        	startMain.addCategory(Intent.CATEGORY_HOME);
        	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(startMain);        	
        }
    }	
}
