package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Direction;
import core.types.Point;

/**
 * A field object that is *not* a player object
 * @author Jani Nykänen
 *
 */
public abstract class NonPlayerFieldObject extends FieldObject {

	/** Default death time */
	protected static final float DEFAULT_DEATH_TIME = 30.0f;
	
	
	/** Is dying */
	protected boolean dying;
	/** Death timer */
	protected float deathTimer;
	/** Initial death time */
	protected float initialDeathTime;
	
	/** Player direction, from player's point of view,
	 * only if the player object is touching the object*/
	protected Direction playerDir;
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public NonPlayerFieldObject(Point pos) {
		
		super(pos);
	
		dying = false;
		deathTimer = 0.0f;
	}
	
	
	/**
	 * Object-to-player collision
	 * @param pl Player
	 */
	public abstract void playerCollision(Player pl, Gamepad vpad, Stage stage, TimeManager tman);

	
	/**
	 * Update death timer
	 * @param tm Time mul.
	 */
	public void updateDeath(float tm) {
		
		if(dying) {

			deathTimer -= 1.0f * tm;
			if(deathTimer <= 0.0f) {
				
				dying = false;
			}
		}
	}
	
	
	/**
	 * Die
	 * @param time Time
	 * @param tman Time manager
	 */
	public void die(float time, TimeManager tman) {
		
		deathTimer = time;
		initialDeathTime = time;
		dying = true;
		exist = false;
		
		tman.setTimer(false, time);
		tman.forceStopSignal();
	}
	
	
	/**
	 * Die
	 * @param tman Time manager
	 */
	public void die(TimeManager tman) {
	
		die(DEFAULT_DEATH_TIME, tman);
	}
	
	
	/**
	 * Player interaction
	 * @param stage Stage
	 */
	protected void eventPlayerInteraction(Stage stage, TimeManager tman) {};
	
	
	/**
	 * Check player position and trigger player interaction
	 * if close and facing this object
	 * @param pl Player
	 * @param vpad Game pad
	 * @param stage Stage
	 * @param tman Time manager
	 */
	public void checkPlayerInteraction(Player pl, Gamepad vpad, Stage stage, TimeManager tman) {
		
		if(!exist || tman.isWaiting() || pl.isMoving()) return;
		
		Point p = pl.getPosition();
		
		// Check if next to this object
		if((int)Math.abs(p.x-pos.x) + (int)Math.abs(p.y-pos.y) == 1) {
			
			// Check direction
			// NOTE: Player direction is actually direction from player's
			// perspective
			if(p.y < pos.y) playerDir = Direction.Down;
			else if(p.y > pos.y) playerDir = Direction.Up;
			else if(p.x < pos.x) playerDir = Direction.Right;
			else if(p.x > pos.x) playerDir = Direction.Left;

			if(vpad.getDirection() == playerDir)
				eventPlayerInteraction(stage, tman);
		}
		else {
			
			playerDir = Direction.None;
		}
	}
	
	/**
	 * Draw death
	 * @param g Graphics object
	 * @param bmp Bitmap
	 * @param sx Source x
	 * @param sy Source y
	 * @param sw Source width
	 * @param sh Source height
	 * @param scale Scale
	 */
	public void drawDeath(Graphics g, Bitmap bmp, int sx, int sy, int sw, int sh, float scale) {
		
		if(!dying) return;
		
		float t = 1.0f - deathTimer / initialDeathTime;
		
		float dw = scaleValue.x * (1 + scale * t);
		float dh = scaleValue.y * (1 + scale * t);
		
		float dx = vpos.x - (dw-scaleValue.x)/2;
		float dy = vpos.y - (dw-scaleValue.y)/2;
		
		g.setColor(1, 1, 1, 1.0f - t);
		g.drawScaledBitmapRegion(bmp, sx, sy, sw, sh, dx, dy, dw, dh, Flip.NONE);
		g.setColor();
	}
}
