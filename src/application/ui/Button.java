package application.ui;

import core.types.Direction;
import core.utility.VoidCallback;

/**
 * A button
 * @author Jani Nykänen
 *
 */
public class Button implements Cloneable {

	/** Text */
	private String text;
	
	/** Is disabled */
	private boolean disabled = false;
	
	/** Called when "pressed" */
	private VoidCallback cb = null;
	
	/** Directional callback, "Left" */
	private VoidCallback leftCb = null;
	/** Directional callback, "Right" */
	private VoidCallback rightCb = null;
	
	
	/**
	 * Constructor
	 * @param text Text
	 * @param cb Callback
	 */
	public Button(String text, VoidCallback cb) {
		
		this.text = text;
		this.cb = cb;
		disabled = false;
	}
	
	
	/**
	 * Constructor
	 * @param text Text
	 */
	public Button(String text) {
		
		this(text, null);
	}
	
	/**
	 * Constructor
	 * @param text Text
	 * @param left Left callback
	 * @param right Right callback
	 */
	public Button(String text, VoidCallback left, VoidCallback right) {
		
		this.text = text;
		leftCb = left;
		rightCb = right;
		disabled = false;
	}
	
	
	/**
	 * Constructor
	 */
	public Button() {
		
		this("", null);
	}
	
	
	/**
	 * Constructor
	 * @param text Text
	 * @param cb Press callback
	 * @param left Left callback
	 * @param right Right callback
	 */
	public Button(String text, VoidCallback cb, VoidCallback left, VoidCallback right) {
		
		this.text = text;
		this.cb = cb;
		leftCb = left;
		rightCb = right;
		disabled = false;
	}


	/**
	 * Disable button
	 */
	public void disable() {
		
		disabled = true;
	}
	
	
	/**
	 * Enable
	 */
	public void enable() {
		
		disabled = false;
	}
	
	
	/**
	 * Is the button disabled
	 * @return True, if disabled
	 */
	public boolean isDisabled() {
		
		return disabled;
	}
	
	
	/**
	 * Set a new text
	 * @param text New text
	 */
	public void setText(String text) {
		
		this.text = text;
	}
	
	
	/**
	 * Get text
	 * @return Text
	 */
	public String getText() {
		
		return text;
	}
	
	
	/**
	 * Set a new callback function
	 * @param cb New callback function
	 */
	public void setCallback(VoidCallback cb) {
		
		this.cb = cb;
	}
	
	
	/**
	 * Set directional callbacks
	 * @param left Left callback
	 * @param right Right callback
	 */
	public void setDirectionalCallbacks(VoidCallback left, VoidCallback right) {
		
		leftCb = left;
		rightCb = right;
	}
	
	
	/**
	 * Call the callback function
	 */
	public void activate() {
		
		if(cb != null)
			cb.execute();
	}
	
	
	/**
	 * Activate directional callback
	 * @param dir Direction
	 */
	public void activate(Direction dir) {
		
		if(dir == Direction.Left && leftCb != null)
			leftCb.execute();
		
		else if(dir == Direction.Right && rightCb != null)
			rightCb.execute();
	}
	
	
	@Override
	public Button clone() {
		
		return new Button(text, cb);
	}
}
