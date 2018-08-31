package application;

import core.WeakEventManager;
import core.renderer.Graphics;
import core.utility.AssetPack;

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
	/** Weak event manager reference */
	protected WeakEventManager eventMan;
	
	
	/** Initialize 
	 * @throws Exception */
	public abstract void init(AssetPack assets) throws Exception;
	
	
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
	
	
	/**
	 * Set a reference to the weak event manager
	 * @param e Event manager
	 */
	public void setWeakEventManager(WeakEventManager e) {
		
		eventMan = e;
	}
	
	
	/**
	 * Get the reference to the weak event manager.
	 * @return
	 */
	public WeakEventManager getWeakEventManager() {
		
		return eventMan;
	}
}
