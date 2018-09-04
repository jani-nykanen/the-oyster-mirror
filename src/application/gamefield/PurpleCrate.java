package application.gamefield;

import core.types.Point;

/**
 * A purple crate
 * @author Jani Nykänen
 *
 */
public class PurpleCrate extends Crate {

	/** Die if in lava */
	protected boolean dieInLava = true;
	
	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public PurpleCrate(Point pos) {
		
		super(pos);
		sx = 128;
	}
	
	
	@Override
	protected void deathEvent(Stage stage) {
		
		stage.startPurpleTileFading(DEFAULT_DEATH_TIME);
	}

}
