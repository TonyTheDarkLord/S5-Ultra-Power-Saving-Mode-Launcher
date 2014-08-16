package com.spacewizz.powersavelauncher;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {
	
	public static Boolean scheduledRestart = false;
	DrawerAdapter SpaceCakeAdapter;
	SharedPreferences preferences;
	GridView SpaceCakeGrid;
	RelativeLayout UltraPowerSavingLayout;
	TextView BatteryPercentageLoader;	
		  
	class Pac {
		Drawable icon;
		String name;
		String label;
	}

	Pac[] packagesmaster;
	PackageManager packagesmanagerboss;
	private OnSharedPreferenceChangeListener listener;
	public static int themeId;
	public static int gridSize;	
	static boolean appRocketLauncheable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
        setTheme(LauncherSettings.themeId);              	
		super.onCreate(savedInstanceState);	
        
		getSupportActionBar().setIcon(android.R.color.transparent);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		
		setContentView(R.layout.activity_main);
		SpaceCakeGrid = (GridView) findViewById(R.id.content);
		try{ 
			SpaceCakeGrid.setNumColumns(2);
			SpaceCakeGrid.setNumColumns(LauncherSettings.gridSize);	
		}
		catch(Exception ex){
			SpaceCakeGrid.setNumColumns(2);
		}
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
		} else {
			registerForContextMenu(SpaceCakeGrid);			
		}			
		SpaceClock dc = (SpaceClock)findViewById(R.id.clock);
		
		UltraPowerSavingLayout = (RelativeLayout) findViewById(R.id.touchhome);
		
		BatteryPercentageLoader = (TextView) findViewById(R.id.tv_percentage);
		getBatteryPercentage();
		
		packagesmanagerboss = getPackageManager();
		set_pacs();
		appRocketLauncheable = true;		

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(new PacReceiver(), filter);

		
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
	 
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      if (v.getId()==R.id.content) {
	          android.view.MenuInflater inflater = getMenuInflater();
	          inflater.inflate(R.menu.longclick_app, menu);
	      }
	}

	public boolean onContextItemSelected(android.view.MenuItem item,final int pos) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      switch(item.getItemId()) {
	         case R.id.s5launcher_uninstall_item:
	        	 try {
             		Uri packageURI = Uri.parse("package:" + packagesmaster[pos].name);
             		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);	                
             		startActivity(uninstallIntent);	        
             	} catch ( ActivityNotFoundException e ) {	                	
             	}
	            return true;
	          case R.id.s5launcher_info_item:
	        	  try {
          		    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          		    intent.setData(Uri.parse("package:" + packagesmaster[pos].name));
          		    startActivity(intent);
          		} catch ( ActivityNotFoundException e ) {
          		    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
          		    startActivity(intent);
          		}
	                return true;
	          default:
	                return super.onContextItemSelected(item);
	      }
	}	
	public void set_pacs() {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> pacsList = packagesmanagerboss.queryIntentActivities(mainIntent, 0);
		packagesmaster = new Pac[pacsList.size()];
		for (int I = 0; I < pacsList.size(); I++) {
			packagesmaster[I] = new Pac();
			packagesmaster[I].icon = pacsList.get(I).loadIcon(packagesmanagerboss);
			packagesmaster[I].name = pacsList.get(I).activityInfo.packageName;
			packagesmaster[I].label = pacsList.get(I).loadLabel(packagesmanagerboss).toString();

		}

		new SortItems().exchange_sort(packagesmaster);
		SpaceCakeAdapter = new DrawerAdapter(this, packagesmaster);
		SpaceCakeGrid.setAdapter(SpaceCakeAdapter);
		SpaceCakeGrid.setOnItemClickListener(new DrawerClickListener(this, packagesmaster,
				packagesmanagerboss));
		SpaceCakeGrid.setOnItemLongClickListener(new DrawerLongClickListener(this, packagesmaster, UltraPowerSavingLayout));
	}
	
	 private void getBatteryPercentage() {
		   
		   
		  BroadcastReceiver batteryLevel = new BroadcastReceiver() {
		    
		   public void onReceive(Context context, Intent intent) {
		    context.unregisterReceiver(this);
		    int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		    int level= -1;
		    if (currentLevel >= 0 && scale > 0) {
		     level = (currentLevel * 100) / scale;
		    }
		    BatteryPercentageLoader.setText(level + "%");
		   }
		  };
		   
		  IntentFilter batteryLevelFilter = new IntentFilter(
		    Intent.ACTION_BATTERY_CHANGED);
		  registerReceiver(batteryLevel, batteryLevelFilter);
		   
		 }
	 
	public class PacReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {

		}

	}
	
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		
		return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	 protected void onResume() {
	 super.onResume();
	 //restart the activity when preference is changed 
	 
	 if(scheduledRestart)
	 {
	 //restart the activity
	 scheduledRestart = false;
	this.finish();
	this.startActivity(new Intent(this, this.getClass())); 
	//or use the below code to restart activity
	//Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
	//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//startActivity(i);
	 }
	 }
	
	public boolean onOptionsItemSelected(MenuItem item)	{
        int itemId = item.getItemId();		
		if (itemId == R.id.PowerSaverButton) {
			// Launch Preference activity
			 Intent ppreference = new Intent(this, PowerSavingSettings.class);
			 startActivity(ppreference); 
			 return true;			
 		} else if (itemId == R.id.LauncherSettingsButton) {
 			 // Launch Preference activity
 			 Intent preference = new Intent(this, LauncherSettings.class);
 			 startActivity(preference); 
 			 return true; 		
		} else if (itemId == R.id.SettingsButton) {
		try{ 
			startActivity((new Intent("android.settings.SETTINGS")).setFlags(0x10000000));
		}
		catch(Exception ex){
			ClickedTextBox("Settings not working!");
		} 		
		}		
	return true;
	}
	
	public void MenuClickButton(View v) {
	    openOptionsMenu();
	}
    
	public void ClickedTextBox(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	} 
	
	@Override
	public void onBackPressed() {
	    // do nothing.
	}
}
