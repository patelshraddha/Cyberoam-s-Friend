package com.netsavvies.cyberoam.backend;

/*
 * The service which performs the actual tasks. Started by Control.class/CybApp.class or by the system at boot and not directly accessible by gui
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

class CybService extends Service {

	@Override
	public	IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
