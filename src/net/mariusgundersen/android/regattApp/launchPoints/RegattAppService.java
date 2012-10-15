package net.mariusgundersen.android.regattApp.launchPoints;

import net.mariusgundersen.android.regattApp.R;
import net.mariusgundersen.android.regattApp.R.drawable;
import net.mariusgundersen.android.regattApp.application.RunningAverageBearing;
import net.mariusgundersen.android.regattApp.application.RunningAverageSpeed;
import net.mariusgundersen.android.regattApp.data.Delta;
import net.mariusgundersen.android.regattApp.data.GpsInformation;
import net.mariusgundersen.android.regattApp.data.Point;
import net.mariusgundersen.android.regattApp.data.RecentRoute;
import net.mariusgundersen.android.regattApp.data.RunningAverage;
import net.mariusgundersen.android.regattApp.watchInterface.BitmapUtil;
import net.mariusgundersen.android.regattApp.watchInterface.CurrentDataWidget;
import net.mariusgundersen.android.regattApp.watchInterface.MaxMinWidget;
import net.mariusgundersen.android.regattApp.watchInterface.NotificationRenderer;
import net.mariusgundersen.useful.Callback;
import net.mariusgundersen.useful.Lambda;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;

public class RegattAppService extends Service {

	private static final int TICK_DURATION = 3000;
	private static final String TAG = "RegattAppService";
	private static final double TO_KNOTS =  3600.0 / 1852.0;

	private long lastWidgetUpdateTick = 0;

	private double maxSpeed = 0;

	private LocationManager locationManager;
	private MaxMinWidget maxMinWidget;
	private CurrentDataWidget currentDataWidget;
	private GpsInformation gpsInformation;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (currentDataWidget == null) {
			currentDataWidget = new CurrentDataWidget(getApplicationContext());
			maxMinWidget = new MaxMinWidget(getApplicationContext());
		}
		if (locationManager == null) {
			locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
		}
		Log.d(TAG, "start service");

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		gpsInformation = new GpsInformation(getApplicationContext(), satelliteCountChanged, gpsStateChanged);
		startRacing();
		createNotification();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "stop service");
		locationManager.removeUpdates(locationListener);
		gpsInformation.stop();

		stopRacing();
		removeNotification();
		super.onDestroy();
	}

	protected boolean gotFix = false;

	private Callback<GpsInformation.State> gpsStateChanged = new Callback<GpsInformation.State>() {public void _(GpsInformation.State param) {
		if(param != GpsInformation.State.Locating){
			gotFix = false;
		}
	}};
	
	private Callback<Integer> satelliteCountChanged = new Callback<Integer>() {public void _(Integer param){
		gotFix = param >= 4;
		if(gotFix == false){
			currentDataWidget.genWidgetCentered(param + (param == 1 ? " satellite" : " satellites"));
		}
	}};

	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			if(gotFix)
				findSpeedAndBearing(location);
		}

		public void onProviderEnabled(String provider) {}
		public void onProviderDisabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
	
	private Notification notification;
	private RecentRoute route = new RecentRoute();
	private Point prevPoint;
	private long lastTick = 0;
	private RunningAverageSpeed avgSpeed = new RunningAverageSpeed(10);
	private RunningAverageBearing avgBearing = new RunningAverageBearing(10);

	private void findSpeedAndBearing(Location location) {
		Point point = new Point(location);
		route.addPoint(point);
		if(prevPoint != null){
			Delta delta = new Delta(prevPoint, point);
			avgSpeed.eventOccured(delta);
			avgBearing.eventOccured(delta);
		}
		prevPoint = point;
		// float vmg = location.bearingTo(dest)

		long tick = System.currentTimeMillis();
		if (tick - lastTick >= TICK_DURATION) {
			double spd = 0.0;
			double brg = 0.0;
			if(lastTick != 0){
				spd = avgSpeed.average()*TO_KNOTS;
				brg = avgBearing.average();
				if (spd > maxSpeed) {
					maxSpeed = spd;
					maxMinWidget.genWidget(maxSpeed);
				}
			}
			lastTick = tick;

			// sendApplicationText(text);
			currentDataWidget.genWidget(brg, spd);
		}
	}

	private void startRacing() {
		maxSpeed = 0;
		maxMinWidget.genWidget(maxSpeed);
		currentDataWidget.genWidgetCentered("Starting Race");
		// startApp();
	}

	private void stopRacing() {
		currentDataWidget.genWidgetCentered("On Land");
		// stopApp();
	}

	public void createNotification() {
		int notificationIcon = R.drawable.notification_icon;
		notification = new Notification(notificationIcon,
				"Meta Sailing", System.currentTimeMillis());
		notification.flags |= android.app.Notification.FLAG_ONGOING_EVENT;

		notification.setLatestEventInfo(this, "Meta Sailing Watch", "Racing",
				createNotificationPendingIntent());

		startForeground(1, notification);
	}

	private PendingIntent createNotificationPendingIntent() {
		return PendingIntent.getActivity(this, 0, new Intent(this,
				RegattAppActivity.class), 0);
	}

	public void removeNotification() {
		stopForeground(true);
	}


}
