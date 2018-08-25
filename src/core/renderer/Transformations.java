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
	
	/** Active shader */
	private Shader activeShader;
	
	
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
	
	
	/**
	 * Set model matrix to identity
	 */
	public void identity() {
		
		model.identity();
	}
	
	
	/**
	 * Translate model space
	 * @param x X translation
	 * @param y Y translation
	 */
	public void translate(float x, float y) {
		
		operand.translate(x, y);
		model = model.multiply(operand);
	}
	
	
	/**
	 * Scale model space
	 * @param x X scaling
	 * @param y Y scaling
	 */
	public void scale(float x, float y) {
		
		operand.scale(x, y);
		model = model.multiply(operand);
	}
	
	
	/**
	 * Rotate model space
	 * @param angle Angle
	 */
	public void rotate(float angle) {
		
		operand.rotate(angle);
		model = model.multiply(operand);
	}
	
	
	/**
	 * Push current model matrix to a stack
	 */
	public void push() {
		
		stack.push(model.clone());
	}
	
	
	/**
	 * Pop model stack
	 */
	public void pop() {
		
		model = stack.pop().clone();
		use();
	}
	
	
	/**
	 * Use the current transformations
	 * @param shader Shader
	 */
	public void use() {
		
		activeShader.setTransformationUniforms(model, view);
	}
	
	
	/**
	 * Update the framebuffer size info
	 * @param w
	 * @param h
	 */
	public void updateFrameBufferSize(int w, int h) {
		
		frameBufferSize.x = w;
		frameBufferSize.y = h;
	}
	
	
	/**
	 * Set active shader
	 * @param sh Shader
	 */
	public void bindShader(Shader sh) {
		
		activeShader = sh;
	}
	
	
	/**
	 * Get viewport dimensions
	 * @return Viewport dimensions
	 */
	public Vector2 getViewport() {
		
		return viewport;
	}
}
