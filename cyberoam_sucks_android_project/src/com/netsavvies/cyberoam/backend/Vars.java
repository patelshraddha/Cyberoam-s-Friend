package com.netsavvies.cyberoam.backend;

import java.util.Hashtable;

//TODO remove comment

/*
 * Parameters/Vars on which the service runs. Not accessible directly by gui
 */

public class Vars {
	
	static final public Hashtable<Const, Boolean> bool_hs;
	
	// Intervals
	static final int loginInterval = 2 * 60 * 60*1000; // if already loggend in, then automatically
	// attempt to login again after this interval.
	// Generally kept to be 2 -3 hrs
	static final int loginTrialInterval=30*1000; // if a login attempt fails, then try to
									// login again after this interval (5 -10
									// secs)
	static final int netCheckInterval=3*60*1000; // check if we are connected to the interval.
									// Have to give a thought on its value
	static final int cybCheckInterval=30*1000;
	static final int thresholdStrength = 30;
	static  String url = "http://10.100.56.55:8090/httpclient.html";
	
	
	static String loginMessage = "You have successfully logged in";
	
	static final String logoutMessage ="You have successfully logged off" ;
	static final String wrongIdpwdmessage = "The system could not log you on. Make sure your password is correct";
	static final String cyberlessMessage = "You are already logged in as a clientless user";
	static final String maxloginMessage = "You have reached Maximum Login Limit.";
	
	static int icon;
	static String message;
	
	//strings/messages
	
	static final public Hashtable<Const,String> strings;
	static final public Hashtable<Const,Integer> icons;
	static final public Hashtable<Const,String> buttons;
	// user data
	static String loginId;
	static boolean isloggedIn;
	static String password;

	public static int imageid;

	static {
		bool_hs = new Hashtable<Const, Boolean>();
		strings = new Hashtable<Const, String>();
		icons=new Hashtable<Const,Integer>();
		buttons=new Hashtable<Const,String>();
	}
}
