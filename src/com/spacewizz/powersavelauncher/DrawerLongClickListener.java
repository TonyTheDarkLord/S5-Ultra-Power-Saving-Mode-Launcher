package com.spacewizz.powersavelauncher;

import com.actionbarsherlock.view.MenuItem;

import android.app.ActionBar.LayoutParams;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DrawerLongClickListener implements OnItemLongClickListener {

	Context mContext;
	private PopupMenu mPopup;
	MainActivity.Pac[] pacsForAdapter;
	RelativeLayout homeViewForAdapter;

	public DrawerLongClickListener(Context ctxt, MainActivity.Pac[] pacs, RelativeLayout TouchwizHome) {
		mContext = ctxt;
		pacsForAdapter = pacs;
		homeViewForAdapter = TouchwizHome;

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long arg3) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
		final PopupMenu popup =
	            new PopupMenu(mContext, arg0 == null ? arg1 : arg0);
	        mPopup = popup;
	        popup.getMenuInflater().inflate(R.menu.longclick_app, popup.getMenu());
	        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	            public boolean onMenuItemClick(android.view.MenuItem item) {
	                if (item.getItemId() == R.id.s5launcher_uninstall_item) {
	                	try {
	                		Uri packageURI = Uri.parse("package:" + pacsForAdapter[pos].name);
	                		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);	                
	                		mContext.startActivity(uninstallIntent);	        
	                	} catch ( ActivityNotFoundException e ) {	                	
	                	}
	                } else if (item.getItemId() == R.id.s5launcher_info_item) {
	            		try {
	            		    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
	            		    intent.setData(Uri.parse("package:" + pacsForAdapter[pos].name));
	            		    mContext.startActivity(intent);
	            		} catch ( ActivityNotFoundException e ) {
	            		    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
	            		    mContext.startActivity(intent);

	            		}
	            		return true;
	                } else {
	                    return false;
	                }
	                return true;
	            }
	        });
	        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
	            public void onDismiss(PopupMenu menu) {
	                mPopup = null;
	            }
	        });
	        popup.show();
			return true;
		} else {
			return false;
		}			
	}

}
