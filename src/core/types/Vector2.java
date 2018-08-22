package core.types;

/**
 * 2D vector
 * @author Jani Nykänen
 *
 */
public class Vector2 implements Cloneable {

	
	/** Components */
	public float x, y;
	
	
	/**
	 * Constructor
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Vector2(float x, float y) {
		
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Constructor
	 */
	public Vector2() {
		
		this(0, 0);
	}
	
	
	/**
	 * Add a vector to this vector
	 * @param v Vector to be added
	 * @return The sum vector
	 */
	public Vector2 add(Vector2 v) {
		
		return new Vector2(x + v.x, y + v.y);
	}
	
	
	/**
	 * Clone
	 */
	public Vector2 clone() {
		
		return new Vector2(x, y);
	}
}
