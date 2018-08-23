package application;

import core.renderer.Graphics;

/**
 * A scene class.
 * @author Jani Nykänen
 *
 */
public abstract class Scene {

	/** Scene name */
	private String name = "";
	
	/** Scene manager reference */
	protected SceneManager sceneMan;
	
	
	/** Initialize 
	 * @throws Exception */
	public abstract void init() throws Exception;
	
	
	/**
	 * Update frame
	 * @param input Input manager
	 * @param tm Time multiplier
	 */
	public abstract void update(Gamepad gamepad, float tm); 
	
	
	/** 
	 * Draw frame 
	 * @param g Graphics object 
	 */
	public abstract void draw(Graphics g);
	
	
	/** Destroy scene */
	public abstract void destroy();
	
	
	/** Change to to this scene */
	public abstract void changeTo();
	
	
	/**
	 * Get scene name
	 * @return Name
	 */
	public String getName() {
		
		return name;
	}
	
	
	/**
	 * Set scene manager reference
	 * @param s Scene manager
	 */
	public void setSceneManager(SceneManager s) {
		
		sceneMan = s;
	}
}
