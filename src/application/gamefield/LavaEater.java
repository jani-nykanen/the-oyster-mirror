package application.gamefield;

import core.types.Point;

/**
 * A crate that "eats" lava
 * @author Jani Nykänen
 *
 */
public class LavaEater extends Crate {

	/** Eye close timer */
	private static final float EYE_CLOSE_TIME = 6.0f;
	
	
	/** Eye close timer. Required to keep the eye open
	 * even if moving
	 */
	protected float eyeCloseTimer = 0.0f;
	
	
	@Override
	protected boolean freeTileCheck(Stage stage, int x, int y) {
		
		return stage.isLava(x, y);
	}
	
	
	@Override
	protected void updateEvent(Stage stage, float tm) {
		
		//  Handle eye blinking
		if(!moving && eyeCloseTimer > 0.0f) {
			
			eyeCloseTimer -= 1.0f * tm;
			
			if(eyeCloseTimer <= 0.0f) {
				
				sx = 0;
			}
		}
		else if(eyeCloseTimer <= 0.0f) {
			
			if(sx > 0 && !moving) {	
			
				eyeCloseTimer = EYE_CLOSE_TIME;
			}
			else if(moving) {
				
				sx = 128;
			}
		}
	}
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public LavaEater(Point pos) {
		
		super(pos);
		dieInLava = false;
		sx = 0;
		sy = 128;
	}

}
