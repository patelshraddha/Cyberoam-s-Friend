package com.netsavvies.cyberoam.backend;

/*
 * The service which performs the actual tasks. Started by Control.class/CybApp.class or by the system at boot and not directly accessible by gui
 */

import java.util.Hashtable;

import com.netsavvies.cyberoam.gui.InformGui;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import static com.netsavvies.cyberoam.backend.Const.*;
import static com.netsavvies.cyberoam.backend.Vars.*;
import static com.netsavvies.cyberoam.backend.Methods.*;

public class CybService extends Service {

	int connection;

	private Handler theHandler = new Handler() {

		protected void finalize() {
			dispatch(top, stop);
		}

		@Override
		public void handleMessage(Message msg) {
			Const constMsg = Const.valueOf(msg.getData().getString(("Const")));
			if (constMsg == init) {
				init();
			} else {
				dispatch(top, constMsg);
			}
			super.handleMessage(msg);
		}
	}; // the thread handling all the actual operations

	private BroadcastReceiver receiversForCybService;
	// all events
	// all states
	private Handler handler;
	private Hashtable<Const, Runnable> run_hs;
	private Hashtable<Const, Boolean> runExist_hs;

	private Hashtable<Const, BroadcastReceiver> bcr_hs;
	private Hashtable<Const, Boolean> bcrExist_hs;

	// all commands

