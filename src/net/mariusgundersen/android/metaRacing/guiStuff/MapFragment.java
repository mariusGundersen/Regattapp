package net.mariusgundersen.android.metaRacing.guiStuff;

import net.mariusgundersen.android.metaRacing.MapActivity;
import android.app.Activity;

public class MapFragment extends ActivityManagerFragment {

	@Override
	protected Class<? extends Activity> getActivityClass() {
		return MapActivity.class;
	}

}
