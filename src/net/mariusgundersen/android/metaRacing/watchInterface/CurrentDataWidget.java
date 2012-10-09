package net.mariusgundersen.android.metaRacing.watchInterface;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class CurrentDataWidget extends Widget{
	public static final String ID = "net.mariusgundersen.android.metaRacing.currentData";
	public static final String DESC = "Sailing Watch Current (96x32)";
	private static final String TAG = "Widget";
	private static final int PRIORITY = 1;
	private static final int WIDTH = 96;
	private static final int HEIGHT = 32;

	

	private DecimalFormat decOneDigit;
	private DecimalFormat decTwoDigits;
	
	public CurrentDataWidget(Context context){
		super(context, WIDTH, HEIGHT);

		decOneDigit = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.ENGLISH));
		decTwoDigits = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
		
	}
	
	public synchronized void genWidget(double bearing, double knots){
		
		
		String brgString = String.valueOf(Math.round(bearing)) + "°";
		String ktsString = toFixed(knots) + "kt";
		
		Log.d(TAG, ktsString+", "+brgString);
		
		renderer.clearCanvas(widgetCanvas);
		renderer.renderTextToCanvas(brgString, widgetCanvas, new Rect(4, HEIGHT-4, WIDTH-8, HEIGHT-56), Paint.Align.LEFT);
		renderer.renderTextToCanvas(ktsString, widgetCanvas, new Rect(WIDTH-4, HEIGHT-4, WIDTH-8, HEIGHT-56), Paint.Align.RIGHT);
		
		sendWidget(ID, DESC, PRIORITY);
	}

	public synchronized void genWidget(String text) {
		renderer.clearCanvas(widgetCanvas);
		renderer.renderTextToCanvas(text, widgetCanvas);
		sendWidget(ID, DESC, PRIORITY);
	}

	
	private String toFixed(double v ){
		if(v >= 10){
			return decOneDigit.format(v);
		}else{
			return decTwoDigits.format(v);
		}
	}
	

	public void genWidgetCentered(String text) {
		renderer.clearCanvas(widgetCanvas);
		renderer.renderTextToCanvas(text, widgetCanvas, new Rect(WIDTH/2, HEIGHT-4, WIDTH-8, HEIGHT-56), Paint.Align.CENTER);
		sendWidget(ID, DESC, PRIORITY);
	}
}
