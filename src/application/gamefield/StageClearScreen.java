package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;

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
		
		final float STAR_SIZE = 256;
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
		final float TEXT_SCALE = 1.25f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float xpos = view.x/2 + tx;
		float ypos = view.y/2 + ty;
		
		g.drawWavingText(bmpFont, TEXT, xpos, ypos, TEXT_XOFF, 0, true, 
				textWave, TEXT_AMPLITUDE, TEXT.length() / 2, TEXT_SCALE);
	}
	
	
	/**
	 * Constructor
	 */
	public StageClearScreen() {
		
		active = false;
		phase = 0;
		timer = 0.0f;
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
	 */
	public void draw(Graphics g) {
		
		final float FILL_ALPHA = 1.0f;
		final float SHADOW_OFF = 8.0f;
		final float TEXT_SHADOW_ALPHA = 0.5f;
		final float TEXT_YPLUS = 128.0f;
		
		if(!active) return;
		
		// Fill the screen with white
		// (Note: we cannot use global fading
		//  here)
		float t = timer / START_TIME;
		float alpha = phase == 0 ? (1-t) * FILL_ALPHA : FILL_ALPHA;
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
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
}
