package com.netsavvies.cyberoam.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReceiversForCybService extends BroadcastReceiver {
    

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Control.startService(context,intent.getAction());
		
	}

	
}