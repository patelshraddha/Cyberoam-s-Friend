package com.netsavvies.cyberoam.gui;

import com.netsavvies.cyberoam.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.widget.Toast;

/*
 * Methods eg. - loginFailed(), loggedIn(), loggedOut() - called from CybService
 * These methods will handle the creation of notifications/toasts/something-similar, to inform the user
 */

public class InformGui {
	
	private static String loginmessage="You are logged in by id  ";
	private static String logoutmessage="You are logged out";
	private static String loginfailed="Login failed due to maximum login limit or due to wrong password";
	
	
	public static void Notify(String subject, Context context) {

		Intent intent = new Intent(context, LoginActivity.class);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);
		
		//Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.large);

		Notification noti = new NotificationCompat.Builder(context)
				.setContentTitle("Internet Connection Status")
				.setContentText(subject)
		       // .setLargeIcon(largeIcon)
				.setWhen(0)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent).build();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Do not Hide the notification after its selected
		noti.flags |= Notification.FLAG_NO_CLEAR;
		// Let the notification be ongoing
		noti.flags |= Notification.FLAG_ONGOING_EVENT;
		// Let the notification be ongoing
		noti.flags |= Notification.FLAG_FOREGROUND_SERVICE;
		notificationManager.notify(0, noti);
	}

	public static void loggedIn(Context context,String id)
	{
		Toast logintoast=Toast.makeText(context,loginmessage+id,Toast.LENGTH_LONG);
		logintoast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
		logintoast.show();
	}
	public static void loggedOut(Context context)
	{
		Toast logintoast=Toast.makeText(context,logoutmessage,Toast.LENGTH_LONG);
		logintoast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
		logintoast.show();
	}
	public static void loginFailed(Context context)
	{
		Toast logintoast=Toast.makeText(context,loginfailed,Toast.LENGTH_LONG);
		logintoast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
		logintoast.show();
	}
	
   
}
