package net.mariusgundersen.android.metaRacing.watchInterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

public class NotificationRenderer {

	private Paint metawatch11px;
	private Paint metawatch7px;
	private Paint metawatch5px;

	public NotificationRenderer(Context context) {
		createFontPainters(context);
	}

	private void createFontPainters(Context context) {
		Typeface fontMetawatch11px = Typeface.createFromAsset(
				context.getAssets(), "fonts/metawatch_16pt_11pxl_proto1.ttf");
		metawatch11px = new Paint();
		metawatch11px.setTypeface(fontMetawatch11px);
		metawatch11px.setTextSize(16);

		Typeface fontMetawatch7px = Typeface
				.createFromAsset(context.getAssets(),
						"fonts/metawatch_8pt_7pxl_CAPS_proto1.ttf");
		metawatch7px = new Paint();
		metawatch7px.setTypeface(fontMetawatch7px);
		metawatch7px.setTextSize(8);

		Typeface fontMetawatch5px = Typeface
				.createFromAsset(context.getAssets(),
						"fonts/metawatch_8pt_5pxl_CAPS_proto1.ttf");
		metawatch5px = new Paint();
		metawatch5px.setTypeface(fontMetawatch5px);
		metawatch5px.setTextSize(8);
	}
	
	public void clearCanvas(Canvas canvas){
		canvas.drawColor(Color.WHITE);
	}

	public void renderTextToCanvas(String text, Canvas canvas){
		String[] lines = text.split("\n");
		canvas.drawColor(Color.WHITE);
		for(int i=0; i<lines.length; i++){
			renderText(canvas, lines[i], 0, new Rect(4, 16*i+16, 96, 96), Paint.Align.LEFT);
		}
	}

	public Bitmap renderText(String text) {
		Bitmap result = Bitmap.createBitmap(ApolloConfig.DISPLAY_WIDTH,
				ApolloConfig.DISPLAY_HEIGHT, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(result);
		canvas.drawColor(Color.WHITE);
		String[] lines = text.split("\n");
		for(int i=0; i<lines.length; i++){
			renderText(canvas, lines[i], 0, new Rect(4, 16*i+16, 96, 96), Paint.Align.LEFT);
		}
		return result;
	}

	private void renderText(Canvas canvas, String text, int fontSize,
			Rect bounds, Paint.Align align) {
		if (bounds == null) {
			bounds = new Rect(0, 0, ApolloConfig.DISPLAY_WIDTH,
					ApolloConfig.DISPLAY_HEIGHT);
		}

		// TODO: Use fontSize and adjust font size to fit text?
		Paint textPaint = metawatch11px;
		textPaint.setTextAlign(align);

		canvas.drawText(text, bounds.left, bounds.top, textPaint);

	}

	public void renderTextToCanvas(String text, Canvas canvas, Rect bounds, Paint.Align align) {
		Log.d("RENDER", text);
		renderText(canvas, text, 11, bounds, align);
	}

}
