package net.mariusgundersen.android.regattApp.launchPoints;

import net.mariusgundersen.android.regattApp.R;
import net.mariusgundersen.android.regattApp.data.GpsInformation;
import net.mariusgundersen.useful.Callback;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatusFragment extends Fragment {
	private static final String SATELLITES_FOUND_TEXT = "satellitesFoundText";
	private static final String GPS_STATE_TEXT = "gpsStateText";
	private TextView gpsStateText;
	private TextView satellitesFoundText;
	private GpsInformation gpsInfo;
	private View view;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Log.d("Status", "restoreInstanceState");
		if(savedInstanceState != null && view != null){
			satellitesFoundText.setText(savedInstanceState.getString(SATELLITES_FOUND_TEXT, ""));
			gpsStateText.setText(savedInstanceState.getString(GPS_STATE_TEXT, ""));
		}

		if(gpsInfo == null){
			gpsInfo = new GpsInformation(getActivity().getApplicationContext(), new Callback<Integer>() {
				public void _(Integer param) {
					satellitesFoundText.setText(param + "");
				}
			}, new Callback<GpsInformation.State>() {
				public void _(GpsInformation.State param) {
					gpsStateText.setText(textFromState(param));
				}
			});
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view == null){
			view = inflater.inflate(R.layout.status_fragment, container, false);
	
			gpsStateText = (TextView) view.findViewById(R.id.gpsState);
			satellitesFoundText = (TextView) view.findViewById(R.id.satellitesFound);
		}

		return view;
	}
	
	private String textFromState(GpsInformation.State state){
		switch(state){
		case Started:
			return "GPS started";
		case Stopped:
			return "GPS stopped";
		case Locating:
			return "GPS connected to satellites";
		case Located:
			return "GPS got fix";
		}
		return "";
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("Status", "saveInstanceState");
		super.onSaveInstanceState(outState);
		if(view != null){
			outState.putString(GPS_STATE_TEXT, gpsStateText.getText().toString());
			outState.putString(SATELLITES_FOUND_TEXT, satellitesFoundText.getText().toString());
		}
	}
	
	
	
}
