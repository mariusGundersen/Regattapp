package net.mariusgundersen.android.regattApp.guiStuff;

import net.mariusgundersen.android.regattApp.launchPoints.MapActivity;
import android.app.Activity;

public class MapFragment extends ActivityManagerFragment {

	@Override
	protected Class<? extends Activity> getActivityClass() {
		return MapActivity.class;
	}

}
