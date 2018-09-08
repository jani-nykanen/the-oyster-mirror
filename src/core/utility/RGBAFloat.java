package core.utility;

/**
 * Red-green-blue color,
 * range [0.0f, 1.0f]
 * @author Jani Nykänen
 *
 */
public class RGBAFloat {

	/** Components */
	public float r, g, b, a;
	
	
	/**
	 * Constructor
	 * @param r Red
	 * @param g Green
	 * @param b Blue
	 * @param a Alpha
	 */
	public RGBAFloat(float r, float g, float b, float a) {
		
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	
	/**
	 * Constructor
	 * @param r Red
	 * @param g Green
	 * @param b Blue
	 */
	public RGBAFloat(float r, float g, float b) {
		
		this(r,g,b,1.0f);
	}
	
	
	/**
	 * Constructor
	 */
	public RGBAFloat() {
		
		this(1, 1, 1, 1);
	}
}
