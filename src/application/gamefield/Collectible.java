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
	
	/** If exist */
	protected boolean exist;
	
	/** Float timer */
	private float floatTimer;
	
	
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
	

	@Override
	public void update(Gamepad vpad, TimeManager tman, float tm) {

		final float FLOAT_SPEED = 0.05f;
		
		if(!exist) return;
		
		
		// Update floating timer
		floatTimer += FLOAT_SPEED * tm;
	}
	

	@Override
	public void draw(Graphics g) {

		final float FLOAT_AMPLITUDE = 8.0f;
		
		if(!exist) return;
		
		
		// Calculate "floating position"
		float floatPos = (float)Math.sin(floatTimer) * FLOAT_AMPLITUDE;
		
		// Draw
		g.drawScaledBitmapRegion(bmpCollectibles, id*128, 0, 128, 128, 
				vpos.x, vpos.y + floatPos, scaleValue.x, scaleValue.y, Flip.NONE);
	}


	@Override
	public void playerCollision(Player pl, TimeManager tman) {
		
		if(!exist) return;
		
		// If not waiting, check if player has the same position
		if(!tman.isWaiting()) {
			
			if(pl.getPosition().equals(pos)) {
				
				onPlayerCollision(pl);
				exist = false;
			}
		}
	}

	
	/**
	 * Called when player is overlaying this object
	 * @param pl Player
	 */
	public abstract void onPlayerCollision(Player pl);
}
