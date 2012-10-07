package net.mariusgundersen.android.metaRacing;

import net.mariusgundersen.android.metaRacing.data.Point;
import net.mariusgundersen.android.metaRacing.data.RecentRoute;
import net.mariusgundersen.android.metaRacing.watchInterface.BitmapUtil;
import net.mariusgundersen.android.metaRacing.watchInterface.CurrentDataWidget;
import net.mariusgundersen.android.metaRacing.watchInterface.MaxMinWidget;
import net.mariusgundersen.android.metaRacing.watchInterface.NotificationRenderer;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;

public class MetaRacingService extends Service {

	private static final int TICK_DURATION = 3000;
	private static final String TAG = "MetaRacingService";
	private static final double TO_KNOTS =  3600.0 / 1852.0;

	private long lastWidgetUpdateTick = 0;

	private double maxSpeed = 0;

	private LocationManager locationManager;
	private MaxMinWidget maxMinWidget;
	private CurrentDataWidget currentDataWidget;

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

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		startRacing();
		createNotification();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "stop service");
		locationManager.removeUpdates(locationListener);

		stopRacing();
		removeNotification();
		super.onDestroy();
	}

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location
			// provider.
			findSpeedAndBearing(location);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			String statusString = status == LocationProvider.AVAILABLE ? "available"
					: status == LocationProvider.OUT_OF_SERVICE ? "out of service"
							: status == LocationProvider.TEMPORARILY_UNAVAILABLE ? "temporary unavailable"
									: "";
			Log.d(TAG,
					provider + " " + statusString + " ("
							+ extras.getInt("satellites") + ")");
			currentDataWidget.genWidget(provider + statusString);
		}
	};
	private Notification notification;
	private RecentRoute route = new RecentRoute();
	private Point prevPoint;

	private void findSpeedAndBearing(Location location) {
		Point point = new Point(location);
		route.addPoint(point);
		// float vmg = location.bearingTo(dest)

		
		if (prevPoint == null || point.getTime() - prevPoint.getTime() >= TICK_DURATION) {
			double spd = 0.0;
			double brg = 0.0;
			if(prevPoint != null){
				spd = prevPoint.speedTo(point);
				brg = prevPoint.bearingTo(point);
				if (spd > maxSpeed) {
					maxSpeed = spd;
					maxMinWidget.genWidget(maxSpeed);
				}
			}
			prevPoint = point;

			// sendApplicationText(text);
			currentDataWidget.genWidget(brg, spd);
		}
	}

	private void startRacing() {
		maxSpeed = 0;
		maxMinWidget.genWidget(maxSpeed);
		currentDataWidget.genWidgetRacing();
		// startApp();
	}

	private void stopRacing() {
		currentDataWidget.genWidgetPaused();
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
				MetaRacingActivity.class), 0);
	}

	public void removeNotification() {
		stopForeground(true);
	}

	private void sendApplicationText(String text) {

		Intent broadcast = new Intent(
				"org.metawatch.manager.APPLICATION_UPDATE");
		Bundle b = new Bundle();

		byte[] image = BitmapUtil.bitmapToBuffer(new NotificationRenderer(
				getBaseContext()).renderText(text));

		b.putByteArray("buffer", image);

		broadcast.putExtras(b);

		this.sendBroadcast(broadcast);
	}

	private void startApp() {
		Intent broadcast = new Intent("org.metawatch.manager.APPLICATION_START");
		this.sendBroadcast(broadcast);

	}

	private void stopApp() {

		Intent broadcast = new Intent("org.metawatch.manager.APPLICATION_STOP");
		this.sendBroadcast(broadcast);
	}

}