	private void init() {
		
		run_hs = new Hashtable<Const, Runnable>();
		runExist_hs = new Hashtable<Const, Boolean>();
		bcr_hs = new Hashtable<Const, BroadcastReceiver>();
		bcrExist_hs = new Hashtable<Const, Boolean>();

		// set boolean & noti

		/* not needed
		set(c, false);
		set(l, false);
		set(str, false);
		set(wifi, false);
		set(isHalted, false);
		set(cyberLess, false);
		*/

		// set handler and tasks
		run_hs.put(c, new Runnable() {

			@Override
			public void run() {
				Log.wtf("hadler cybcheck", runExist_hs.put(c, false) + "");
				runExist_hs.put(c, false);
				timer(c, true);
				dispatch(top, cybCheck);
			}

		});
		run_hs.put(l, new Runnable() {

			@Override
			public void run() {
				Log.wtf("hadler logincheck", runExist_hs.put(c, false) + "");
				if (runExist_hs.get(l)) {
					runExist_hs.put(l, false);
					timer(l, true);
				}
				if (runExist_hs.get(lF)) {
					runExist_hs.put(lF, false);
					timer(lF, true);
				}

				dispatch(top, reLogin);
			}
		});

		run_hs.put(net, new Runnable() {

			@Override
			public void run() {
				Log.wtf("hadler netcheck", runExist_hs.put(c, false) + "");
				runExist_hs.put(net, false);
				timer(net, true);
				dispatch(top, netCheck);

			}
		});

		runExist_hs.put(c, false);
		runExist_hs.put(l, false);
		runExist_hs.put(lF, false);
		runExist_hs.put(net, false);

		handler = new Handler();

		// receivers
		bcr_hs.put(wifi, new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					if (Methods.isWifiConnected(context) ) {
						//wifi connected
						if(get(wifiLocha)){
							if(!get(wifiLochaThikKaring)){
								set(wifiLochaThikKaring,true);
								dispatch(top, wifiKaLochaTheekKaro);
							}
						}
						else if (!get(wifi)){
							set(wifi, true);
							dispatch(top, wifiConnected);
						}

					} else { //wifi disc
						
						if(get(wifiLocha)){
							if(get(wifiLochaThikKaring)){
								set(wifiLochaThikKaring,false);
								dispatch(top, wifiKaLochaGaya);
							}
						}
						else if (get(wifi)){
							set(wifi, false);
							dispatch(top, wifiDisconnected);
						}
						
					}
				}
			}
		});

		bcr_hs.put(str, new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
					// TODO: use extra
					if (check(str)) {
						if (!get(str)) {
							set(str, true);
							dispatch(top, strChange);
						}
					} else {
						if (get(str)) {
							set(str, false);
							dispatch(top, strChange);
						}
					}
				}
			}
		});

		bcrExist_hs.put(wifi, false);
		bcrExist_hs.put(wifiLocha, false);
		bcrExist_hs.put(str, false);

	}

	private boolean check(Const key) {
		boolean result = false;
		switch (key) {
		case wifi:
			result = Methods.isWifiConnected(getApplicationContext());
			break;
		case c:
			result = Methods.isCyberoamAvailbale(getApplicationContext());
			break;
		case l:
			result = get(key);
		case str:
			result = Methods.isStrengthEnough(getApplicationContext());
		}
		Log.wtf("check", key.name() + " " + result);
		return result;

	}

	private void timer(Const key, boolean bool) {
		Log.wtf("timer", key.name() + " " + bool);

		switch (key) {
		case l:
			if (bool) {

				if (!(runExist_hs.get(l) || runExist_hs.get(lF))) {
					handler.postDelayed(run_hs.get(l), Vars.loginInterval);
					runExist_hs.put(l, true);

				}

			} else {

				handler.removeCallbacks(run_hs.get(l));
				runExist_hs.put(l, false);
			}

			break;

		case lF:
			if (bool) {

				if (runExist_hs.get(l)) {
					timer(l, false);

				} else if (!runExist_hs.get(lF)) {
					handler.postDelayed(run_hs.get(l), Vars.loginTrialInterval);
					runExist_hs.put(lF, true);
				}

			} else {

				handler.removeCallbacks(run_hs.get(l));
				runExist_hs.put(l, false);
			}

			break;
		case c:
			if (bool) {
				if (!(runExist_hs.get(c))) {
					handler.postDelayed(run_hs.get(c), Vars.cybCheckInterval);
					runExist_hs.put(c, true);
				}

			} else {
				handler.removeCallbacks(run_hs.get(c));
				runExist_hs.put(c, false);
			}
			break;
		case net:
			if (bool) {
				if (!(runExist_hs.get(net))) {
					handler.postDelayed(run_hs.get(net), Vars.netCheckInterval);
					runExist_hs.put(net, true);
				}

			} else {

				handler.removeCallbacks(run_hs.get(net));
				runExist_hs.put(net, false);
			}
			break;
		}
	}

	private void receiver(Const key, boolean bool) {
		Log.wtf("reciever", key.name() + " " + bool);

		switch (key) {
		case wifi:
			if (bool && !bcrExist_hs.get(wifi)) {
				registerReceiver(bcr_hs.get(wifi), new IntentFilter(
						ConnectivityManager.CONNECTIVITY_ACTION));
				bcrExist_hs.put(wifi, true);
			} else if (bcrExist_hs.get(wifi)) {
				unregisterReceiver(bcr_hs.get(wifi));
				bcrExist_hs.put(wifi, false);
			}
			break;

		case wifiLocha:
			if (bool && !bcrExist_hs.get(wifiLocha)) {
				registerReceiver(bcr_hs.get(wifiLocha), new IntentFilter(
						ConnectivityManager.CONNECTIVITY_ACTION));
				bcrExist_hs.put(wifiLocha, true);
			} else if (bcrExist_hs.get(wifiLocha)) {
				unregisterReceiver(bcr_hs.get(wifiLocha));
				bcrExist_hs.put(wifiLocha, false);
			}
			break;

		case str:
			if (bool && !bcrExist_hs.get(str)) {
				registerReceiver(bcr_hs.get(str), new IntentFilter(
						WifiManager.RSSI_CHANGED_ACTION));
				bcrExist_hs.put(str, true);
			} else if (bcrExist_hs.get(str)) {
				unregisterReceiver(bcr_hs.get(str));
				bcrExist_hs.put(str, false);
			}
			break;

		default:
			// locho
			break;

		}
	}

	private void dispatch(Const level, Const command) {
		Log.wtf("dispatch", level.name() + " " + command.name());
		Toast.makeText(getApplicationContext(), level + "   " + command,Toast.LENGTH_SHORT).show();
		switch (level) {
		case top:
			switch (command) {

			case disable:
				set(isDisabled, true);
				command = stop;
			case wrongIdPwd:
			case stop:
				if (get(isRunning)) {
					set(isRunning, false);
					dispatch(wifi, stop);
				}
				break;
			case enable:
				set(isDisabled, false);
				command = start;
			case start:
				if (!get(isRunning) && !get(isDisabled)) {
					set(isRunning, true);
					dispatch(wifi, command);
				} else {
					Log.wtf("service", "halted or running service can't start");
				}

				break;
			case restart:
				dispatch(top, stop);
				dispatch(top, start);

				break;
			default:
				if (get(isRunning)) {
					dispatch(wifi, command);
				}
				break;
			}

			break; // Top Over

		case wifi: {
			// after getting ip address
			switch (command) {
			case stop:
				receiver(wifi, false);
				handleGuiStatus(stop);
			case wifiDisconnected:
				// InformGui.updateGuiStatus(wifiDisconnected, true);
			case wifiConnected:
				dispatch(net, command);
				break;
			case start:
				if (check(wifi)) {
					set(wifi, true);
					receiver(wifi, true);
					dispatch(net, command);
				} else {
					Toast.makeText(this,"wifidisconnected",Toast.LENGTH_SHORT).show();
					handleGuiStatus(wifiDisconnected);
					receiver(wifi, true);
				}
				break;
			case wifiKaLochaAaya:
				set(wifiLocha, true);
				execute(wifi, true, this);
				handleGuiStatus(wifiKaLochaAaya);
				break;
			case wifiKaLochaTheekKaro:
				dispatch(net, wifiKaLochaTheekKaro);
				execute(wifi, false, this);
				break;
			case wifiKaLochaGaya:
				set(wifiLocha, false);
				handleGuiStatus(wifiDisconnected);
				break;
			case wifiKaBahotBadaLocha:
				// don't know
				break;
			default:
				dispatch(net, command);
				break;
			}
		}
			break; // Wifi over

		case net: {
			switch (command) {
			case wifiKaLochaTheekKaro:
			case strChange:
			case reLogin:
			case cybCheck:
				dispatch(str, command);
				break;
			case wifiDisconnected:
			case wrongIdPwd:
			case stop:
				timer(net, false);
				set(net, false);
				dispatch(str, command);
				break;
			case wifiConnected:
			case start:
				timer(net, true);
				if (Methods.isConnectionAlive(getApplicationContext()) == 1) {
					// set(cyberLess, true);
					set(net, true);
					handleGuiStatus(otherWifi);
					// inform that cyberless login or another wifi
				} else {
					dispatch(str, command);
				}
				break;
			case netCheck:
				int retValue = Methods
						.isConnectionAlive(getApplicationContext());
				if (retValue == 1)
					set(net, true);
				else {
					set(net, false);
					switch (retValue) {
					case 0:
						dispatch(net, notLoggedIn);
						break;
					case 2:
						dispatch(str, noNet);
						break;
					}
				}
				break;
			case netRecheck:
				if (Methods.isConnectionAlive(getApplicationContext()) == 1) {
					set(net, true);
					handleGuiStatus(loggedIn);
				} else {
					set(net, false);
					handleGuiStatus(noNet);
				}
				break;

			}

		}
			break; // Net over

		case str:
			switch (command) {
			case noNet:
			case notLoggedIn:
				set(str, check(str));
				break;

			case wifiKaLochaTheekKaro:
				dispatch(c, command);
				return;
			case wifiConnected:
			case start:
				if (check(str)) {
					set(str, true);
					receiver(str, true);
					dispatch(c, command);

				} else {
					handleGuiStatus(lowSTR);
					receiver(str, true);
				}
				return;

			case stop:
			case wifiDisconnected:
				set(str, false);
			case wrongIdPwd:
				receiver(str, false);
				dispatch(c, command);
				return;
			case strChange:
				if (get(str)) {
					dispatch(c, highSTR);
				} else {
					dispatch(c, lowSTR);
				}
				return;
			}

			if (get(str)) {
				switch (command) {
				case cybCheck:
				case reLogin:
				case noNet:
				case notLoggedIn:
					dispatch(c, command);
					break;
				default:
					// locha
				}
			}
			break;

		case c: {
			if (check(c)) {
				timer(c, false);

				if (!get(c))
					set(c, true);
				switch (command) {
				case stop:
					set(c, false);
					dispatch(l, command);
					break;
				case wifiKaLochaTheekKaro:
					dispatch(l, wifiKaLochaTheekKaro);
					set(c, false);
					break;
				case wrongIdPwd:
					break;
				default:
					dispatch(l, command);
					break;
				}
			} else {
				switch (command) {
				case wifiDisconnected:
				case stop:
					set(c, false);
					timer(c, false);
					dispatch(l, command);
					break;

				case noNet:
				case cybCheck:
				case wifiConnected:
				case start:
				case highSTR:
				case reLogin:
					timer(c, true);
					handleGuiStatus(noCyb);
					break;

				case lowSTR:
				case wifiKaLochaTheekKaro:
					handleGuiStatus(wifiKaBahotBadaLocha);
					dispatch(top, wifiKaBahotBadaLocha);
					timer(c, false);
					break;

				default:
					timer(c, false);
					break;
				}

				if (get(c)) {
					dispatch(l, noCyb);

				}

			}
		}
			break; // C over

		case l: {
			timer(l, false);
			set(noUser, false);
			switch (command) {
			case noCyb:
				if (get(l))
					// big locha
					set(l, false);
				break;
			case lowSTR:
				if (get(l)) {
					Methods.attemptLogout(getApplicationContext(),
							Vars.loginId, Vars.password);
					handleGuiStatus(loggedOutLessStrength);
					set(l, false);
				}
				break;

			case notLoggedIn:
				set(l, false);
			case highSTR:
			case cybCheck:
			case reLogin:
			case wifiConnected:
			case noNet:
			case start:
				if ((get(l)) && (command != reLogin)) {
					if (command == noNet) {
						// inform that logged in but still no net
					}
				} else {
					Const result = Methods
							.attemptLogin(getApplicationContext());
					if (result == loggedIn) {
						set(maxLogin, false);
						set(l, true);
						timer(l, true);
						handleGuiStatus(loggedIn);
						dispatch(top, netRecheck);
					} else {
						set(l, false);
						switch (result) {
						case maxLogin:
							set(maxLogin, true);
							handleGuiStatus(loginFailed);
							timer(lF, true);
							break;
						case noUser:
							handleGuiStatus(noUser);
							set(noUser, true);
							dispatch(top, stop);
							break;
						case wrongIdPwd:
							handleGuiStatus(loginFailed);
							dispatch(top, wrongIdPwd);
							break;
						}
					}
				}

				break;

			case wifiKaLochaTheekKaro:
			case stop:
				if (get(l))
					Methods.attemptLogout(getApplicationContext(),
							Vars.loginId, Vars.password);
				set(l, false);
				break;

			case wifiDisconnected:
				if (get(l))
					dispatch(top, wifiKaLochaAaya);
				else
					handleGuiStatus(wifiDisconnected);
				break;
			default:
				break;
			}
		}
		default:
			break;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		theHandler.handleMessage(constructHandlerMessage(init));

		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.wtf("WTF", "lowMemory");
		theHandler.handleMessage(constructHandlerMessage(stop));
	}

	/*
	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		Log.wtf("WTF","trimMemory");
		dispatch(top, stop);
	}
	*/

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.wtf("WTF", "destroy");
		theHandler.handleMessage(constructHandlerMessage(stop));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Vars.isloggedIn = false;
		Vars.loginId = null;
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		theHandler.handleMessage(constructHandlerMessage(start));

		return super.onStartCommand(intent, flags, startId);

	}

	private void handleGuiStatus(Const key){
		startForeground(notiId,InformGui.updateGuiStatus(key,this));
	}
	
}
