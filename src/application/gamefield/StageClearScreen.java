package application.gamefield;

import application.Gamepad;
import application.global.Transition;
import application.ui.Button;
import application.ui.VerticalButtonList;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;
import core.utility.VoidCallback;

/**
 * A screen shown when the stage is beaten
 * @author Jani Nykänen
 *
 */
public class StageClearScreen {


	/** Font bitmap */
	static private Bitmap bmpFont;
	/** Collectibles bitmap */
	static private Bitmap bmpCollectibles;
	
	/** Start time */
	static private final float START_TIME = 60.0f;
	/** Amount of buttons */
	static private final int BUTTON_COUNT = 2;
	/** Button text */
	static private String[] BUTTON_TEXT = new String[] {
			"Stage menu",
			"Play again"
	};

	/** Is active */
	private boolean active;
	/** Screen phase */
	private int phase;
	/** Timer */
	private float timer;
	/** If the start was golden */
	private boolean goldenStar;
	/** Star scale timer */
	private float starScaleTimer;
	/** Text wave */
	private float textWave;
	/** Is restarting */
	private boolean restarting = false;
	
	/** Buttons */
	private VerticalButtonList buttons;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		// Get bitmaps
		bmpFont = assets.getBitmap("font");
		bmpCollectibles = assets.getBitmap("collectibles");
	}
	
	
	/**
	 * Draw a star
	 * @param g Graphics object
	 * @param tx Translation x
	 * @param ty Translation y
	 * @param isShadow If only a shadow
	 */
	private void drawStar(Graphics g, float alpha, float tx, float ty, boolean isShadow) {
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		final float STAR_SIZE = 192;
		final float STAR_SIZE_FACTOR = 0.125f;
		final float STAR_ROTATE_ANGLE = (float)Math.PI / 12.0f;
		final float SHADOW_ALPHA = 0.5f;

		// Center to the star
		tr.push();
		tr.translate(view.x / 2 + tx, view.y/2-STAR_SIZE/2 + ty);
		
		float starScale = 1.0f;
		if(phase == 1) {
			
			float s1 = (float)Math.sin(starScaleTimer);
			float s2 = (float)Math.sin(starScaleTimer / 2.0f);
			
			// Calculate scale value
			starScale = 1.0f + s1 * STAR_SIZE_FACTOR;
			
			// Rotate
			tr.rotate(s2 * STAR_ROTATE_ANGLE);
			
			// Set "shining" color
			float shineValue = starScale;
			g.setColor(shineValue, shineValue, shineValue);
 		}
		else if(phase == 0) {
			
			starScale = alpha;
		}
		// Scale
		tr.scale(starScale, starScale);
		tr.use();

		// If shadow, make black
		if(isShadow) {
			
			g.setColor(0, 0, 0, SHADOW_ALPHA * alpha);
		}
		
		// Draw star
		g.drawScaledBitmapRegion(bmpCollectibles, goldenStar ? 256 : 384, 0, 
				128, 128, 
				-STAR_SIZE/2, 
				- STAR_SIZE/2, 
				STAR_SIZE, STAR_SIZE,
				Flip.NONE);
		
		
		tr.pop();
		g.setColor();
	}
	
	
	/**
	 * Draw "Stage clear!" text
	 * @param g Graphics object
	 * @param tx Translation x
	 * @param ty Translation y
	 */
	private void drawClearText(Graphics g, float tx, float ty) {
		
		final String TEXT = "STAGE CLEAR!";
		final int TEXT_XOFF = -26;
		final float TEXT_AMPLITUDE = 16.0f;
		final float TEXT_SCALE = 1.0f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float xpos = view.x/2 + tx;
		float ypos = view.y/2 + ty;
		
		g.drawWavingText(bmpFont, TEXT, xpos, ypos, TEXT_XOFF, 0, true, 
				textWave, TEXT_AMPLITUDE, TEXT.length() / 2, TEXT_SCALE);
	}
	
	
	/**
	 * Draw the buttons menu
	 * @param g Graphics object
	 */
	private void drawButtonsMenu(Graphics g) {
		
		final float XOFF = -26;
		final float YOFF = 56.0f;
		final float POS_X = -192.0f;
		final float POS_Y = 128.0f;
		final float TEXT_SCALE = 0.80f;
		final float SHADOW_OFF = 8.0f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		buttons.draw(g, bmpFont, view.x/2 + POS_X, view.y/2 + POS_Y, XOFF, YOFF, 
				TEXT_SCALE, false, SHADOW_OFF);
	}
	
	
	/**
	 * Constructor
	 * @param game Reference to the game object
	 */
	public StageClearScreen(GameField game) {
		

		// Set button callbacks
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		// Stage menu
		cbs[0] = new VoidCallback() {
			@Override
			public void execute() {
				
				restarting = false;
				game.quit();
			}
		};
		// Restart
		cbs[1] = new VoidCallback() {
			@Override
			public void execute() {
				
				restarting = true;
				game.fadeAndReset();
			}
		};
		
		// Create button list
		buttons = new VerticalButtonList();
		// Add buttons
		for(int i = 0; i < BUTTON_COUNT; ++ i) {
			
			buttons.addButton(new Button(BUTTON_TEXT[i], cbs[i]));
		}
		
		// Set defaults
		reset();
	}
	

	/**
	 * Update clear screen
	 * @param vpad Game pad
	 * @param tm Time mul.
	 */
	public void update(Gamepad vpad, float tm) {
		
		final float STAR_SCALE_SPEED = 0.05f;
		final float TEXT_WAVE_SPEED = 0.05f;
		
		if(!active) return;
		
		switch(phase) {
		
		// Appear
		case 0:
			
			timer -= 1.0f * tm;
			if(timer <= 0.0f) {
				
				++ phase;
				timer = 0.0f;
			}
			
			break;
			
		// Menu
		case 1:
			
			// Update star scale
			starScaleTimer += STAR_SCALE_SPEED * tm;
			
			// Update buttons
			buttons.update(vpad, tm);
			
			break;
			
			
		default:
			break;

		}
		
		// Update text wave
		textWave += TEXT_WAVE_SPEED * tm;
	}
	
	
	/**
	 * Draw "Stage Clear"
	 * @param g Graphics objects
	 * @param trans Transition
	 */
	public void draw(Graphics g, Transition trans) {
		
		final float FILL_ALPHA = 1.0f;
		final float SHADOW_OFF = 8.0f;
		final float TEXT_SHADOW_ALPHA = 0.5f;
		final float TEXT_YPLUS = 128.0f;
		final float SCALE_OUT = 0.5f;
		final float SCALE_IN = 0.5f;
		
		if(!active) return;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		// If restarting, zoom in
		if(trans.isActive() && trans.getMode() == Transition.Mode.In) {
			
			float scale = 1.0f;
			if(restarting)
				scale = 1.0f + (1.0f-trans.getTimer()) * SCALE_IN;
			else
				scale = 1.0f - (1.0f-trans.getTimer())*SCALE_OUT;
			
			tr.translate(view.x/2, view.y/2);
			tr.scale(scale, scale);
			tr.translate(-view.x/2, -view.y/2);
		}
		
		
		// Fill the screen with white
		// (Note: we cannot use global fading
		//  here)
		float t = timer / START_TIME;
		float alpha = phase == 0 ? (1-t) * FILL_ALPHA : FILL_ALPHA;
		g.setColor(1, 1, 1, alpha);
		g.fillRect(0, 0, view.x, view.y);
		
		// Draw star shadow
		drawStar(g, alpha, SHADOW_OFF, SHADOW_OFF, true);
		
		// Draw star
		drawStar(g, alpha, 0, 0, false);
		
		
		// Draw clear text shadow
		float texty = TEXT_YPLUS * t;
		g.setColor(0, 0, 0, alpha * TEXT_SHADOW_ALPHA);
		drawClearText(g, SHADOW_OFF, SHADOW_OFF + texty);
		
		// Draw clear text
		g.setColor(1, 1, 0, alpha);
		drawClearText(g, 0, texty);
		
		// Draw buttons menu
		g.setColor();
		g.setGlobalAlpha(alpha);
		drawButtonsMenu(g);
		g.setGlobalAlpha();
	}
	
	
	/**
	 * Activate
	 * @param goldenStar If the achieved start was golden
	 * (i.e goal reached within the turn limit)
	 */
	public void activate(boolean goldenStar) {
		
		timer = START_TIME;
		active = true;
		phase = 0;
		this.goldenStar = goldenStar;
	}
	
	
	/**
	 * Is this object active
	 * @return True, if active
	 */
	public boolean isActive() {
		
		return active;
	}
	
	
	/**
	 * Is the scene fading in
	 * @return True, if fading
	 */
	public boolean isFadingIn() {
		
		return phase == 0;
	}
	
	
	/**
	 * Get fade value
	 * @return Fade value
	 */
	public float getFadeValue() {
		
		return timer / START_TIME;
	}
	
	
	/**
	 * Reset
	 */
	public void reset() {
		
		active = false;
		phase = 0;
		timer = 0.0f;
		textWave = 0.0f;
		starScaleTimer = 0.0f;
		
		buttons.resetCursor();
	}
}
