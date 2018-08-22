package application;

import core.renderer.Graphics;

/**
 * Application scene
 * @author Jani Nykänen
 *
 */
public interface Scene {

	/** Scene name */
	public final String name = "";
	
	
	/** Initialize 
	 * @throws Exception */
	public void init() throws Exception;
	
	
	/**
	 * Update frame
	 * @param input Input manager
	 * @param tm Time multiplier
	 */
	public void update(Gamepad gamepad, float tm); 
	
	
	/** 
	 * Draw frame 
	 * @param g Graphics object 
	 */
	public void draw(Graphics g);
	
	
	/** Destroy scene */
	public void destroy();
	
	
	/** Change to to this scene */
	public void changeTo();
}
