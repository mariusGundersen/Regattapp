package net.mariusgundersen.android.metaRacing;

import net.mariusgundersen.android.metaRacing.data.GpsInformation;
import net.mariusgundersen.useful.Callback;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatusFragment extends Fragment {
	private TextView gpsStateText;
	private TextView satellitesFoundText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.status_fragment, container, false);

		gpsStateText = (TextView) view.findViewById(R.id.gpsState);
		satellitesFoundText = (TextView) view.findViewById(R.id.satellitesFound);

		satellitesFoundText.setText("Finding...");
		gpsStateText.setText("Finding...");
		

		new GpsInformation(view.getContext(), new Callback<Integer>() {
			public void _(Integer param) {
				satellitesFoundText.setText(param + "");
			}
		}, new Callback<GpsInformation.State>() {
			public void _(GpsInformation.State param) {
				gpsStateText.setText(textFromState(param));
			}
		});

		return view;
	}
	
	private String textFromState(GpsInformation.State state){
		switch(state){
		case Started:
			return "GPS started";
		case Stopped:
			return "GPS stopped";
		case Locating:
			return "GPS locating you";
		case Located:
			return "GPS got fix";
		}
		return "";
	}
	
}
