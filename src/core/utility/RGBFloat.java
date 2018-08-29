package core.utility;

/**
 * Red-green-blue color,
 * range [0.0f, 1.0f]
 * @author Jani Nykänen
 *
 */
public class RGBFloat {

	/** Components */
	public float r, g, b;
	
	
	/**
	 * Constructor
	 * @param r Red
	 * @param g Green
	 * @param b Blue
	 */
	public RGBFloat(float r, float g, float b) {
		
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	
	/**
	 * Constructor
	 */
	public RGBFloat() {
		
		this(1, 1, 1);
	}
}
