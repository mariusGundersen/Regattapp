package net.mariusgundersen.android.metaRacing.watchInterface;

import android.graphics.Bitmap;
import android.graphics.Color;

public class BitmapUtil {

	private static final byte WHITE_PIXEL_BIT = 0x0;
	private static final byte BLACK_PIXEL_BIT = 0x1;

	private BitmapUtil() {
	}

	public static int[] bitmapToPixelArray(Bitmap bitmap) {
		int pixels[] = new int[ApolloConfig.DISPLAY_WIDTH
				* ApolloConfig.DISPLAY_HEIGHT];
		for (int x = 0; x < ApolloConfig.DISPLAY_WIDTH; x++) {
			for (int y = 0; y < ApolloConfig.DISPLAY_HEIGHT; y++) {
				int color = Color.BLACK;
				if (x < bitmap.getWidth() && y < bitmap.getHeight()) {
					color = bitmap.getPixel(x, y);
				}
				color = (Math.abs(Color.BLACK - color) < Math.abs(Color.WHITE
						- color)) ? Color.BLACK : Color.WHITE;
				pixels[(y * ApolloConfig.DISPLAY_WIDTH) + x] = color;
			}
		}
		return pixels;
	}

	public static byte[] bitmapToBuffer(Bitmap bitmap) {
		byte buffer[] = new byte[(ApolloConfig.DISPLAY_WIDTH * ApolloConfig.DISPLAY_HEIGHT) / 8];
		for (int x = 0; x < ApolloConfig.DISPLAY_WIDTH; x++) {
			for (int y = 0; y < ApolloConfig.DISPLAY_HEIGHT; y++) {
				int color = Color.BLACK;
				if (x < bitmap.getWidth() && y < bitmap.getHeight()) {
					color = bitmap.getPixel(x, y);
				}
				byte pixelBit = (Math.abs(Color.BLACK - color) < Math
						.abs(Color.WHITE - color)) ? BLACK_PIXEL_BIT
						: WHITE_PIXEL_BIT;
				int pixelIndex = (y * ApolloConfig.DISPLAY_WIDTH) + x;
				int byteIndex = pixelIndex / 8;
				buffer[byteIndex] = (byte) (buffer[byteIndex] | (pixelBit << (pixelIndex % 8)));
			}
		}
		return buffer;
	}

	public static Bitmap bufferToBitmap(byte[] buffer) {
		Bitmap bitmap = Bitmap.createBitmap(ApolloConfig.DISPLAY_WIDTH,
				ApolloConfig.DISPLAY_HEIGHT, Bitmap.Config.RGB_565);

		for (int i = 0; i < buffer.length; i++) {
			for (int j = 0; j < 8; j++) {
				int pixelIndex = (i * 8) + j;
				int x = pixelIndex % ApolloConfig.DISPLAY_WIDTH;
				int y = pixelIndex / ApolloConfig.DISPLAY_WIDTH;
				if (((buffer[i] >> j) & 1) == WHITE_PIXEL_BIT) {
					bitmap.setPixel(x, y, Color.WHITE);
				} else {
					bitmap.setPixel(x, y, Color.BLACK);
				}
			}
		}

		return bitmap;
	}

}
