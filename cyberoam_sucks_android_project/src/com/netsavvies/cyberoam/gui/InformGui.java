package com.netsavvies.cyberoam.gui;

import android.content.Context;
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
