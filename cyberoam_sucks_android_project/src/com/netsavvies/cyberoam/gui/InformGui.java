package com.netsavvies.cyberoam.gui;

import static com.netsavvies.cyberoam.backend.Const.loggedIn;
import static com.netsavvies.cyberoam.backend.Const.notiTitle;
import static com.netsavvies.cyberoam.backend.Methods.getButton;
import static com.netsavvies.cyberoam.backend.Methods.getIcon;
import static com.netsavvies.cyberoam.backend.Methods.getMessage;
import static com.netsavvies.cyberoam.backend.Vars.currBtn;
import static com.netsavvies.cyberoam.backend.Vars.currIcon;
import static com.netsavvies.cyberoam.backend.Vars.currMsg;
import static com.netsavvies.cyberoam.backend.Vars.strings;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.widget.Toast;

import com.netsavvies.cyberoam.backend.Const;
import com.netsavvies.cyberoam.backend.Vars;

/*
 * Methods eg. - loginFailed(), loggedIn(), loggedOut() - called from CybService
 * These methods will handle the creation of notifications/toasts/something-similar, to inform the user
 */

public class InformGui {

	private static String loginmessage = "You are logged in by id  ";
	private static String logoutmessage = "You are logged out";
	private static String loginfailed = "Login failed due to maximum login limit or due to wrong password";

	private static NotificationCompat.Builder notiBuilder = null;
	public static Notification updateGuiStatus(Const key, Context context) {
		
		if(key.equals(loggedIn))
			currMsg = getMessage(key)+Vars.loginId;
		else
			currMsg = getMessage(key);
		
		currIcon = getIcon(key);

		currBtn = getButton(key);
		
		Intent intent = new Intent(context, StatusActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		// Bitmap largeIcon =
		// BitmapFactory.decodeResource(context.getResources(),R.drawable.large);
		//Toast.makeText(context,"Notify"+getMessage(current),Toast.LENGTH_SHORT).show();
		
		if(notiBuilder == null)
		{
			 notiBuilder = new NotificationCompat.Builder(context)
				.setContentTitle(strings.get(notiTitle))
				.setContentText(currMsg)
				.setSmallIcon(currIcon)
				.setOnlyAlertOnce(true);
		}
		else{
			notiBuilder
			.setContentText(currMsg)
			.setSmallIcon(currIcon);
		}
    
		Notification currentNoti = notiBuilder.setContentIntent(pIntent).build();
		// Do not Hide the notification after its selected
		currentNoti.flags |= Notification.FLAG_NO_CLEAR;
		// Let the notification be ongoing
		currentNoti.flags |= Notification.FLAG_ONGOING_EVENT;
		// Let the notification be ongoing
		currentNoti.flags |= Notification.FLAG_FOREGROUND_SERVICE;
		
		return currentNoti;
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
	
}
