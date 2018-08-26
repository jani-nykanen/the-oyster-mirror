package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Point;
import core.utility.AssetPack;

/**
 * Player object
 * @author Jani Nykänen
 *
 */
public class Player extends FieldObject {

	/** Player bitmap */
	static private Bitmap bmpPlayer;
	
	
	/**
	 * Initialize global player content
	 * @param assets Asset pack
	 */
	static public void init(AssetPack assets) {
	
		// Get global bitmaps
		bmpPlayer = assets.getBitmap("player");
	}
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public Player(Point pos) {
		
		super(pos);
	}
	

	@Override
	public void update(Gamepad vpad, TimeManager tman, float tm) {
		
		// ...
	}
	

	@Override
	public void draw(Graphics g) {
		
		// Base block
		g.drawScaledBitmapRegion(bmpPlayer, 0, 0, 128, 128,
				vpos.x, vpos.y, scaleValue.x, scaleValue.y, Flip.NONE);
		
		// Eyes
		g.drawScaledBitmapRegion(bmpPlayer, 128, 0, 128, 128,
				vpos.x, vpos.y, scaleValue.x, scaleValue.y, Flip.NONE);
	}

}
