package core.types;

/**
 * A 3x3 square matrix for 2D transformations
 * @author Jani Nykänen
 *
 */
public class Matrix3 {

	/** Matrix elements */
	private float 
		m11, m21, m31,
		m12, m22, m32,
		m13, m23, m33;
	
	
	/**
	 * Set matrix to an identity matrix
	 */
	public void identity() {
		
		m11 = 1; m21 = 0; m31 = 0;
	    m12 = 0; m22 = 1; m32 = 0;
	    m13 = 0; m23 = 0; m33 = 1;
	}
	
	
	/**
	 * Constructor, sets matrix to identity matrix
	 */
	public Matrix3() {
		
		identity();
	}
	
	
	/**
	 * Matrix multiplication
	 * @param M Right-side operand
	 * @return Result matrix
	 */
	public Matrix3 multiply(Matrix3 M) {

	    Matrix3 A = new Matrix3();

	    A.m11 = this.m11 * M.m11 + this.m21 * M.m12 + this.m31 * M.m13;
	    A.m21 = this.m11 * M.m21 + this.m21 * M.m22 + this.m31 * M.m23;
	    A.m31 = this.m11 * M.m31 + this.m21 * M.m32 + this.m31 * M.m33;

	    A.m12 = this.m12 * M.m11 + this.m22 * M.m12 + this.m32 * M.m13;
	    A.m22 = this.m12 * M.m21 + this.m22 * M.m22 + this.m32 * M.m23;
	    A.m32 = this.m12 * M.m31 + this.m22 * M.m32 + this.m32 * M.m33;

	    A.m13 = this.m13 * M.m11 + this.m23 * M.m12 + this.m33 * M.m13;
	    A.m23 = this.m13 * M.m21 + this.m23 * M.m22 + this.m33 * M.m23;
	    A.m33 = this.m13 * M.m31 + this.m23 * M.m32 + this.m33 * M.m33;

	    return A;
	}
	
	
	/**
	 * Set to translation matrix
	 * @param x X translation
	 * @param y Y translation
	 */
	public void translate(float x, float y) {
		
		this.m11 = 1; this.m21 = 0; this.m31 = x;
	    this.m12 = 0; this.m22 = 1; this.m32 = y;
	    this.m13 = 0; this.m23 = 0; this.m33 = 1;
	}
	
	
	/**
	 * Set to scaling matrix
	 * @param x X scaling
	 * @param y Y scaling
	 */
	public void scale(float x, float y) {
		
		this.m11 = x; this.m21 = 0; this.m31 = 0;
	    this.m12 = 0; this.m22 = y; this.m32 = 0;
	    this.m13 = 0; this.m23 = 0; this.m33 = 1;
	}
	
	
	/**
	 * Set to rotation matrix
	 * @param angle Angle
	 */
	public void scale(float angle) {
		
		float c = (float)Math.cos(angle);
	    float s = (float)Math.sin(angle);

	    this.m11 = c; this.m21 =-s; this.m31 = 0;
	    this.m12 = s; this.m22 = c; this.m32 = 0;
	    this.m13 = 0; this.m23 = 0; this.m33 = 1;
	}
	
	
	/**
	 * Set to 2D ortho projection matrix
	 * @param top Top
	 * @param left Left
	 * @param bottom Bottom
	 * @param right Right
	 */
	public void ortho2D(float left, float right, float bottom, float top) {
		
		float w = right - left;
	    float h = top - bottom;

	    this.m11 = 2.0f / w; this.m21 = 0; this.m31 = -(right+left)/w;
	    this.m12 = 0; this.m22 = -2.0f / h; this.m32 = (top+bottom)/h;
	    this.m13 = 0; this.m23 = 0; this.m33 = 1;
	}
	
	
	/**
	 * Convert to an-OpenGL-friendly array
	 * @return Array
	 */
	public float[] toArray() {
		
		return new float[] {
			this.m11 , this.m12 , this.m13,
		    this.m21 , this.m22 , this.m23,
		    this.m31 , this.m32 , this.m33,
		};
	}
}
