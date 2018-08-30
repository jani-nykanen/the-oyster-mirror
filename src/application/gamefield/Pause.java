package application.gamefield;

import application.Gamepad;
import application.ui.Button;
import application.ui.VerticalButtonList;
import core.ApplicationListener;
import core.State;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;
import core.utility.VoidCallback;

/**
 * Pause screen
 * @author Jani Nykänen
 *
 */
public class Pause {
	
	/** Amount of pause buttons */
	static private int BUTTON_COUNT = 4;
	
	/** Button text */
	static private final String[] BUTTON_TEXT = {
		"Resume",
		"Restart",
		"Settings",
		"Quit"
	};
	
	/** Time required for in/out transition */
	static private final float TRANSITION_TIME = 30.0f;

	/** Font bitmap */
	static public Bitmap bmpFont;
	
	/** Buttons */
	private VerticalButtonList buttons;
	
	/** Timer */
	private float timer;
	/** Is active */
	private boolean active;
	/** Is leaving */
	private boolean leaving;
	
	
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
	private void drawBox(Graphics g, float w, float h, float shadow) {
		
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
	public Pause(GameField game) {
		
		// Create callbacks for buttons
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		// Resume
		cbs[0] = new VoidCallback() {
			@Override
			public void execute() {
				
				timer = TRANSITION_TIME;
				leaving = true;
			}
		};
		
		// Restart
		cbs[1] = new VoidCallback() {
			@Override
			public void execute() {
						
				timer = TRANSITION_TIME;
				leaving = true;
				game.fadeAndReset();
			}
		};
		// Settings
		cbs[2] = null;
		// Quit
		cbs[3]  = new VoidCallback() {
			@Override
			public void execute() {
						
				timer = TRANSITION_TIME;
				leaving = true;
				game.quit();
			}
		};
		
		// Create buttons
		buttons = new VerticalButtonList();
		for(int i = 0; i < BUTTON_COUNT; ++ i) {
			
			buttons.addButton(new Button(BUTTON_TEXT[i], cbs[i]));
		}
		
		// Set default values
		timer = 0.0f;
		active = false;
		leaving = false;
	}
	
	
	/**
	 * Update pause screen
	 * @param vpad Virtual gamepad
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
			buttons.update(vpad, tm);
		}
	}
	
	
	/**
	 * Fade-out only update function
	 * @param tm Time multiplier
	 */
	public void fadeOnly(float tm) {
		
		if(!active) return;
		
		if(leaving && timer > 0.0f) {
			
			timer -= 1.0f * tm;
			if(timer <= 0.0f && leaving) {
				
				active = false;
			}
		}
	}
	
	
	/**
	 * Draw pause screen
	 * @param g Graphics object
	 */
	public void draw(Graphics g) {
		
		if(!active) return;
		
		final float BOX_WIDTH = 432;
		final float BOX_HEIGHT = 288;
		final float SHADOW_OFFSET = 16;
		final float BG_ALPHA = 0.5f;
		
		final float BUTTONS_X = 48.0f;
		final float BUTTONS_Y = 24.0f;
		final float BUTTONS_TEXT_OFF = -26.0f;
		final float BUTTONS_YOFF = 64.0f;
		final float BUTTONS_SCALE = 0.8f;
		final float BUTTONS_SHADOW = 4.0f;
		
		// Calculate scale value
		float scale = timer > 0.0f ? 1.0f - timer / TRANSITION_TIME : 1.0f;
		if(leaving)
			scale = 1.0f - scale;
		
		// Darken the background
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		g.setColor(0, 0, 0, BG_ALPHA * scale);
		g.fillRect(0, 0, view.x, view.y);
		
		// Set global alpha
		g.setGlobalAlpha(scale);
		
		// Center the screen
		tr.push();
		tr.translate(view.x / 2, view.y / 2);
		tr.use();
		
		// Draw a box
		drawBox(g, BOX_WIDTH * scale, BOX_HEIGHT * scale, SHADOW_OFFSET * scale);
		
		// Set scale for button text
		tr.scale(scale, scale);
		tr.use();
		
		// Draw buttons menu
		buttons.draw(g, bmpFont, -BOX_WIDTH/2 + BUTTONS_X, -BOX_HEIGHT/2 + BUTTONS_Y, 
				BUTTONS_TEXT_OFF, BUTTONS_YOFF, BUTTONS_SCALE, false, BUTTONS_SHADOW);
		
		g.setGlobalAlpha();
		tr.pop();
	}
	
	
	/**
	 * Activate pause
	 */
	public void activate() {
		
		active = true;
		timer = TRANSITION_TIME;
		leaving = false;
		buttons.resetCursor();
	}
	
	
	/**
	 * Is the pause screen active
	 * @return True, if active
	 */
	public boolean isActive() {
		
		return active;
	}
	
}
