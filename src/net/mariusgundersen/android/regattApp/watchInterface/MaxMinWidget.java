package net.mariusgundersen.android.regattApp.watchInterface;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class MaxMinWidget extends Widget{
	public static final String ID = "net.mariusgundersen.android.regattApp.maxMinData";
	public static final String DESC = "Sailing Watch Max/Min (96x32)";
	private static final String TAG = "Widget";
	private static final int PRIORITY = 1;
	private static final int WIDTH = 96;
	private static final int HEIGHT = 32;

	private static final float TO_KNOTS = 1852.0f/3600.0f;
	

	private DecimalFormat decOneDigit;
	private DecimalFormat decTwoDigits;
	
	public MaxMinWidget(Context context){
		super(context, WIDTH, HEIGHT);

		decOneDigit = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.ENGLISH));
		decTwoDigits = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
		
	}
	
	public synchronized void genWidget(double knots){		
		
		String ktsString = "max: " + toFixed(knots) + "kt";
		
		renderer.clearCanvas(widgetCanvas);
		renderer.renderTextToCanvas(ktsString, widgetCanvas, new Rect(WIDTH-4, HEIGHT-4, WIDTH-8, HEIGHT-56), Paint.Align.RIGHT);
		
		sendWidget(ID, DESC, PRIORITY);
	}

	public synchronized void genWidget(String text) {
		Log.d(TAG, "genWidget() start");

		renderer.clearCanvas(widgetCanvas);
		renderer.renderTextToCanvas(text, widgetCanvas);

		sendWidget(ID, DESC, PRIORITY);

		Log.d(TAG, "genWidget() end");
	}

	
	private String toFixed(double v ){
		if(v >= 10){
			return decOneDigit.format(v);
		}else{
			return decTwoDigits.format(v);
		}
	}
}
