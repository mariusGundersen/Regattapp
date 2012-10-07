package net.mariusgundersen.android.metaRacing.watchInterface;

import java.util.ArrayList;
import java.util.Arrays;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WidgetIntentReceiver extends BroadcastReceiver {

	private static final String TAG = "WidgetIntentReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action != null
				&& action
						.equals("org.metawatch.manager.REFRESH_WIDGET_REQUEST")) {

			Log.d(TAG, "Received intent");

			Bundle bundle = intent.getExtras();

			boolean getPreviews = bundle
					.containsKey("org.metawatch.manager.get_previews");
			if (getPreviews)
				Log.d(TAG, "get_previews");

			ArrayList<String> widgets_desired = null;

			if (bundle.containsKey("org.metawatch.manager.widgets_desired")) {
				Log.d(TAG, "widgets_desired");
				widgets_desired = new ArrayList<String>(
						Arrays.asList(bundle
								.getStringArray("org.metawatch.manager.widgets_desired")));
			}

			// Check if widgets_desired contains each widget ID you're
			// responsible for
			// and send an update
			if(widgets_desired != null){
				widgets_desired = new ArrayList<String>();
			}
			
			// Always send an update if the broadcast specifies get_previews
			if(getPreviews || widgets_desired.contains(CurrentDataWidget.ID)){
				
				new CurrentDataWidget(context).genWidgetPaused();
				
			}
			if(getPreviews || widgets_desired.contains(MaxMinWidget.ID)){
				
				new MaxMinWidget(context).genWidget(0f);
			
			}
		}
	}

}
