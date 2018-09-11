package application.gamefield;

import core.types.Point;

/**
 * A key (game object)
 * @author Jani Nykänen
 *
 */
public class Key extends Collectible {

	/**
	 * Constructor
	 * @param pos Position
	 */
	public Key(Point pos) {
		
		super(pos);
		
		id = 0;
		animationMode = AnimationMode.Float;
	}

	
	@Override
	public void onPlayerCollision(Player pl,  Stage stage) {
		
		pl.addKey();
	}

}
