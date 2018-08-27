package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Point;
import core.types.Vector2;
import core.utility.AssetPack;

/**
 * Player object
 * @author Jani Nykänen
 *
 */
public class Player extends FieldObject {

	/** Player bitmap */
	static private Bitmap bmpPlayer;
	
	/** Outline opaqueness factor" */
	private float outlineFactor = 0.0f;
	
	/** Eye position */
	private Vector2 eyePos;
	
	
	/**
	 * Initialize global player content
	 * @param assets Asset pack
	 */
	static public void init(AssetPack assets) {
	
		// Get global bitmaps
		bmpPlayer = assets.getBitmap("player");
	}
	
	
	/**
	 * Control the player
	 * @param vpad Game pad
	 * @param tman Time manager
	 * @param stage Stage
	 */
	private void control(Gamepad vpad, TimeManager tman, Stage stage) {
	
		final float STICK_DELTA = 0.5f;
		
		if(tman.isWaiting()) return;
		
		// Get stick
		Vector2 stick = vpad.getStick();
		Point t = pos.clone();
		// Horizontal movement
		if(stick.x > STICK_DELTA) {
			
			++ t.x;
		}
		else if(stick.x < -STICK_DELTA) {
			
			-- t.x;
		}
		// Vertical movement
		else if(stick.y > STICK_DELTA) {
			
			++ t.y;
		}
		else if(stick.y < -STICK_DELTA) {
			
			-- t.y;
		}
		
		boolean doMove = true;
		// If no movement has happened
		if(t.x == pos.x && t.y == pos.y) {
			
			doMove = false;
		}
		
		// Check if not out of bounds
		else if(t.x < 0 || t.y < 0 
			|| t.x >= stage.getWidth() || t.y >= stage.getHeight()) {
			
			doMove = false;
		}
		
		// Check if a free tile
		// ...
		
		// If no obstacles, move
		if(doMove) {
			
			target.x = t.x;
			target.y = t.y;
			
			tman.setTimer(true);
			moving = true;

		}
		
	}
	
	
	/**
	 * Move eyes
	 * @param tm Time mul-
	 */
	private void moveEyes(float tm) {
		
		final float SPEED_FACTOR = 3.6f;
		final float MIN_DIST = 1.0f;
		
		// Calculate angle & distance
		float angle = (float)Math.atan2(vpos.y-eyePos.y, vpos.x-eyePos.x);
		float dist = (float)Math.hypot(vpos.x-eyePos.x, vpos.y-eyePos.y);
		
		// If distance is high enough, move eyes
		if(dist > MIN_DIST) {
			
			dist -= MIN_DIST;
			float mod = dist / SPEED_FACTOR;
			eyePos.x += (float)Math.cos(angle) * mod * tm;
			eyePos.y += (float)Math.sin(angle) * mod * tm;
		
		}
	}
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public Player(Point pos) {
		
		super(pos);
		
		eyePos = new Vector2();
		eyePos = vpos.clone();
	}
	

	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {
		
		final float OUTLINE_TIMER_SPEED = 0.025f;
		
		// Update "outline factor"
		outlineFactor += OUTLINE_TIMER_SPEED * tm;
		
		// Control
		control(vpad, tman, stage);
		
		// Move eyes
		moveEyes(tm);
	}
	

	@Override
	public void draw(Graphics g) {
		
		final float OUTLINE_OPAQUE_DELTA = 0.125f;
		final float OUTLINE_OPAQUE_START = 0.5f + OUTLINE_OPAQUE_DELTA;
		
		// Green outline
		g.setColor(0.5f, 1, 0, 
				OUTLINE_OPAQUE_START + (float)Math.sin(outlineFactor) * OUTLINE_OPAQUE_DELTA);
		g.fillRect(vpos.x-8, vpos.y-8, scaleValue.x+16, scaleValue.y+16);
		g.setColor();
		
		// Base block
		g.drawScaledBitmapRegion(bmpPlayer, 0, 0, 128, 128,
				vpos.x, vpos.y, scaleValue.x, scaleValue.y, Flip.NONE);
		
		// Eyes
		g.drawScaledBitmapRegion(bmpPlayer, 128, 0, 128, 128,
				eyePos.x, eyePos.y, scaleValue.x, scaleValue.y, Flip.NONE);
	}

}
