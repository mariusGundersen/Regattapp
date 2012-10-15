package net.mariusgundersen.android.regattApp.launchPoints;

import net.mariusgundersen.android.regattApp.R;
import net.mariusgundersen.android.regattApp.R.id;
import net.mariusgundersen.android.regattApp.R.layout;
import net.mariusgundersen.android.regattApp.watchInterface.BitmapUtil;
import net.mariusgundersen.android.regattApp.watchInterface.CurrentDataWidget;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WidgetFragment extends Fragment {
	private ImageView previewImage;
	private BroadcastReceiver receiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_fragment, container, false);
        

		previewImage = (ImageView) view.findViewById(R.id.previewImage);
			
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		receiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				
				Log.d("WidgetFragment", "received buffer");
				byte[] buffer = intent.getExtras().getByteArray("buffer");
				previewImage.setImageBitmap(BitmapUtil.bufferToBitmap(buffer));
			}
		
		};
		
		
		

		getActivity().registerReceiver(receiver, new IntentFilter("org.metawatch.manager.APPLICATION_UPDATE"));
		
	}
	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}
}
