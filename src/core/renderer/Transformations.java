package core.renderer;

import java.util.Stack;

import core.types.Matrix3;
import core.types.Vector2;

/**
 * Handles 2D transformations
 * @author Jani Nykänen
 *
 */
public class Transformations {

	/** Model matrix */
	private Matrix3 model;
	/** View matrix */
	private Matrix3 view;
	/** Operand matrix */
	private Matrix3 operand;
	
	/** Viewport dimensions */
	private Vector2 viewport;
	/** Framebuffer size */
	private Vector2 frameBufferSize;
	
	/** A stack of matrices */
	private Stack<Matrix3> stack; 
	
	
	/**
	 * Constructor
	 */
	public Transformations() {
		
		// Create matrices
		model = new Matrix3();
		view = new Matrix3();
		operand = new Matrix3();
		
		// Initialize other components
		stack = new Stack<Matrix3> ();
		viewport = new Vector2(1, 1);
		frameBufferSize = new Vector2(1, 1);
	}
	
	
	/**
	 * Set view
	 * @param w View width
	 * @param h View height
	 */
	public void setView(float w, float h) {
		
		view.ortho2D(0, w, 0, h);
	    viewport.x = w;
	    viewport.y = h;
	}
	
	
	/**
	 * Fit the view size to the view height
	 * @param h View height
	 */
	public void fitViewHeight(float h) {
		
		float ratio = frameBufferSize.x / frameBufferSize.y;
		float w = ratio * h;
		
		setView(w, h);
	}
}
