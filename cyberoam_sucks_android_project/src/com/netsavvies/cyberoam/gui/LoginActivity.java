package com.netsavvies.cyberoam.gui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.netsavvies.cyberoam.R;

public class LoginActivity extends Activity {

	private String url;
	private TextView display;
	private final String loginid = "200901031";
	private final String loginpassword = "divya";
	private final String loginmode = "191";
	private HttpResponse response;
	BroadcastReceiver reg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		display=(TextView)findViewById(R.id.textview);

		int percentage = 0;

	

		display.setText("" + percentage);

		//reg = new BroadcastReceiver() {

		/*	@Override
			public void onReceive(Context arg0, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
					int mWifiRssi = intent.getIntExtra(
							WifiManager.EXTRA_NEW_RSSI, -200);
					Log.d("Sucks", "" + mWifiRssi);
					int mWifiLevel = this.calculateSignalLevel(mWifiRssi);
					display.setText("" + mWifiLevel);
				}

			}

			
		};
         	
		registerReceiver(reg, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
         
		
		
	     
		
	/*	if(Control.isServiceRunning(this)==false)
		{
		Toast.makeText(this,"HERE",Toast.LENGTH_SHORT).show();
		Intent startServiceIntent = new Intent(this, com.netsavvies.cyberoam.backend.CybService.class);
        startService(startServiceIntent);
        
        Toast.makeText(this,"Service started by login activity",Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(this,"Service already running",Toast.LENGTH_SHORT).show();*/
	
			
			
			 final SharedPreferences settings = getSharedPreferences("user_details", 0);
			 SharedPreferences.Editor editor = settings.edit();
		      editor.putString("user1","200901031");
		      editor.putString("password1","divya");
		      // Commit the edits!
		      editor.commit();
		}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//unregisterReceiver(reg);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	//	registerReceiver(reg, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
