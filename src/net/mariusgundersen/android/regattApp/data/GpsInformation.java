package net.mariusgundersen.android.regattApp.data;

import java.util.Iterator;

import net.mariusgundersen.useful.Callback;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsInformation {

	private LocationManager locationManager;
	private final Callback<Integer> satelliteCountCallback;
	private final Callback<State> gpsStateCallback;
	private int previousEvent = -1;
	private static final String TAG = "GpsInfo";

	public GpsInformation(Context context, Callback<Integer> satelliteCountCallaback, Callback<State> gpsStateCallback) {
		this.satelliteCountCallback = satelliteCountCallaback;
		this.gpsStateCallback = gpsStateCallback;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		
		locationManager.addGpsStatusListener(gpsListener);
	}
	
	public void stop(){
		locationManager.removeGpsStatusListener(gpsListener);
	}
	
	GpsStatus.Listener gpsListener = new GpsStatus.Listener(){

		private GpsStatus status;

		@Override
		public void onGpsStatusChanged(int event) {
			
			if(event != previousEvent){
				gpsStateCallback._(State.fromGpsStatus(event));
				previousEvent = event;
			}
			
			if(event == GpsStatus.GPS_EVENT_SATELLITE_STATUS){
				status = locationManager.getGpsStatus(status);
				Iterator<GpsSatellite> sats = status.getSatellites().iterator();
	
				int satellitesFound = 0;
				while (sats.hasNext()) {
					GpsSatellite gpsSatellite = sats.next();
	
					if (gpsSatellite.usedInFix()) {
						satellitesFound++;
					}
				}
	
				satelliteCountCallback._(satellitesFound);
			}else{
				satelliteCountCallback._(0);
			}
		}
		
	};


	public enum State {
		Started, Stopped, Locating, Located;

		public static State fromGpsStatus(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				return Started;
			case GpsStatus.GPS_EVENT_STOPPED:
				return Stopped;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				return Located;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				return Locating;
			default:
				return Stopped;

			}
		}
	}
}
