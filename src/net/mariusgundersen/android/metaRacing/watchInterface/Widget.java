package net.mariusgundersen.android.metaRacing.watchInterface;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

public abstract class Widget {
	
	protected Context context;
	protected NotificationRenderer renderer;
	protected Bitmap widgetBitmap;
	protected Canvas widgetCanvas;
	protected int[] pixelArray;
	
	public Widget(Context context, int width, int height){
		
		this.context = context;
		renderer = new NotificationRenderer(context);
		
		
		pixelArray = new int[width * height];
		widgetBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		widgetCanvas = new Canvas(widgetBitmap);
		widgetCanvas.drawColor(Color.WHITE);
	}
	

	protected void sendWidget(String id, String desc, int priority) {

		renderPixelArray();
		
		Intent intent = new Intent("org.metawatch.manager.WIDGET_UPDATE");
		Bundle b = new Bundle();
		b.putString("id", id);
		b.putString("desc", desc);
		b.putInt("width", widgetBitmap.getWidth());
		b.putInt("height", widgetBitmap.getHeight());
		b.putInt("priority", priority);
		b.putIntArray("array", pixelArray);
		intent.putExtras(b);

		context.sendBroadcast(intent);
	}

	protected int[] renderPixelArray() {
		widgetBitmap.getPixels(pixelArray, 0, widgetBitmap.getWidth(), 0, 0,
				widgetBitmap.getWidth(), widgetBitmap.getHeight());
		return pixelArray;
	}

	public synchronized Bitmap getBitmap() {		
		return widgetBitmap;
	}
}
