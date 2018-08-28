package application.gamefield;

import application.Gamepad;
import core.renderer.Graphics;
import core.types.Point;
import core.types.Vector2;

/**
 * A generic field object
 * @author Jani Nykänen
 *
 */
public abstract class FieldObject {

	/** Initial scale value */
	static private Vector2 globalScaleValue = new Vector2();
	
	
	/** Grid position */
	protected Point pos;
	/** Target position */
	protected Point target;
	/** Rendering position (in decimal tile coordinates) */
	protected Vector2 vpos;
	/** Scale value */
	protected Vector2 scaleValue;
	
	/** Is moving */
	protected boolean moving;
	/** Does exist */
	protected boolean exist = true;
	

	/**
	 * Set global scale value
	 * @param x X scale
	 * @param y Y scale
	 */
	static public void setScaleValue(float x, float y) {
		
		globalScaleValue.x = x;
		globalScaleValue.y = y;
	}
	
	
	/**
	 * Constructor
	 * @param pos Target position
	 */
	public FieldObject(Point pos) {
		
		// Set positions & default values
		this.pos = pos.clone();
		target = pos.clone();
		vpos = new Vector2(pos.x * globalScaleValue.x, pos.y * globalScaleValue.y);
		moving = false;
		exist = true;

		// Set scale value
		scaleValue = globalScaleValue.clone();
	}
	
	
	/**
	 * Update field object position
	 * @param tman Time manager
	 */
	public void updatePosition(TimeManager tman, Stage stage) {
		
		if(!exist) return;
		
		if(moving) {
			
			if(tman.isWaiting()) {
				
				// Calculate "virtual position"
				float t = tman.getTime();
				vpos.x = (float)pos.x * t + (1-t) * (float)target.x;
				vpos.y = (float)pos.y * t + (1-t) * (float)target.y;
			}
			else {
				
				moving = false;
				
				// Move to the target position
				pos.x = target.x;
				pos.y = target.y;
				
				vpos.x = (float)pos.x;
				vpos.y = (float)pos.y;
				
				// Call "moving stopped" method
				onMovingStopped(stage, tman);
			}

		}
		else {
			
			vpos.x = (float)pos.x;
			vpos.y = (float)pos.y;
			
			pos.x = target.x;
			pos.y = target.y;
		}
		
		// Scale correctly
		vpos.x *= scaleValue.x;
		vpos.y *= scaleValue.y;
	}
	
	
	
	/**
	 * Force moving to be stopped
	 * @param stage Stage
	 * @param tman Time manager
	 */
	public void stopMoving(Stage stage, TimeManager tman) {

		moving = false;
		
		// Move to the target position
		pos.x = target.x;
		pos.y = target.y;
		
		vpos.x = (float)pos.x;
		vpos.y = (float)pos.y;
		
		// Call "moving stopped" method
		onMovingStopped(stage, tman);
	}
	
	
	/**
	 * Called when the moving is stopped
	 * @param stage Stage
	 */
	public void onMovingStopped(Stage stage, TimeManager tman) {};
	
	
	/**
	 * Update field object
	 * @param vpad Virtual gamepad
	 * @param tman Time manager
	 * @param stage Stage
	 * @param tm Time mul.
	 */
	public abstract void update(Gamepad vpad, TimeManager tman, Stage stage, float tm);
	
	
	/**
	 * Draw object
	 * @param g Graphics object
	 */
	public abstract void draw(Graphics g);
	

	/**
	 * Get object position
	 * @return Position
	 */
	public Point getPosition() {
		
		return pos;
	}
	
	
	/**
	 * Is the object moving
	 * @return True, if moving
	 */
	public boolean isMoving() {
		
		return moving;
	}
}
