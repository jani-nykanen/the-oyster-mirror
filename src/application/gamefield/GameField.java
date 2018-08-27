package application.gamefield;

import application.Gamepad;
import application.Scene;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.utility.AssetPack;


/**
 * A game field scene.
 * @author Jani Nykänen
 *
 */
public class GameField extends Scene {

	/** Scene name */
	public String name = "game";
	
	/** Test font */
	private Bitmap bmpFont;
	
	/** Stage manager */
	private Stage stage;
	/** Time manager */
	private TimeManager timeMan;
	/** Object manager */
	private ObjectManager objMan;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Load test bitmap
		bmpFont = assets.getBitmap("font");
		
		// Create stage
		stage = new Stage(assets);
		stage.loadMap(1);
		// Initialize object manager
		ObjectManager.init(assets);
		// Create object manager & objects
		objMan = new ObjectManager();
		stage.createObjects(objMan);
		
		// Create time manager
		timeMan = new TimeManager();
	}
	

	@Override
	public void update(Gamepad vpad, float tm) {
		
		// Update stage
		stage.update(tm);
		// Update time manager
		timeMan.update(tm);
		// Update game objects
		objMan.update(vpad, timeMan, stage, tm);
		
	}
	

	@Override
	public void draw(Graphics g) {
		
		// Clear screen
		g.clearScreen(1,1,1);
		
		// Set transformations
		Transformations tr = g.transform();

		// Draw stage
		stage.setTransform(g);
		stage.draw(g);
		
		// Draw game objects
		objMan.draw(g);
		
		// Hello world!
		tr.fitViewHeight(720.0f);
		tr.identity();
		tr.use();
		g.setColor(1,1,0);
		g.drawText(bmpFont, "Hello world!", 8, 8, -16, 0, false, 0.75f);
		g.setColor();
	}
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void changeTo() {
		// TODO Auto-generated method stub
		
	}

}
