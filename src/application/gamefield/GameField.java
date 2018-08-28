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
	/** Status manager */
	private StatusManager statMan;
	
	
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
		
		// Create status manager
		StatusManager.init(assets);
		statMan = new StatusManager();
		statMan.setInitial(stage);
	}
	

	@Override
	public void update(Gamepad vpad, float tm) {
		
		// Update stage
		stage.update(tm);
		// Update time manager
		timeMan.update(tm);
		// Update game objects
		objMan.update(vpad, timeMan, stage, statMan, tm);
		
	}
	

	@Override
	public void draw(Graphics g) {
		
		// TODO: Put this elsewhere
		final float DEFAULT_VIEW_HEIGHT = 720.0f;
		
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
		tr.fitViewHeight(DEFAULT_VIEW_HEIGHT);
		tr.identity();
		tr.use();
		
		// Draw status manager
		statMan.draw(g);
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
