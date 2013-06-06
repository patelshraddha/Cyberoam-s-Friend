package com.netsavvies.cyberoam.backend;

/*
 * Parameters/Vars on which the service runs. Not accessible directly by gui
 */

class StaticParameters {
	//Intervals
	static int loginInterval; // if already loggend in, then automatically attempt to login again after this interval. Generally kept to be 2 -3 hrs
	static int loginTrialInterval; //if a login attempt fails, then try to login again after this interval (5 -10 secs)
	static int loginCheckInterval; // check if we are connected to the interval. Have to give a thought on its value
	
	//user data
	static int userId;
	static int password;
	
	//bools
	static boolean isAutoLoginAfterIntervalOn;
	static boolean isAutoCheckOn;
	static boolean isAutoTryOn;
	
}
