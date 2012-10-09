package net.mariusgundersen.android.metaRacing;

import net.mariusgundersen.android.metaRacing.guiStuff.MapFragment;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Switch;

public class MetaRacingActivity extends Activity {


	private static final String SELECTED_TAB = "SELECTED_TAB";
	private ActionBar actionbar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		actionbar = getActionBar();
		// Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		ActionBar.Tab statusTab = actionbar.newTab().setText("Status");
		ActionBar.Tab mapTab = actionbar.newTab().setText("Map");
		ActionBar.Tab widgetTab = actionbar.newTab().setText("Widget");

		// create the two fragments we want to use for display content
		Fragment statusFragment = new StatusFragment();
		Fragment mapFragment = new MapFragment();
		Fragment widgetFragment = new WidgetFragment();

		// set the Tab listener. Now we can listen for clicks.
		statusTab.setTabListener(new MyTabsListener(statusFragment));
		mapTab.setTabListener(new MyTabsListener(mapFragment));
		widgetTab.setTabListener(new MyTabsListener(widgetFragment));

		// add the two tabs to the actionbar
		actionbar.addTab(statusTab);
		actionbar.addTab(mapTab);
		actionbar.addTab(widgetTab);

		View actionCustomView = getLayoutInflater().inflate(R.layout.action_bar, null);
		Switch toggleServiceSwitch = (Switch) actionCustomView.findViewById(R.id.toggleServiceSwitch);
		toggleServiceSwitch.setChecked(isMyServiceRunning());
		actionbar.setCustomView(actionCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.RIGHT));

		int change = actionbar.getDisplayOptions() ^ ActionBar.DISPLAY_SHOW_CUSTOM;
		actionbar.setDisplayOptions(change, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt(SELECTED_TAB, actionbar.getSelectedTab().getPosition());
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		actionbar.selectTab(actionbar.getTabAt(savedInstanceState.getInt(SELECTED_TAB)));
	}
	

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// Toast.makeText(StartActivity.appContext, "Reselected!",
			// Toast.LENGTH_LONG).show();
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}

	}

	public void onToggleService(View view) {
		Intent intent = new Intent(getApplicationContext(),
				MetaRacingService.class);

		Switch toggleServiceButton = (Switch) view;
		if (toggleServiceButton.isChecked()) {
			startService(intent);
		} else {
			stopService(intent);
		}
	}
	
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	    	String name = service.service.getClassName();
	    	if(name.contains("MetaRacingService")) {
	            return true;
	        }
	    }
	    return false;
	}

}