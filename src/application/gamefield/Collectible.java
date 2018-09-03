package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Point;
import core.utility.AssetPack;

/**
 * A collectible item (key, coin etc)
 * @author Jani Nykänen
 *
 */
public abstract class Collectible extends NonPlayerFieldObject {

	/** Collectibles bitmap */
	static private Bitmap bmpCollectibles;
	
	/** Collectible id */
	protected int id = 0;
	/** Animation mode 
	 * TODO: Enumeration?
	 */
	protected int animationMode = 0;
	
	/** Float timer */
	private float floatTimer = 0.0f;
	/** Shine timer */
	private float shineTimer = 0.0f;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets
	 */
	static public void init(AssetPack assets) {
		
		// Get assets
		bmpCollectibles = assets.getBitmap("collectibles");
	}
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public Collectible(Point pos) {
		
		super(pos);
		exist = true;
		
		// Calculate initial float time
		final float FLOAT_MOD = (float)Math.PI / 6.0f;
		floatTimer = ( pos.x + pos.y ) * FLOAT_MOD;
	}
	
	
	/**
	 * Called on update function
	 * @param tman Time manager
	 * @param stage Stage
	 */
	public void onUpdate(TimeManager tman, Stage stage) {};

	
	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {

		final float FLOAT_SPEED = 0.05f;
		final float SHINE_SPEED = 0.025f;
		
		if(!exist) {
			
			updateDeath(tm);
			return;
		};
		
		// Update timers
		floatTimer += FLOAT_SPEED * tm;
		shineTimer += SHINE_SPEED * tm;
		
		// Call "on update" function
		onUpdate(tman, stage);
	}
	

	@Override
	public void draw(Graphics g) {

		final float FLOAT_AMPLITUDE = 8.0f;
		final float SCALE_FACTOR = 0.125f;
		final float COLOR_MOD = 0.125f;
		final float DEATH_SCALE = 2.0f;
		
		int bsx = (id % 4) * 128;
		int bsy = (id / 4);
		bsy *= 128;
		
		if(!exist) {
			
			// Draw death
			drawDeath(g, bmpCollectibles, bsx, bsy, 128, 128, DEATH_SCALE);
			
			return;
		};
		
		
		// Calculate "floating position" & scaling
		float floatPos = 0.0f;
		float sx = 1.0f;
		float sy = 1.0f;
		
		float s = (float)Math.sin(floatTimer);
		float c = 1.0f;
		if(animationMode == 0) {
			
			floatPos = s * FLOAT_AMPLITUDE;
			c = 1.0f + (float)Math.sin(shineTimer)*COLOR_MOD;
		}
		else if(animationMode == 1) {
			
			sx = 1.0f + s * SCALE_FACTOR;
			sy = 1.0f + s * SCALE_FACTOR;
			c = 1.0f + s*COLOR_MOD;
		}
		
		// Set shining color
		g.setColor(c,c,c);
		
		// Draw
		g.drawScaledBitmapRegion(bmpCollectibles, bsx, bsy, 128, 128, 
				vpos.x - scaleValue.x*(sx-1.0f)/2.0f, vpos.y - scaleValue.y*(sy-1.0f)/2.0f + floatPos, 
				scaleValue.x*sx, scaleValue.y*sy, Flip.NONE);
		
		g.setColor();
	}


	@Override
	public void playerCollision(Player pl, Gamepad vpad, Stage stage, TimeManager tman) {
		
		if(!exist) return;
	
		// If the player collides, make the player stop
		if(pl.getPosition().equals(pos)) {
				
			onPlayerCollision(pl, stage);
			pl.forceStop();
			die(tman);
		}
	}

	
	/**
	 * Called when player is overlaying this object
	 * @param pl Player
	 */
	public abstract void onPlayerCollision(Player pl, Stage stage);
}
