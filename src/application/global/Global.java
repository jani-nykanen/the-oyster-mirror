package application.global;

import application.Gamepad;
import application.Scene;
import core.renderer.Graphics;
import core.utility.AssetPack;

/**
 * The global scene
 * @author Jani Nykänen
 *
 */
public class Global extends Scene {

	/** Scene name */
	public String name = "game";
	
	/** Transition object */
	private Transition trans;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Create global components
		trans = new Transition();
	}

	
	@Override
	public void update(Gamepad vpad, float tm) {
		
		// Update transitions
		trans.update(tm);
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
}
