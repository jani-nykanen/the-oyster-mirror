package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.types.Point;
import core.utility.AssetPack;

public class StarContainer extends NonPlayerFieldObject {

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
	public StarContainer(Point pos) {
		
		super(pos);
	}

	
	@Override
	protected void eventPlayerInteraction(Player pl, Stage stage, TimeManager tman) { }
	
	
	@Override
	public void playerCollision(Player pl, Gamepad vpad, Stage stage, TimeManager tman) {

		if(!exist) return;
		
		if(pl.getRemainingEmeralds() <= 0) {
			
			stage.updateTileData(pos.x, pos.y, 0);
			die(tman);
		}
	}

	
	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {

		if(!exist) {
			
			updateDeath(tm);
		}
	}

	
	@Override
	public void draw(Graphics g) {
		
		if(exist) return;
		
		drawDeath(g, bmpStatic, 128, 256, 128, 128, 4.0f);
	}

}
