package application.gamefield;

import application.Gamepad;
import application.Scene;
import core.InputManager;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;

/**
 * A game field scene.
 * @author Jani Nykänen
 *
 */
public class GameField implements Scene {

	/** Test bitmap */
	private Bitmap bmpTest;
	/** Test font */
	private Bitmap bmpFont;
	
	/** Test timer */
	private float timer = 0.0f;
	
	/** Test position */
	private Vector2 testPos = new Vector2(400,360);
	
	
	@Override
	public void init() throws Exception {
		
		// Load test bitmap
		bmpTest = new Bitmap("assets/bitmaps/test.png");
		bmpFont = new Bitmap("assets/bitmaps/font.png");
		
	}
	

	@Override
	public void update(Gamepad vpad, float tm) {
		
		final float TIMER_SPEED = 0.05f;
		final float MOVE_TIMER = 12.0f;
		
		// Update timer
		timer += TIMER_SPEED * tm;
		
		// Update test position
		Vector2 stick = vpad.getStick();
		testPos.x += stick.x * MOVE_TIMER * tm;
		testPos.y += stick.y * MOVE_TIMER * tm;
		
	}
	

	@Override
	public void draw(Graphics g) {
		
		// Clear screen
		g.clearScreen(0.67f, 0.67f, 0.67f);
		
		// Set transformations
		float s1 = (float)Math.sin(timer);
		float s2 = (float)Math.sin(timer/2.0f);
		Transformations tr = g.transform();
		tr.fitViewHeight(720.0f);
		tr.identity();
		
		g.setColor();
		g.drawBitmap(bmpTest, 32, 32, Flip.VERTICAL);
		
		tr.push();
		tr.translate(320.0f + (1.0f+s1)*128.0f, 240.0f);
		tr.rotate(timer);
		tr.scale(2.0f+s2,2.0f+s2);
		tr.translate(-64, -64);
		
		tr.use();
		
		g.setColor(0.0f, 1.0f, 0.0f, 0.67f);
		g.drawScaledBitmap(bmpTest, 0, 0, 128, 128, Flip.NONE);
		
		tr.pop();
		
		g.setColor(1,0,1);
		g.drawBitmapRegion(bmpTest,16,16,128,64, 256, 32, Flip.BOTH);
	
		g.setColor(0.5f, 0, 1.0f, 0.67f);
		g.fillRect(testPos.x-32, testPos.y-32, 64, 64);
		g.setColor();
		
		g.drawText(bmpFont, "Hello world!", 8, 8, -16, 0, false);
		g.drawText(bmpFont, "World, hello!", 256, 72, -16, 0, true, 0.75f);
		
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
