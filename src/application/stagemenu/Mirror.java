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
	/** Icons bitmap */
	static private Bitmap bmpIcons;

	
	/** Size factor */
	private float sizeFactor = 0.0f;
	
	/** Cursor position */
	private Vector2 cursorPos;
	/** Pointer position */
	private Vector2 pointerPos;
	/** Cursor floating factor */
	private float floatFactor;
	/** Has the pointer been initialized (i.e initial pos set) */
	private boolean pointerInitialized;
	
	/** Width */
	private float width;
	/** Height */
	private float height;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		// Get bitmaps
		bmpMirror = assets.getBitmap("mirror");
		bmpIcons = assets.getBitmap("icons");
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
	 * Draw cursor
	 * @param g Graphics object
	 * @param dx Destination x
	 * @param dy Destination y
	 */
	private void drawCursor(Graphics g, float dx, float dy) {

		final float MARKER_SCALE_X= 2.0f;
		final float MARKER_SCALE_Y = 1.0f;
		final float CURSOR_SCALE = 1.5f;
		
		final float FLOAT_AMPLITUDE = 8.0f;
		
		// Draw marker
		float w = 64.0f * MARKER_SCALE_X;
		float h = 64.0f * MARKER_SCALE_Y;
		float x = dx + cursorPos.x - w/2;
		float y = dy + cursorPos.y - h/2;
		g.drawScaledBitmapRegion(bmpIcons, 64, 0, 64, 64, 
				x,y, w,h, Flip.NONE);
		
		// Draw pointer
		float cursorOff = -FLOAT_AMPLITUDE + (float)Math.sin(floatFactor) * FLOAT_AMPLITUDE;
		w = 64.0f * CURSOR_SCALE;
		h = 64.0f * CURSOR_SCALE;
		x = dx + pointerPos.x - w/2;
		y = dy + pointerPos.y - h + cursorOff;
		g.drawScaledBitmapRegion(bmpIcons, 0, 0, 64, 64, 
				x, y, w, h, Flip.NONE);
		
	}
	
	
	/**
	 * Update pointer
	 * @param tm Time mul.
	 */
	private void updatePointer(float tm) {
		
		final float POINTER_SPEED_FACTOR = 16.0f;
		final float POINTER_DELTA = 1.0f;
		
		// Calculate distance
		float dist = (float)Math.hypot(cursorPos.x-pointerPos.x, 
				cursorPos.y-pointerPos.y);
		
		// If far enough, start moving
		if(dist > POINTER_DELTA) {
			
			float speed = dist / POINTER_SPEED_FACTOR;
			
			float angle = (float)Math.atan2(cursorPos.y-pointerPos.y, 
					cursorPos.x-pointerPos.x);
			
			float sx = (float)Math.cos(angle);
			float sy = (float)Math.sin(angle);
			
			pointerPos.x += sx * speed * tm;
			pointerPos.y += sy * speed * tm;
		}
		else {
			
			pointerPos.x = cursorPos.x;
			pointerPos.y = cursorPos.y;
		}
		
	}
	
	
	/**
	 * Constructor
	 * @param w Width
	 * @param h Height
	 */
	public Mirror(float w, float h) {
		
		// Set defaults
		sizeFactor = 0.0f;
		cursorPos = new Vector2();
		pointerPos = new Vector2();
		floatFactor = 0.0f;
		pointerInitialized = false;
		
		// Store dimensions
		width = w;
		height = h;
	}
	
	
	/**
	 * Update mirror
	 * @param tm Time multiplier
	 */
	public void update(float tm) {
		
		final float SIZE_FACTOR_SPEED = 0.025f;
		final float FLOAT_FACTOR_SPEED = 0.05f;

		// Update size factor
		sizeFactor += SIZE_FACTOR_SPEED * tm;
		
		// Update floating factor
		floatFactor += FLOAT_FACTOR_SPEED * tm;
	
		// Update pointer
		updatePointer(tm);
	}
	
	
	/**
	 * Draw mirror
	 * @param g Graphics object
	 */
	public void draw(Graphics g) {
		
		final float POS_X_P = 0.66f;
		final float POS_Y_P = 0.45f;
		final float SCALE_MULTIPLIER1 = 0.05f;
		final float SCALE_MULTIPLIER2 = 0.1f;
		final float ALPHA_START = 0.3f;
		final float ALPHA_FACTOR = 0.1f;
		final float SHADOW_OFF = 16.0f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float x = view.x * POS_X_P - width/2;
		float y = view.y * POS_Y_P - height/2;
		
		float cubeWidth = width / 256.0f * 128.0f;
		float cubeHeight = height / 256.0f * 128.0f;

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
		g.setColor(0, 0.2f, 0.5f, alpha);
		g.fillRect(x+80, y+80, width-160, height-160);
		g.setColor();
		
		// Draw mirror frame (base)
		drawMirrorFrame(g, x, y, scaleFactor, scaleValue1, width, height, false);
		
		g.toggleAutocrop(true);
		
		// Draw cursor
		if(pointerInitialized)
			drawCursor(g, x, y);
	}


	/**
	 * Set cursor position
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void setCursorPosition(float x, float y) {
		
		if(!pointerInitialized) {
			
			pointerPos.x = x;
			pointerPos.y = y;
			pointerInitialized = true;
		}
		
		cursorPos.x = x;
		cursorPos.y = y;
	}
	
	
	/**
	 * Calculate cursor position to be in a spiral
	 * @param n Amount of intervals
	 * @param k Current position
	 */
	public void calculateCursorPosition(int n, int k) {
		
		final float RADIUS = 256.0f;
		
		float t = (float)(n-k) * ( ( (float)Math.PI * 2.0f) / (float)n );
		
		float x = (float)Math.cos(t * 4.0f) * 0.1f * t * RADIUS;
		float y = (float)Math.sin(t * 4.0f) * 0.1f * t * RADIUS;

		x += width / 2;
		y += height / 2;
		
		setCursorPosition(x, y);
	}
}
