package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.types.Point;
import core.utility.AssetPack;

/**
 * A lock. Notice that this thing
 * is not drawn until it's dying
 * @author Jani Nykänen
 *
 */
public class LavaContainer extends NonPlayerFieldObject {

	
	/** Static objects bitmap */
	static private Bitmap bmpStatic = null;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		// Get bitmaps
		bmpStatic = assets.getBitmap("static");
	}
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public LavaContainer(Point pos) {
		
		super(pos);
	}

	
	@Override
	protected void eventPlayerInteraction(Player pl, Stage stage, TimeManager tman) {
		
		if(pl.getHammerCount() <= 0)  return;
		
		// Reduce key
		pl.reduceHammer();
		
		// Update tile data in the container position
		// to lava
		stage.updateTileData(pos.x, pos.y, 3);
		
		// Die
		die(tman);
	}
	
	
	@Override
	public void playerCollision(Player pl, Gamepad vpad, Stage stage, TimeManager tman) {

		checkPlayerInteraction(pl, vpad, stage, tman);
	}

	
	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {

		// Update death
		if(!exist) {
			
			updateDeath(tm);
		}
	}

	
	@Override
	public void draw(Graphics g) {
		
		if(exist) return;
		
		drawDeath(g, bmpStatic, 0, 256, 128, 128, 1.0f);
	}

}
