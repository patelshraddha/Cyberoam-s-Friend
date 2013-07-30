package com.netsavvies.cyberoam.gui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.widget.Toast;
import android.app.Service;

import static com.netsavvies.cyberoam.backend.Methods.*;
import static com.netsavvies.cyberoam.backend.Vars.*;
import com.netsavvies.cyberoam.R;
import com.netsavvies.cyberoam.backend.Const;
import com.netsavvies.cyberoam.backend.CybService;
import com.netsavvies.cyberoam.backend.Vars;

import static com.netsavvies.cyberoam.backend.Const.*;

/*
 * Methods eg. - loginFailed(), loggedIn(), loggedOut() - called from CybService
 * These methods will handle the creation of notifications/toasts/something-similar, to inform the user
 */

public class InformGui {

	private static String loginmessage = "You are logged in by id  ";
	private static String logoutmessage = "You are logged out";
	private static String loginfailed = "Login failed due to maximum login limit or due to wrong password";

	public static Notification Notify(String subject, Context context,int icon) {

		Intent intent = new Intent(context, StatusActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);

		
		// Bitmap largeIcon =
		// BitmapFactory.decodeResource(context.getResources(),R.drawable.large);
		//Toast.makeText(context,"Notify"+getMessage(current),Toast.LENGTH_SHORT).show();
		Notification noti = new NotificationCompat.Builder(context)
				.setContentTitle("Internet Connection Status")
				.setContentText(subject).setSmallIcon(icon)
				.setContentIntent(pIntent).build();
    
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

	   
		// Do not Hide the notification after its selected
		noti.flags |= Notification.FLAG_NO_CLEAR;
		// Let the notification be ongoing
		noti.flags |= Notification.FLAG_ONGOING_EVENT;
		// Let the notification be ongoing
		noti.flags |= Notification.FLAG_FOREGROUND_SERVICE;
	     
		
		return noti;
	}

	public static void loggedIn(Context context, String id) {
		Toast logintoast = Toast.makeText(context, loginmessage + id,
				Toast.LENGTH_LONG);
		logintoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		logintoast.show();
	}

	public static void loggedOut(Context context) {
		Toast logintoast = Toast.makeText(context, logoutmessage,
				Toast.LENGTH_LONG);
		logintoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		logintoast.show();
	}

	public static void loginFailed(Context context) {
		Toast logintoast = Toast.makeText(context, loginfailed,
				Toast.LENGTH_LONG);
		logintoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		logintoast.show();
	}

	/*public static void updateGuiStatus(Const key, Boolean bool) {
		switch (key) {
		case stop:
			strings.put(current, getMessage(stop));
			icons.put(current, getIcon(stop));
			buttons.put(current, getButton(stop));
			Notify(getMessage(current), CybService.getContext());
			break;
		case wifiKaLochaAaya:
			strings.put(current, getMessage(key));
			icons.put(current, getIcon(key));
			buttons.put(current, getButton(key));
			Notify(getMessage(current), CybService.getContext());
		case wifiDisconnected:
			if (!bool) {
				strings.put(current, getMessage(wifiDisconnected));
				icons.put(current, getIcon(wifiDisconnected));
				buttons.put(current, getButton(wifiDisconnected));
				Notify(getMessage(current), CybService.getContext());
			}
			break;
		case otherWifi:
			strings.put(current, getMessage(otherWifi));
			icons.put(current, getIcon(otherWifi));
			buttons.put(current, getButton(otherWifi));
			Notify(getMessage(current), CybService.getContext());
			break;
		case str:
			if (!bool) {
				// message not able to login because not proper strength
				strings.put(current, getMessage(lowSTR));
				icons.put(current, getIcon(lowSTR));
				buttons.put(current, getButton(lowSTR));
				Notify(getMessage(current), CybService.getContext());
			}
			break;
		case c:
			if (!bool) {
				// message cyberoam not available so no net access
				strings.put(current, getMessage(noCyb));
				icons.put(current, getIcon(noCyb));
				buttons.put(current, getButton(noCyb));
				Notify(getMessage(current), CybService.getContext());
			}
			break;
		case l:
			if (bool) {
				// message logged in by id
				// button-log out
				// screen with scroll view
				strings.put(current, getMessage(loggedIn));
				icons.put(current, getIcon(loggedIn));
				buttons.put(current, getButton(loggedIn));
				Notify(getMessage(current), CybService.getContext());
			} else {
				if (get(noUser)) {
					// message no user
					// button-prefrences screen
					strings.put(current, getMessage(noUser));
					icons.put(current, getIcon(noUser));
					buttons.put(current, getButton(noUser));
					Notify(getMessage(current), CybService.getContext());
				} else {
					// message login failed
					// screen with scroll view
					strings.put(current, getMessage(noUser));
					icons.put(current, getIcon(noUser));
					buttons.put(current, getButton(noUser));
					Notify(getMessage(current), CybService.getContext());
				}
			}
			break;
		default:
			break;

		}
	}*/
	
	public static Notification updateGuiStatus(Context context,Const key) 
	{
		if(key.equals(loggedIn))
			strings.put(current,getMessage(key)+Vars.loginId);
		else
		strings.put(current, getMessage(key));
		//Toast.makeText(context,getMessage(current)+R.drawable.ic_launcher,Toast.LENGTH_SHORT).show();
		icons.put(current, getIcon(key));
		buttons.put(current, getButton(key));
		return Notify(getMessage(current),context,getIcon(current));
	}
		
	
	
	
		

	

}
