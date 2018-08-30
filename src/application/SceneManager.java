package application;

import java.util.ArrayList;
import java.util.List;

import core.renderer.Graphics;
import core.utility.AssetPack;


/**
 * A scene manager. Handles scene
 * related things, like adding and
 * changing scenes
 * @author Jani Nykänen
 *
 */
public class SceneManager extends Scene {

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
		
		s.setSceneManager(this);
		s.setWeakEventManager(eventMan);
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
	
	
	/**
	 * Change to a scene
	 * @param name Name of the new scene
	 */
	public void changeScene(String name) {
		
		// Find a scene with the same name
		for(Scene s : scenes) {
			
			if(s.getName() == name) {
				
				current = s;
				s.changeTo();
				return;
			}
		}
 	}

	
	/**
	 * Get the global scene
	 * @return Global scene
	 */
	public Scene getGlobalScene() {
		
		return global;
	}
	

	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Initialize all the scenes
		for(Scene s : scenes) {
			
			s.init(assets);
		}
	}


	@Override
	public void update(Gamepad vpad, float tm) {
		
		// Update the current and global scenes, if not null
		if(current != null)
			current.update(vpad, tm);
		
		if(global != null)
			global.update(vpad, tm);
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
