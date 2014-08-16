package com.spacewizz.powersavelauncher;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.provider.Settings.SettingNotFoundException;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Window;

public class PowerSavingSettings extends SherlockPreferenceActivity {
	
	boolean CheckboxPreference;
	private SeekBar seekBar;
	private int brightness;
	private ContentResolver cResolver;
	private Window window;
	final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(false);
		addPreferencesFromResource(R.xml.powersavingx);
		setContentView(R.layout.brigh_lay);
		seekBar = (SeekBar) findViewById(R.id.seekBar);	
		seekBar.setMax(255);
		float curBrightnessValue = 0;
		  try {
		   curBrightnessValue = android.provider.Settings.System.getInt(
		     getContentResolver(),
		     android.provider.Settings.System.SCREEN_BRIGHTNESS);
		  } catch (SettingNotFoundException e) {
		   e.printStackTrace();
		  }

		   int screen_brightness = (int) curBrightnessValue;
		  seekBar.setProgress(screen_brightness);
		  seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		   int progress = 0;
		   public void onProgressChanged(SeekBar seekBar, int progresValue,
		     boolean fromUser) {
		    progress = progresValue;
		   }

		    public void onStartTrackingTouch(SeekBar seekBar) {
		    // Do something here,
		    // if you want to do anything at the start of
		    // touching the seekbar
		   }

		    public void onStopTrackingTouch(SeekBar seekBar) {
		    	ContentResolver resolver= getContentResolver();
		        Uri uri = android.provider.Settings.System
		                .getUriFor("screen_brightness");
		        android.provider.Settings.System.putInt(resolver, "screen_brightness",
		        		progress);
		        resolver.notifyChange(uri, null);
		   }
		  });		   
		
		final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("wifiPref");	
		checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
	    public boolean onPreferenceChange(Preference preference, Object newValue) {
	        if (newValue.toString().equals("true")) {
                toggleWiFi(true);
	        } else {
                toggleWiFi(false);
	        }
	        return true;
	    }
	});
		
		final CheckBoxPreference btcheckboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("btPref");	
		btcheckboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
	    public boolean onPreferenceChange(Preference preference, Object newValue) {
	        if (newValue.toString().equals("true")) {
	        	startActivityForResult(new Intent(
	        		       BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
	        } else {
	        	bluetooth.disable();
	        }
	        return true;
	    }
	});				
    }
	
    public void toggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }	
}
