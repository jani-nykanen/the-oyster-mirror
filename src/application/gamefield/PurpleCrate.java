package application.gamefield;

import core.types.Point;

/**
 * A purple crate
 * @author Jani Nykänen
 *
 */
public class PurpleCrate extends Crate {

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
