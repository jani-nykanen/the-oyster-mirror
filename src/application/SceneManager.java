package application;

import java.util.ArrayList;
import java.util.List;

import core.InputManager;
import core.renderer.Graphics;

/**
 * A scene manager. Handles scene
 * related things, like adding and
 * changing scenes
 * @author Jani Nykänen
 *
 */
public class SceneManager implements Scene {

	/** A list of scenes */
	private List<Scene> scenes;
	
	/** Current scene */
	private Scene current;
	
	/** Global scene */
	private Scene global;
	
	
	/**
	 * Constructor
	 */
	public SceneManager() {
		
		// Initialize components
		scenes = new ArrayList<Scene> ();
		current = null;
		global = null;
	}
	
	
	/**
	 * Add a scene
	 * @param s Scene to be added
	 * @param makeCurrent Whether to make the scene
	 * the current scene
	 */
	public void addScene(Scene s, boolean makeCurrent) {
		
		scenes.add(s);
		if(makeCurrent || current == null) {
			
			current = s;
		}
	}
	
	
	/**
	 * Add a scene
	 * @param s Scene to be added
	 */
	public void addScene(Scene s) {
		
		addScene(s, false);
	}
	
	
	/**
	 * Add the global scene. Can be done
	 * only once, after that does nothing.
	 * @param s Scene
	 */
	public void addGlobalScene(Scene s) {
		
		if(global == null) {
			
			global = s;
			addScene(global);
		}
	}


	@Override
	public void init() {
		
		// Initialize all the scenes
		for(Scene s : scenes) {
			
			s.init();
		}
	}


	@Override
	public void update(InputManager input, float tm) {
		
		// Update the current and global scenes, if not null
		if(current != null)
			current.update(input, tm);
		
		if(global != null)
			global.update(input, tm);
	}


	@Override
	public void draw(Graphics g) {
		
		// Draw the current and global scenes, if not null
		if(current != null)
			current.draw(g);
		
		if(global != null)
			global.draw(g);
	}


	@Override
	public void destroy() {
		
		// Destroy all the scenes
		for(Scene s : scenes) {
			
			s.destroy();
		}
	}


	@Override
	public void changeTo() { }
}
