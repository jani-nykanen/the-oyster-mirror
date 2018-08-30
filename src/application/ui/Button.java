package application.ui;

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
	 */
	public Button() {
		
		this("", null);
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
	 * Call the callback function
	 */
	public void activate() {
		
		if(cb != null)
			cb.execute();
	}
	
	
	@Override
	public Button clone() {
		
		return new Button(text, cb);
	}
}
