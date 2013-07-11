package com.netsavvies.cyberoam.backend;

//TODO remove comment

/*
 * Parameters/Vars on which the service runs. Not accessible directly by gui
 */

public class Vars {
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
	static int thresholdStrength = 30;
	static String url = "http://10.100.56.55:8090/httpclient.html";
	
	// user data
	static String loginId;
	static boolean isloggedIn;
	static String password;

	// bools
	/*
	static boolean isAutoLoginAfterIntervalOn;
	static boolean isAutoCheckOn;
	static boolean isAutoTryOn;
	*/

}
