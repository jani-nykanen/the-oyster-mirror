package application.gamefield;

import application.Gamepad;
import core.types.Point;

/**
 * A field object that is *not* a player object
 * @author Jani Nykänen
 *
 */
public abstract class NonPlayerFieldObject extends FieldObject {

	/**
	 * Constructor
	 * @param pos Position
	 */
	public NonPlayerFieldObject(Point pos) {
		
		super(pos);
	}
	
	
	/**
	 * Object-to-player collision
	 * @param pl Player
	 */
	public abstract void playerCollision(Player pl, Gamepad vpad, Stage stage, TimeManager tman);

	
}
