package net.mariusgundersen.android.metaRacing.watchInterface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MetaWatchApplication {

	private final Context context;

	public MetaWatchApplication(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public void sendApplicationText(String text) {

		Intent broadcast = new Intent(
				"org.metawatch.manager.APPLICATION_UPDATE");
		Bundle b = new Bundle();

		byte[] image = BitmapUtil.bitmapToBuffer(new NotificationRenderer(context).renderText(text));

		b.putByteArray("buffer", image);

		broadcast.putExtras(b);

		context.sendBroadcast(broadcast);
	}

	public void startApp() {
		Intent broadcast = new Intent("org.metawatch.manager.APPLICATION_START");
		context.sendBroadcast(broadcast);

	}

	public void stopApp() {

		Intent broadcast = new Intent("org.metawatch.manager.APPLICATION_STOP");
		context.sendBroadcast(broadcast);
	}
}
