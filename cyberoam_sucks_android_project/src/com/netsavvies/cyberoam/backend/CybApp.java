package com.netsavvies.cyberoam.backend;

import android.app.Application;
import android.content.Intent;

public class CybApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		if (Methods.isServiceRunning(this) == false) {
			Thread thd = new Thread(){
				@Override
				public void run() {
					super.run();
					Intent startServiceIntent = new Intent(getApplicationContext(),com.netsavvies.cyberoam.backend.CybService.class);
					startService(startServiceIntent);
				}
			};
			thd.start();
			
		}	
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (Methods.isServiceRunning(this) == false) {
			Thread thd = new Thread(){
				@Override
				public void run() {
					super.run();
					Intent startServiceIntent = new Intent(getApplicationContext(),com.netsavvies.cyberoam.backend.CybService.class);
					startService(startServiceIntent);
				}
			};
			thd.start();
			
		}	
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (Methods.isServiceRunning(this) == false) {
			Thread thd = new Thread(){
				@Override
				public void run() {
					super.run();
					Intent startServiceIntent = new Intent(getApplicationContext(),com.netsavvies.cyberoam.backend.CybService.class);
					startService(startServiceIntent);
				}
			};
			thd.start();
			
		}	
	}
	
	
	
}
