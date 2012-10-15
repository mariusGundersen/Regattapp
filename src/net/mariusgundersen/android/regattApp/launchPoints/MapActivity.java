package net.mariusgundersen.android.regattApp.launchPoints;

import android.os.Bundle;
import net.mariusgundersen.android.regattApp.R;
import net.mariusgundersen.android.regattApp.R.layout;

public class MapActivity extends com.google.android.maps.MapActivity {

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map_fragment);
	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
