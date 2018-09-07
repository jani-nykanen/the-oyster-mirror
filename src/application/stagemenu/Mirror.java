package application.stagemenu;

import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;

/**
 * Mirror aka "map"
 * @author Jani Nykänen
 *
 */
public class Mirror {

	/** Mirror bitmap */
	static private Bitmap bmpMirror;
	
	/** Size factor */
	private float sizeFactor = 0.0f;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		bmpMirror = assets.getBitmap("mirror");
	}
	

	/**
	 * Draw the mirror frame
	 * @param g Graphics object
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param scaleFactor Scale factor
	 * @param scaleValue Scale value
	 * @param width Width
	 * @param height Height
	 * @param shadow Is shadow drawn
	 */
	private void drawMirrorFrame(Graphics g, float x, float y, float scaleFactor, float scaleValue, float width, float height,
			boolean shadow) {
		
		final float SHINE_VALUE = 0.05f;
		final float SHADOW_ALPHA = 0.33f;
		
		Transformations tr = g.transform();
		
		// Set transformations for the mirror
		tr.push();
		tr.translate(x + width/2, y + height/2);
		tr.scale(scaleValue, scaleValue);
		tr.use();

		// Shine
		if(shadow) {
			
			g.setColor(0, 0, 0, SHADOW_ALPHA);
		}
		else {
			
			float shine = 1.0f + scaleFactor * SHINE_VALUE;
			g.setColor(shine, shine, shine);
		}
				
		// Draw mirror
		g.drawScaledBitmapRegion(bmpMirror, 0, 0, 256, 256, 
				-width/2, -height/2, 
				width, height, Flip.NONE);
				
		tr.pop();
	}
	
	
	/**
	 * Constructor
	 */
	public Mirror() {
		
		sizeFactor = 0.0f;
	}
	
	
	/**
	 * Update mirror
	 * @param tm Time multiplier
	 */
	public void update(float tm) {
		
		final float SIZE_FACTOR_SPEED = 0.025f;

		// Update size factor
		sizeFactor += SIZE_FACTOR_SPEED * tm;
	}
	
	
	/**
	 * Draw mirror
	 * @param g Graphics object
	 */
	public void draw(Graphics g) {
		
		final float POS_X_P = 0.66f;
		final float POS_Y_P = 0.45f;
		final float WIDTH = 384.0f;
		final float HEIGHT = 384.0f;
		final float SCALE_MULTIPLIER1 = 0.05f;
		final float SCALE_MULTIPLIER2 = 0.1f;
		final float ALPHA_START = 0.3f;
		final float ALPHA_FACTOR = 0.1f;
		final float SHADOW_OFF = 16.0f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float x = view.x * POS_X_P - WIDTH/2;
		float y = view.y * POS_Y_P - HEIGHT/2;
		
		float cubeWidth = WIDTH / 256.0f * 128.0f;
		float cubeHeight = HEIGHT / 256.0f * 128.0f;
		
		float width = WIDTH;
		float height = HEIGHT;
		
		g.setColor();
		g.toggleAutocrop(false);
		
		// Calculate scale value
		float scaleFactor = (float)Math.sin(sizeFactor);
		float scaleValue1 = 1.0f + SCALE_MULTIPLIER1*scaleFactor;
		float scaleValue2 = 1.0f - SCALE_MULTIPLIER2*scaleFactor;
		
		// Draw shadow
		drawMirrorFrame(g, x + SHADOW_OFF, y + SHADOW_OFF,
				scaleFactor, scaleValue1, width, height, true);
		
		// Set transformations for the cube
		tr.push();
		tr.translate(x + width/2, y + height/2);
		tr.scale(scaleValue2, scaleValue2);
		tr.use();
		g.setColor();
		
		// Draw cube
		g.drawScaledBitmapRegion(bmpMirror, 256, 0, 128, 128, 
				-cubeWidth/2.0f, -cubeHeight/2.0f, 
				cubeWidth, cubeHeight, Flip.NONE);
		
		tr.pop();
		
		// Draw "glass"
		float alpha = ALPHA_START + ALPHA_FACTOR * scaleFactor;
		g.setColor(0, 0, 0.5f, alpha);
		g.fillRect(x+80, y+80, WIDTH-160, HEIGHT-160);
		g.setColor();
		

		// Draw mirror frame (base)
		drawMirrorFrame(g, x, y, scaleFactor, scaleValue1, width, height, false);
		
		g.toggleAutocrop(true);
	}
}
