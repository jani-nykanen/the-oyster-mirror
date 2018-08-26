package core.types;

/**
 * 2D point
 * @author Jani Nykänen
 *
 */
public class Point implements Cloneable {

	
	/** Components */
	public int x, y;
	
	
	/**
	 * Constructor
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Point(int x, int y) {
		
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Constructor
	 */
	public Point() {
		
		this(0, 0);
	}
	
	
	/**
	 * Add a vector to this vector
	 * @param v Vector to be added
	 * @return The sum vector
	 */
	public Point add(Point v) {
		
		return new Point(x + v.x, y + v.y);
	}
	
	
	/**
	 * Clone
	 */
	public Point clone() {
		
		return new Point(x, y);
	}
	
	
	@Override
	public boolean equals(Object o) {
		
		Point p = (Point)o;
		return p.x == x && p.y == y;
	}
}
