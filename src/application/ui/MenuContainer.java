package application.ui;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.utility.AssetPack;


public abstract class MenuContainer {

	/** Transition time */
	static final protected float TRANSITION_TIME = 30.0f;
	
	
	/** Font bitmap */
	static protected Bitmap bmpFont;
	
	/** Timer */
	protected float timer;
	/** Is active */
	protected boolean active;
	/** Is leaving */
	protected boolean leaving;
	
	/** Buttons */
	protected VerticalButtonList buttons;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		// Get assets
		bmpFont = assets.getBitmap("font");
	}
	
	
	/**
	 * Draw a centered box
	 * @param g Graphics object
	 * @param w Width
	 * @param h Height
	 * @param shadow Shadow offset
	 */
	protected void drawBox(Graphics g, float w, float h, float shadow) {
		
		final float SHADOW_ALPHA = 0.5f;
		
		// Draw shadow
		g.setColor(0, 0, 0, SHADOW_ALPHA);
		g.fillRect(-w/2 + shadow, -h/2 + shadow, w, h);
		
		// Draw base box
		g.setColor();
		g.fillRect(-w/2, -h/2 , w, h);
	}
	
	
	/**
	 * Constructor
	 */
	public MenuContainer() {
		
		// Create button list object
		buttons = new VerticalButtonList();
	}
	
	
	/**
	 * Custom update event
	 * @param vpad Virtual game pad
	 * @param tm Time multiplier
	 */
	protected abstract void updateEvent(Gamepad vpad, float tm);
	
	
	/**
	 * Draw menu container
	 * @param g Graphics object
	 */
	public abstract void draw(Graphics g);
	
	
	/**
	 * Update menu container
	 * @param vpad Game pad
	 * @param tm Time mul.
	 */
	public void update(Gamepad vpad, float tm) {
		
		if(!active) return;
		
		// Update timer
		if(timer > 0.0f) {
			
			timer -= 1.0f * tm;
			if(timer <= 0.0f && leaving) {
				
				active = false;
				return;
			}
		}
		else {
			
			// Update button list
			updateEvent(vpad, tm);
		}
	}
	
	
	/**
	 * Activation event, called when activated
	 */
	protected void activationEvent() { }
	
	
	/**
	 * Activate pause
	 */
	public void activate() {
		
		active = true;
		timer = TRANSITION_TIME;
		leaving = false;
		
		if(buttons != null)
			buttons.resetCursor();
		
		activationEvent();
	}
	
	
	/**
	 * Is the pause screen active
	 * @return True, if active
	 */
	public boolean isActive() {
		
		return active;
	}
	
	
	/**
	 * Is ready for "full action"
	 * @return True or false
	 */
	public boolean ready() {
		
		return active && timer <= 0.0f;
	}
	
	
	/**
	 * Get timer value (in [0,1])
	 * @return Timer value
	 */
	public float getTimerValue() {
		
		float t = timer / TRANSITION_TIME;
		if(leaving) 
			t = 1.0f - t;
		
		return t;
	}
}
