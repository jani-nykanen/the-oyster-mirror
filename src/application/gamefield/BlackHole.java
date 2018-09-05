package application.gamefield;

import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Point;
import core.utility.AssetPack;

/**
 * A black hole
 * @author Jani Nykänen
 *
 */
public class BlackHole extends Crate {

	
	/** Black hole bitmap */
	static private Bitmap bmpBlackHole;
	
	/** Angle */
	private float angle = 0.0f;
	/** Size timer */
	private float sizeTimer = 0.0f;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets
	 */
	static public void init(AssetPack assets) {
		
		bmpBlackHole = assets.getBitmap("black_hole");
	}
	
	
	@Override
	protected boolean freeTileCheck(Stage stage, int x, int y) {
		
		return x > 0 && y > 0 && x < stage.getWidth() -1 && y < stage.getHeight()-1;
	}
	
	
	@Override
	protected void updateEvent(Stage stage, float tm) {
		
		final float ROTATE_SPEED = 0.05f;
		final float SIZE_SPEED = 0.05f;
		
		// Update angle
		angle += ROTATE_SPEED * tm;
		
		// Update size
		sizeTimer += SIZE_SPEED * tm;
	}
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public BlackHole(Point pos) {
		
		super(pos);
		dieInLava = false;
		immune = true;
	}

	
	@Override
	public void draw(Graphics g) {
		
		final float SIZE_MOD = 0.20f;
		
		// Calculate scale
		float scale = 1.0f + SIZE_MOD + (float)Math.sin(sizeTimer) * SIZE_MOD;
		
		// Set transformations
		Transformations tr = g.transform();
		tr.push();
		tr.translate(vpos.x + scaleValue.x/2, vpos.y + scaleValue.y / 2);
		tr.rotate(angle);
		tr.scale(scale, scale);
		tr.use();
		
		g.drawScaledBitmapRegion(bmpBlackHole,0,0,128,128,
				-scaleValue.x/2, -scaleValue.y/2, 
				scaleValue.x, scaleValue.y, Flip.NONE);
		
		tr.pop();
	}
	
	
	@Override
	public void onMovingStopped(Stage stage, TimeManager tman) {
		
		stage.updateTileData(pos.x, pos.y, 0);
		stage.updateSolidTileData(pos.x, pos.y, 16);
	};
}
