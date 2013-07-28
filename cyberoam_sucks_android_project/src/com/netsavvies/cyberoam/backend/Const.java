package com.netsavvies.cyberoam.backend;

public enum Const {
	// booleans, dispatch levels 
	top,
	wifi,
	wifiForLocha,
	net,
	str,
	c,
	l,
	lF,
	
	isRunning,
	isDisabled,
	cyberLess,
	
	
	
	//messages
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
	//wifi
	wifiLocha,
	wifiLochaKhatam,
	
	

	// other variables
	noUser,
	notChecked,
	
	
	//messages
	otherWifi,
	loginFailed,
	loggedOutLessStrength,
	current;
	
	
}
