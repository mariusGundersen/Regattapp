package net.mariusgundersen.android.metaRacing.watchInterface;

/**
 * Defines aspects of Apollo.
 */

public class ApolloConfig {
	public static final int DISPLAY_WIDTH = 96;
	public static final int DISPLAY_HEIGHT = 96;

	public enum Button {
		TOP_RIGHT((byte) 0), MIDDLE_RIGHT((byte) 1), BOTTOM_RIGHT((byte) 2), TOP_LEFT(
				(byte) 6), MIDDLE_LEFT((byte) 5), BOTTOM_LEFT((byte) 3);

		private final byte value;

		private Button(byte value) {
			this.value = value;
		}

		public byte value() {
			return value;
		}
	}

	private ApolloConfig() {
	}
}
