package com.netsavvies.cyberoam.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ServiceReceiver extends BroadcastReceiver {
    

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent startServiceIntent = new Intent(context, CybService.class);
        context.startService(startServiceIntent);
        Toast.makeText(context,"Service started",Toast.LENGTH_SHORT).show();
	}

	
}