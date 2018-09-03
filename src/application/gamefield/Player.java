package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Direction;
import core.types.Point;
import core.types.Vector2;
import core.utility.AssetPack;

/**
 * Player object
 * @author Jani Nykänen
 *
 */
public class Player extends FieldObject {

	/** Maximum amount of fadings */
	static private final int FADING_MAX = 16;
	/** Fading generation interval */
	static private final float FADING_INTERVAL = 4.0f;
	/** The stick movement that is required to be recognized
	 * as accepted input */
	static final public float STICK_DELTA = 0.5f;
	
	/** Player bitmap */
	static private Bitmap bmpPlayer;
	
	/** Outline opaqueness factor" */
	private float outlineFactor = 0.0f;
	
	/** Eye position */
	private Vector2 eyePos;
	
	/** Fadings */
	private Fading[] fadings;
	/** Fading generation timer */
	private float fadeGenTimer = FADING_INTERVAL;
	
	/** Key count */
	private int keys;
	/** Hammers */
	private int hammers;
	
	
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

		if(tman.isWaiting() || tman.hasStopped()) return;
		
		// Check direction
		Point t = pos.clone();
		Direction dir = vpad.getDirection();
		switch(dir) {
		
		case Down:		
			++ t.y;
			break;
			
		case Left:
			-- t.x;
			break;
			
		case Right:
			++ t.x;
			break;
			
		case Up:
			-- t.y;
			break;
			
		default:
			break;
		
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
		if(stage.isSolid(t.x, t.y)) {
			
			doMove = false;
		}
		
		// If no obstacles, move
		if(doMove) {
			
			target.x = t.x;
			target.y = t.y;
			
			tman.setTimer(true);
			moving = true;

		}
		
	}
	
	
	
	/**
	 * Generate fadings
	 * @param tm Time mul-
	 */
	private void generateFadings(float tm) {
		
		final float FADING_TIME = 20.0f;
		
		if(!moving) return;
		
		// Update timer
		fadeGenTimer -= 1.0f * tm;
		if(fadeGenTimer <= 0.0f) {
			
			// Create a fading
			for(Fading f : fadings) {
				
				if(!f.doesExist()) {
					
					f.create(vpos, FADING_TIME);
					break;
				}
			}
			
			fadeGenTimer += FADING_INTERVAL;
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
		
		// Create components
		eyePos = new Vector2();
		eyePos = vpos.clone();
		fadings = new Fading[FADING_MAX];
		for(int i = 0; i < fadings.length; ++ i) {
			
			fadings[i] = new Fading();
		}
		
		// Set values
		keys = 0;
	
	}
	

	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {
		
		final float OUTLINE_TIMER_SPEED = 0.05f;
		
		// Update "outline factor"
		outlineFactor += OUTLINE_TIMER_SPEED * tm;
		
		// Update fadings
		for(Fading f : fadings) {
			
			f.update(tm);
		}
		
		// Control
		control(vpad, tman, stage);
		
		// Move eyes
		moveEyes(tm);
		
		// Generate fadings
		generateFadings(tm);
	}
	

	@Override
	public void draw(Graphics g) {
		
		final float OUTLINE_OPAQUE_DELTA = 0.125f;
		final float OUTLINE_OPAQUE_START = 0.5f + OUTLINE_OPAQUE_DELTA;
		
		final float OUTLINE_START = 12;
		final float OUTLINE_DELTA = 4;
		
		// Draw fadings
		for(Fading f : fadings) {
			
			f.draw(g, bmpPlayer, 0, 0, 128, 128, scaleValue.x, scaleValue.y);
		}
		
		// Green outline
		float s = (float)Math.sin(outlineFactor);
		g.setColor(0.5f, 1, 0, 
				OUTLINE_OPAQUE_START + s * OUTLINE_OPAQUE_DELTA);
		
		float outline = OUTLINE_START - s * OUTLINE_DELTA;
		g.fillRect(vpos.x-outline, vpos.y-outline, scaleValue.x+outline*2, scaleValue.y+outline*2);
		g.setColor();
		
		// Base block
		g.drawScaledBitmapRegion(bmpPlayer, 0, 0, 128, 128,
				vpos.x, vpos.y, scaleValue.x, scaleValue.y, Flip.NONE);
		
		// Calculate "true" eye position
		float eyeX = vpos.x + (vpos.x-eyePos.x);
		float eyeY= vpos.y + (vpos.y-eyePos.y);
		
		// Draw eyes
		g.drawScaledBitmapRegion(bmpPlayer, 128, 0, 128, 128,
				eyeX, eyeY, scaleValue.x, scaleValue.y, Flip.NONE);
	}

	
	/**
	 * Force movement
	 * @param dir Direction
	 */
	public void forceMove(Direction dir) {
		
		switch(dir) {
		
		case Down:		
			++ target.y;
			break;
			
		case Left:
			-- target.x;
			break;
			
		case Right:
			++ target.x;
			break;
			
		case Up:
			-- target.y;
			break;
			
		default:
			break;
		
		}
		moving = true;
	}
	
	
	/**
	 * Force stop
	 */
	public void forceStop() {
		
		moving = false;
		target.x = pos.x;
		target.y = pos.y;
		
		vpos.x = pos.x * scaleValue.x;
		vpos.y = pos.y * scaleValue.y;
	}
	
	
	/**
	 * Add a key
	 */
	public void addKey() {
		
		++ keys;
	}

	
	/**
	 * Get amount of keys
	 * @return Key count
	 */
	public int getKeyCount() {
		
		return keys;
	}
	
	
	/**
	 * Reduce a key
	 */
	public void reduceKey() {
		
		if(keys > 0)
			-- keys;
	}
	
	
	/**
	 * Add a hammer
	 */
	public void addHammer() {
		
		++ hammers;
	}

	
	/**
	 * Get amount of hammers
	 * @return Hammer count
	 */
	public int getHammerCount() {
		
		return hammers;
	}
	
	
	/**
	 * Reduce a hammer
	 */
	public void reduceHammer() {
		
		if(hammers > 0)
			-- hammers;
	}
}
