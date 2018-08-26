package application.gamefield;

import application.Gamepad;
import application.Scene;
import core.State;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
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

	/** Test position */
	private Vector2 testPos = new Vector2(400,360);
	
	/** Stage manager */
	private Stage stage;
	/** Time manager */
	private TimeManager timeMan;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Load test bitmap
		bmpFont = assets.getBitmap("font");
		
		// Initialzie components
		stage = new Stage(assets);
		stage.loadMap(1);
		timeMan = new TimeManager();

	}
	

	@Override
	public void update(Gamepad vpad, float tm) {
		
		final float MOVE_TIMER = 12.0f;

		// Update test position
		Vector2 stick = vpad.getStick();
		testPos.x += stick.x * MOVE_TIMER * tm;
		testPos.y += stick.y * MOVE_TIMER * tm;
		
		// Test
		if(vpad.getButtonByName("fire1") == State.Pressed) {
			
			System.out.println("Beep!");
		}
		
		// Update components
		stage.update(tm);
		timeMan.update(tm);
		
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
