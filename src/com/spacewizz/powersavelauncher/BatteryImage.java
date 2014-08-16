package com.spacewizz.powersavelauncher;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BatteryImage extends LinearLayout {
	
	int level;
	 public BatteryImage(final Context context, AttributeSet attrs) {
		  super(context, attrs);
		  

          final ImageView battery =new ImageView(context);

          this.addView(battery);    

          
          BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
              @Override
                //When Event is published, onReceive method is called
              public void onReceive(Context c, Intent i) {
                    //Get Battery %
                  level = i.getIntExtra("level", 0);
              	  battery.setImageLevel(level);
            	 
                  int status = i.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                  boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                        status == BatteryManager.BATTERY_STATUS_FULL;
                  if (isCharging == true){
                  	battery.setImageLevel(level);
        		    battery.setImageResource(R.drawable.stat_battery_level_charging);
                  }
                  else {
                	  battery.setImageResource(R.drawable.stat_battery_level);
                  }                                  
             
              }

          };
          
          this.setOnClickListener(new View.OnClickListener() {
				 
				@Override
				public void onClick(View v) {
	                v.getContext().startActivity((new Intent("android.intent.action.POWER_USAGE_SUMMARY")).setFlags(0x10000000));
				    try{ 
				    	   Object service  = context.getSystemService("statusbar");
				    	   Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
				    	   Method collapse = statusbarManager.getMethod("collapse");
				    	   collapse.invoke(service);
				    	 }
				    	 catch(Exception ex){           

				    	 }
		        }
		});    
          
       
          context.registerReceiver(mBatInfoReceiver, new IntentFilter(
                  Intent.ACTION_BATTERY_CHANGED));
	 }	
}
