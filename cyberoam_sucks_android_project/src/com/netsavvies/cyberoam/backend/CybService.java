package com.netsavvies.cyberoam.backend;

/*
 * The service which performs the actual tasks. Started by Control.class/CybApp.class or by the system at boot and not directly accessible by gui
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class CybService extends Service {

	@Override
	public	IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		 Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_SHORT).show();
		 
		/*try {
			while(true)
			{
			Thread.sleep(5000);*/
			//Control.loginCyberoam(getApplicationContext(),"201101124","shrap17121993");
		// Toast.makeText(getApplicationContext(),"first time",Toast.LENGTH_SHORT).show();
			//SystemClock.sleep(10000);
			//Toast.makeText(getApplicationContext(),"second time",Toast.LENGTH_SHORT).show();
			//Control.loginCyberoam(getApplicationContext(),"201101124","shrap17121993");
			/*}
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		 return super.onStartCommand(intent, flags, startId);
		
		
		
	}

}
