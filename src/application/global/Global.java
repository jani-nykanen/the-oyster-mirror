package application.global;

import application.Gamepad;
import application.Scene;
import core.State;
import core.renderer.Graphics;
import core.utility.AssetPack;

/**
 * The global scene
 * @author Jani Nykänen
 *
 */
public class Global extends Scene {

	/** Default view height */
	public static final float DEFAULT_VIEW_HEIGHT = 720.0f;
	/** Default save game output path */
	public static final String SAVE_DATA_PATH = "savegame.dat";
	
	/** Scene name */
	public String name = "game";
	
	/** Transition object */
	private Transition trans;
	/** Save manager */
	private SaveManager saveMan;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Create global components
		trans = new Transition();
		saveMan = new SaveManager();
		
		// Load settings if exists
		try {
			
			saveMan.loadGame(SAVE_DATA_PATH);
			(new Settings(eventMan)).load();
		}
		catch(Exception e) { }
	}

	
	@Override
	public void update(Gamepad vpad, float tm) {
		
		// Update transitions
		trans.update(tm);
		
		// TODO Remove
		if(vpad.getButtonByName("_debug") == State.Pressed) {
			
			sceneMan.changeScene("ending", new int[] {1});
		}
	}
	

	@Override
	public void draw(Graphics g) {
		
		// Draw transition
		trans.draw(g);
	}
	

	@Override
	public void destroy() {
		
		// ...
	}

	
	@Override
	public void changeTo() {
		
		// ...
	}

	
	/**
	 * Get global transition manager
	 * @return Transition manager
	 */
	public Transition getTransition() {
		
		return trans;
	}
	
	
	/**
	 * Get global save manager
	 * @return Save manager
	 */
	public SaveManager getSaveManager() {
		
		return saveMan;
	}
}
