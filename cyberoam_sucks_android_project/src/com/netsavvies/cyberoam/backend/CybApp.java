package com.netsavvies.cyberoam.backend;

import android.app.Application;
import android.content.Intent;

public class CybApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		if (Control.isServiceRunning(this) == false) {
			Thread thd = new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					Intent startServiceIntent = new Intent(getApplicationContext(),com.netsavvies.cyberoam.backend.CybService.class);
					startService(startServiceIntent);
				}
			};
			thd.start();
			
		}	
	}
}
