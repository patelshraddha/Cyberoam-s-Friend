package com.netsavvies.cyberoam.backend;

public enum Const {
	//booleans, dispatch levels, timers
		top,
		wifi,
		wifiLocha,
		wifiLochaThikKaring,
		net,
		str,
		c,
		l,
		lF, //timer
		
		//bools
		isRunning,
		isDisabled,
		cyberLess,
	
	
	
	//commands
		//TOP
		start,
		restart,
		stop,
		disable,
		enable,
		init,
		
		//wifi
		wifiConnected,
		wifiDisconnected,
		wifiKaLochaAaya,
		wifiKaLochaGaya,
		wifiKaLochaTheekKaro,
		wifiKaBahotBadaLocha,
	
		//net
		noNet,
		netRecheck,
		netCheck,
		cybPage,
	
		//str
		highSTR,
		lowSTR,
		strChange,
	
		//C
		noCyb,
		cybCheck,
	
		//L
		notLoggedIn ,
		maxLogin ,
		reLogin ,
		wrongIdPwd,
	    loggedIn,
	
	//receivers
	    
	    
	// other variables
		noUser,
		notChecked,
	
	
	//status messages,strings
		otherWifi,
		loginFailed,
		loggedOutLessStrength,
		current,
		notiTitle,
	
		//gui commands
		updateGui;
	
}
