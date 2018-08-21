package core.renderer;

/**
 * Flipping flags. Note that we cannot
 * use enum since we need bitwise operations, and
 * in Java you cannot set numeric value to enum.
 * @author Jani Nykänen
 *
 */
public final class Flip {

	/** Flags */
	public static int NONE = 0;
	public static int HORIZONTAL = 1;
	public static int VERTICAL = 2;
	public static int BOTH = 1 | 2;
}
