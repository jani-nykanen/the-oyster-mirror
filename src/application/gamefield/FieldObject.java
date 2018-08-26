package application.gamefield;

import core.renderer.Graphics;
import core.types.Point;
import core.types.Vector2;

/**
 * An object with at least a position
 * @author Jani Nykänen
 *
 */
public abstract class FieldObject {

	/** Grid position */
	private Point pos;
	/** Target position */
	private Point target;
	/** Rendering position */
	private Vector2 vpos;
	
	
	/**
	 * Constructor
	 */
	public FieldObject(Point pos) {
		
		// Set positions
		this.pos = pos.clone();
		target = pos.clone();
		
	}
	
	
	/**
	 * Update field object position
	 */
	public void updatePosition() {
		
	}
	
	
	/**
	 * Draw object
	 * @param g Graphics object
	 */
	public abstract void draw(Graphics g);
}
