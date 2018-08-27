package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Point;
import core.utility.AssetPack;

/**
 * A movable crate
 * @author Jani Nykänen
 *
 */
public class Crate extends NonPlayerFieldObject {

	/** A bitmap of movable object */
	private static Bitmap bmpMovable;
	
	
	/**
	 * Initialize global crate content
	 * (read: get assets)
	 * @param assets Assets
	 */
	static public void init(AssetPack assets) {
		
		// Get assets
		bmpMovable = assets.getBitmap("movable");
	}
	
	
	/**
	 * Constructor
	 * @param pos Target position
	 */
	public Crate(Point pos) {
		
		super(pos);
	}


	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {
		
		// ...
	}
	

	@Override
	public void draw(Graphics g) {
		
		g.drawScaledBitmapRegion(bmpMovable,0,0,128,128,
				vpos.x, vpos.y, scaleValue.x, scaleValue.y, Flip.NONE);
			
	}


	@Override
	public void playerCollision(Player pl, TimeManager tman) {
		
		// ...
	}

}
