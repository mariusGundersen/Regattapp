package net.mariusgundersen.android.metaRacing;

import net.mariusgundersen.android.metaRacing.watchInterface.CurrentDataWidget;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WidgetFragment extends Fragment {
	private ImageView previewImage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_fragment, container, false);
        

		previewImage = (ImageView) view.findViewById(R.id.previewImage);
		previewImage.setImageBitmap(new CurrentDataWidget(view.getContext()).getBitmap());
		
		return view;
	}
}
