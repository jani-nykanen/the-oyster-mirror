package application.gamefield;

import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Vector2;

/**
 * Fading
 * @author Jani Nykänen
 *
 */
public class Fading {

	/** Position */
	private Vector2 pos;
	/** Timer */
	private float timer;
	/** Initial time */
	private float initialTime;
	/** Does exist */
	private boolean exist;
	
	
	/**
	 * Constructor
	 */
	public Fading() {
		
		pos = new Vector2();
		exist = false;
	}
	
	
	/**
	 * Update a fading
	 * @param tm Time mul.
	 */
	public void update(float tm) {
		
		if(!exist) return;
		
		timer -= 1.0f * tm;
		if(timer <= 0.0f) {
			
			exist = false;
		}
	}
	
	
	/**
	 * Draw a fading
	 * @param g Graphics object
	 * @param bmp Bitmap
	 * @param sx Source x
	 * @param sy Source y
	 * @param sw Source width
	 * @param sh Source height
	 * @param dw Destination width
	 * @param dh Destination height
	 */
	public void draw(Graphics g, Bitmap bmp, int sx, int sy, int sw, int sh, float dw, float dh) {
		
		final float BASE_ALPHA = 0.5f;
		
		if(!exist) return;
		
		float alpha = BASE_ALPHA * timer / initialTime;
		g.setColor(1, 1, 1, alpha);
		g.drawScaledBitmapRegion(bmp, sx, sy, sw, sh, pos.x, pos.y, dw, dh, Flip.NONE);
	}
	
	
	/**
	 * Create a fading
	 * @param pos Position
	 * @param time Initial time
	 */
	public void create(Vector2 pos, float time) {
		
		this.pos = pos.clone();
		timer = time;
		initialTime = time;
		exist = true;
	}
	
	
	/**
	 * Check if the fading exist
	 * @return True, if exists
	 */
	public boolean doesExist() {
		
		return exist;
	}
}
