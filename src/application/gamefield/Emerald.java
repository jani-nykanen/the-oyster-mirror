package application.gamefield;

import core.types.Point;

public class Emerald extends Collectible {

	/**
	 * Constructor
	 * @param pos Position
	 */
	public Emerald(Point pos) {
		
		super(pos);
		id = 5;
		animationMode = 1;
	}
	

	@Override
	public void onPlayerCollision(Player pl, Stage stage) {

		// ...
	}

}
