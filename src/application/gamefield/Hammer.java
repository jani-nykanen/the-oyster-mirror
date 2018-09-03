package application.gamefield;

import core.types.Point;

public class Hammer extends Collectible {

	/**
	 * Constructors
	 * @param pos Position
	 */
	public Hammer(Point pos) {
		
		super(pos);
		id = 4;
	}

	
	@Override
	public void onPlayerCollision(Player pl, Stage stage) {
		
		// Add a hammer
		pl.addHammer();
	}

}
