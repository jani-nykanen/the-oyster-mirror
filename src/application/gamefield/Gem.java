package application.gamefield;

import core.types.Point;

/**
 * A purple gem. Used to turn on/off
 * those purple walls
 * @author jani
 *
 */
public class Gem extends Collectible {

	/**
	 * Constructor
	 * @param pos Position
	 */
	public Gem(Point pos) {
		
		super(pos);
		
		id = 1;
	}

	
	@Override
	public void onPlayerCollision(Player pl, Stage stage) {
		
		stage.startPurpleTileFading(DEFAULT_DEATH_TIME);
	}
}
