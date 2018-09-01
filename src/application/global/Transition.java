package application.global;

import core.renderer.Graphics;
import core.renderer.Transformations;
import core.utility.RGBFloat;
import core.utility.VoidCallback;

/**
 * A transition manager
 * @author Jani Nykänen
 *
 */
public class Transition {

	/** The base fade time */
	static private final float FADE_TIME = 60.0f;
	
	
	/** Transition type */
	public enum Type {
		Fade
	};
	
	/** Transition mode */
	public enum Mode {
		In, Out,
	}
	
	
	/** Active type */
	private Type type = Type.Fade;
	/** Active mode */
	private Mode mode = Mode.In;
	
	/** Timer */
	private float timer = 0.0f;
	/** Is active */
	private boolean active = false;
	/** Speed */
	private float speed;
	/** Effect color */
	private RGBFloat color = new RGBFloat(1.0f, 1.0f, 1.0f);
	/** Callback */
	private VoidCallback cb = null;
	/** Callback index value */
	private int cbIndex = 0;
	
	
	/**
	 * Draw fading
	 * @param g Graphics object
	 */
	private void drawFading(Graphics g) {
		
		float t = timer / FADE_TIME;
		float alpha = mode == Mode.In ? 1.0f-t :  t;
		
		// Set transform
		Transformations tr = g.transform();
		tr.setView(1, 1);
		tr.identity();
		tr.use();
		
		// Clear screen with the effect color
		g.setColor(color.r, color.g, color.b, alpha);
		g.fillRect(0, 0, 1, 1);
		g.setColor();
	}
	
	
	/**
	 * Constructor
	 */
	public Transition() {
		
		// ...
	}
	
	
	/**
	 * Update 
	 * @param tm Time multiplier
	 */
	public void update(float tm) {
		
		if(!active) return;
		
		timer -= speed * tm;
		if(timer <= 0.0f) {

			// Call callback function, if not null & mode is in
			if(mode == Mode.In) {
				
				if(cb != null)
					cb.execute(cbIndex);
				
				mode = Mode.Out;
				timer += FADE_TIME;
			}
			else {
				
				active = false;
				timer = 0.0f;
			}
		}
	}
	
	
	/**
	 * Draw transition
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		
		if(!active) return;
		
		switch(type) {
		
		// Fading
		case Fade:
			drawFading(g);
			break;
			
		default:
			break;
			
		}
	}
	
	
	/**
	 * Activate
	 * @param mode Mode
	 * @param type Type
	 * @param speed Speed
	 * @param col Color
	 * @param cb Callback function
	 * @param index Index value
	 */
	public void activate(Mode mode, Type type, float speed, RGBFloat col, VoidCallback cb, int index) {
		
		this.mode = mode;
		this.type = type;
		this.speed = speed;
		this.color = col;
		this.cb = cb;
		this.timer = FADE_TIME;
		this.cbIndex = index;
		
		active = true;
	}
	
	
	/**
	 * Activate
	 * @param mode Mode
	 * @param type Type
	 * @param speed Speed
	 * @param col Color
	 * @param cb Callback function
	 */
	public void activate(Mode mode, Type type, float speed, RGBFloat col, VoidCallback cb) {
		
		activate(mode, type, speed, col, cb, 0);
	}
	
	
	/**
	 * Is active
	 * @return True, if active
	 */
	public boolean isActive() {
		
		return active;
	}
	
	
	/**
	 * Get timer value, scaled to [0,1].
	 * @return Timer value in range [0, 1]
	 */
	public float getTimer() {
		
		return timer / FADE_TIME;
	}
	
	
	/**
	 * Get fading mode
	 * @return
	 */
	public Mode getMode() {
		
		return mode;
	}
}
