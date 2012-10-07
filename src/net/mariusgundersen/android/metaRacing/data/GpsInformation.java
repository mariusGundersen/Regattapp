package net.mariusgundersen.android.metaRacing.data;

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

	private final Callback<Integer> satelliteCountCallback;
	private final Callback<String> gpsStateCallback;
	private static final String TAG = "GpsInfo";

	public GpsInformation(Context context, Callback<Integer> satelliteCountCallaback, Callback<String> gpsStateCallback) {
		this.satelliteCountCallback = satelliteCountCallaback;
		this.gpsStateCallback = gpsStateCallback;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		
		locationManager.addGpsStatusListener(gpsListener);
	}
	
	GpsStatus.Listener gpsListener = new GpsStatus.Listener(){

		private GpsStatus status;

		@Override
		public void onGpsStatusChanged(int event) {
			switch(event){
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				gpsStateCallback._("Found location");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				gpsStateCallback._("GPS running");
				break;
			case GpsStatus.GPS_EVENT_STARTED:
				gpsStateCallback._("Starting the GPS");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				gpsStateCallback._("GPS stopped");
				break;
			default:
				gpsStateCallback._("unknown state");
				break;
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

	private LocationManager locationManager;

}
