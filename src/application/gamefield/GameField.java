package application.gamefield;

import application.Scene;
import core.InputManager;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;

/**
 * A game field scene.
 * @author Jani Nykänen
 *
 */
public class GameField implements Scene {

	/** Test bitmap */
	private Bitmap bmpTest;
	
	/** Test timer */
	private float timer = 0.0f;
	
	
	@Override
	public void init() {
		
		// Load test bitmap
		bmpTest = new Bitmap("/assets/bitmaps/test.png");
		
	}
	

	@Override
	public void update(InputManager input, float tm) {
		
		final float TIMER_SPEED = 0.05f;
		
		timer += TIMER_SPEED * tm;
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
		tr.translate(320.0f + (1.0f+s1)*128.0f, 240.0f);
		tr.rotate(timer);
		tr.scale(2.0f+s2,2.0f+s2);
		tr.translate(-64, -64);
		
		tr.use();
		
		g.setColor(1,1,1,1);
		g.fillRect(-16, -16, 128+32, 128+32);
		
		g.drawScaledBitmapRegion(bmpTest, 0, 0, 256, 256, 0, 0, 128, 128, Flip.NONE);
		
		
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
