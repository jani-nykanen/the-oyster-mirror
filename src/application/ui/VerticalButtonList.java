package application.ui;

import java.util.ArrayList;
import java.util.List;

import application.Gamepad;
import core.State;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Direction;
import core.types.Vector2;
import core.utility.AssetPack;

/**
 * A vertical list of buttons
 * @author Jani Nykänen
 *
 */
public class VerticalButtonList {

	/** Time required cursor to move from button to button */
	static private final float CURSOR_TIME = 15.0f;
	
	/** Cursor bitmap */
	static private Bitmap bmpCursor;
	
	/** Amount of buttons */
	private int length;
	
	/** A list of buttons */
	private List<Button> buttons;
	
	/** Cursor position */
	private int cursorPos;
	/** Old position */
	private int oldPos;
	/** Cursor move timer */
	private float cursorTimer;
	/** Is the cursor moving */
	private boolean cursorMoving;
	
	
	/**
	 * Initialize global content
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		bmpCursor = assets.getBitmap("cursor");
	}
	
	
	/**
	 * Constructor
	 */
	public VerticalButtonList() {
		
		length = 0;
		buttons = new ArrayList<Button> ();
		
		// Set defaults
		cursorMoving = false;
		cursorTimer = 0.0f;
		cursorPos = 0;
	}
	
	
	/**
	 * Add a new button
	 * @param b Button
	 */
	public void addButton(Button b) {
		
		buttons.add(b);
		++ length;
	}
	
	
	/**
	 * Get a button
	 * @param index Button index
	 * @return Button
	 */
	public Button getButton(int index) {
		
		if(index < 0 || index >= length) 
			return null;
		
		return buttons.get(index);
	}
	
	
	
	/**
	 * Update button list
	 * @param vpad Game pad
	 * @param tm Time mul.
	 */
	public void update(Gamepad vpad, float tm) {
		
		final float DELTA_LIMIT = 0.25f;
		
		// Update cursor
		if(cursorMoving) {
			
			cursorTimer -= 1.0f * tm;
			if(cursorTimer <= 0.0f) {
				
				cursorMoving = false;
				cursorTimer = 0.0f;
			}
		}
		else {
			
			// Get stick direction
			Direction dir = vpad.getDirection();
			int opos = cursorPos;
			if(cursorPos < length-1 &&  dir == Direction.Down) {
				
				++ cursorPos;
			}
			else if(cursorPos > 0 && dir == Direction.Up) {
				
				-- cursorPos;
			}
			
			// If changed, move
			if(opos != cursorPos) {
				
				cursorTimer = CURSOR_TIME;
				cursorMoving = true;
				
				oldPos = opos;
				return;
			}
			
			// Check action buttons
			if(vpad.getButtonByName("confirm") == State.Pressed ||
			   vpad.getButtonByName("fire1") == State.Pressed) {
				
				buttons.get(cursorPos).activate(cursorPos);
			}
			
			// Check directional stick movement
			Vector2 delta = vpad.getDelta();
			Vector2 stick = vpad.getStick();
			if(delta.x > DELTA_LIMIT && stick.x > 0.0f) {
				
				buttons.get(cursorPos).activate(Direction.Right, cursorPos);
			}
			else if(delta.x < -DELTA_LIMIT && stick.x < 0.0f) {
				
				buttons.get(cursorPos).activate(Direction.Left, cursorPos);
			}
		}
	}
	
	
	/**
	 * Draw button list
	 * @param g Graphics object
	 * @param bmp Bitmap font
	 * @param dx Starting position x
	 * @param dy Starting position y
	 * @param xoff X offset
	 * @param yoff Y offset
	 * @param scale Scale
	 * @param center Whether to center the text
	 * @param shadow Shadow offset
	 */
	public void draw(Graphics g, Bitmap bmp, float dx, float dy, float xoff, float yoff, float scale, boolean center, float shadow) {
		
		final float SHADOW_ALPHA = 0.5f;
		
		// Calculate "factor value"
		float t = 1.0f - cursorTimer / CURSOR_TIME;
		
		// Draw text
		float tfactor = 0.0f;
		Button b;
		for(int i = 0; i < length; ++ i) {
			
			b = buttons.get(i);
			
			if(cursorPos == i)
				tfactor = t ;
			
			else if(oldPos == i)
				tfactor = (1.0f-t);
			
			else
				tfactor = 0.0f;
			
			// Draw shadow
			g.setColor(0, 0, 0, SHADOW_ALPHA);
			g.drawText(bmp, b.getText(), 
					shadow + dx + tfactor*yoff, 
					shadow + dy + i * yoff, xoff, 
					0.0f, center, scale);
			
			// Set color, gray if disabled, otherwise
			// yellow
			if(b.isDisabled())
				g.setColor(0.75f, 0.75f, 0.75f);
			else
				g.setColor(2.0f - tfactor, 2.0f - tfactor, 1.0f - tfactor);
			// Draw text
			g.drawText(bmp,b.getText(), dx + tfactor*yoff, 
					dy + i * yoff, xoff, 0.0f, center, scale);
		}
		
		
		// Calculate cursor position
		float size = yoff ;
		float cpos = dy + (oldPos * (1.0f-t) + cursorPos * t) * yoff;
		
		// Draw cursor shadow
		g.setColor(0, 0, 0, SHADOW_ALPHA);
		g.drawScaledBitmapRegion(bmpCursor, -2, 0, 132, 128, 
				dx + shadow, cpos + shadow, size, size, Flip.NONE);
		
		// Draw cursor
		g.setColor();
		g.drawScaledBitmapRegion(bmpCursor, -2, 0, 132, 128, dx, cpos, size, size, Flip.NONE);
	}
	
	
	/**
	 * Reset cursor
	 */
	public void resetCursor() {
		
		setCursorPos(0);
	}
	
	
	/**
	 * Set cursor position
	 * @param pos Position
	 */
	public void setCursorPos(int pos) {
		
		if(pos < 0 || pos >= buttons.size())
			pos = 0;
		
		cursorPos = pos;
		oldPos = pos;
		cursorTimer = 0.0f;
	}
}
