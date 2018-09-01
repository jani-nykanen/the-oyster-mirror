package application.gamefield;

import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Vector2;
import core.utility.AssetPack;

/**
 * Handles status managing, mostly drawing
 * status info (items, turns etc.)
 * @author Jani Nykänen
 *
 */
public class StatusManager {

	/** Appearance time */
	static private final float APPEARANCE_TIME = 30.0f;
	
	/** Font bitmap */
	static private Bitmap bmpFont;
	/** Collectibles bitmap */
	static private Bitmap bmpCollectibles;
	
	/** The amount of keys */
	private int keyCount;
	/** The amount of turns taken */
	private int turnCount;
	/** Turn limit */
	private int turnLimit;
	/** Stage name */
	private String stageName;
	/** Stage index */
	private int stageIndex;
	
	/** Key appearance timer*/
	private float keyAppearanceTimer = 0.0f;
	/** Old key count */
	private int oldKeyCount = 0;
	
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
	 * Get drawn text position
	 * @param g
	 * @param str
	 * @param font
	 * @param xoff
	 * @param scale Float scale
	 * @return Position
	 */
	private float getDrawnTextPos(Graphics g, String str, Bitmap font, float xoff, float scale) {
		
		return g.transform().getViewport().x - (str.length() +1) * (bmpFont.getWidth()/16.0f + xoff) * scale;
	}
	
	
	/**
	 * Draw status text
	 * @param g Graphics object
	 * @param tx Translation x
	 * @param ty Translation y
	 */
	private void drawText(Graphics g, float tx, float ty, boolean shadow) {
		
		final float XOFF1 = -28.0f;
		final float XOFF2 = -22.0f;
		final float YOFF = 0;
		final float FONT_SCALE = 0.75f;
		final float TEXT_Y = 10.0f;
		final float STAGE_X = 8.0f;
		final float SHADOW_ALPHA = 0.5f;
		
		if(shadow)
			g.setColor(0,0,0, SHADOW_ALPHA);
		else {
			
			g.setColor(1, 1, 0);
		}

		// Draw stage index
		float viewx = g.transform().getViewport().x;
		String sname = "";
		if( (stageName.length()-1) * ( (64.0f-XOFF1) * FONT_SCALE) > viewx)
			sname = stageName;
		else
			sname = "Stage " + Integer.toString(stageIndex) + ": " + stageName;
			
		g.drawText(bmpFont, sname,
				tx + STAGE_X, ty + TEXT_Y, XOFF1, YOFF, false, FONT_SCALE);
		

		// Create turn strings
		String turnStr1 = "Turn: ";
		String turnStr2 =  Integer.toString(turnCount);
		String turnStr3 = "  (~" + Integer.toString(turnLimit) + ")"; 	
		
		// Calculate positions
		float pos1 = getDrawnTextPos(g, turnStr1+turnStr2+turnStr3, bmpFont, XOFF1, FONT_SCALE);
		float pos2 = getDrawnTextPos(g, turnStr2+turnStr3, bmpFont, XOFF1, FONT_SCALE);
		float pos3 = getDrawnTextPos(g, turnStr3, bmpFont, XOFF2, FONT_SCALE);

		// Draw texts
		g.drawText(bmpFont, turnStr1,
				tx + pos1, ty + TEXT_Y, XOFF1, YOFF, false, FONT_SCALE);
		
		// Switch to special color if too many turns taken
		if(!shadow) {
			
			if(turnCount > turnLimit)
				g.setColor(1, 0.5f, 0);
		}
		
		g.drawText(bmpFont, turnStr2,
				tx + pos2, ty + TEXT_Y, XOFF1, YOFF, false, FONT_SCALE);
		
		// Switch to special color if too many turns taken
		if(!shadow) {
					
			if(turnCount > turnLimit)
				g.setColor(0.75f, 0.75f, 0.75f);
			else
				g.setColor(1, 1, 0);
		}
		g.drawText(bmpFont, turnStr3,
				tx + pos3 , ty + TEXT_Y, XOFF2, YOFF, false, FONT_SCALE);
	}
	
	
	/**
	 * Draw collected items
	 * @param g Graphics objects
	 */
	public void drawItems(Graphics g) {
		
		final float START_Y = 64.0f;
		final float POS_X = 0.0f;
		final float SCALE = 0.75f;
		final float YOFF_FACTOR = 0.75f;
		
		float dw = SCALE * 128.0f;
		float dh = SCALE * 128.0f;
		
		float yoff = dh * YOFF_FACTOR;
		
		for(int i = 0; i < (int)Math.min(keyCount, oldKeyCount); ++ i) {
			
			g.drawScaledBitmapRegion(bmpCollectibles, 0, 0, 128, 128, 
					POS_X, START_Y + yoff*i, dw, dh, Flip.NONE);
		}
		
		// Draw (dis)appearing key
		if(oldKeyCount != keyCount) {
			
			// Calculate alpha & position
			float t = keyAppearanceTimer / APPEARANCE_TIME;
			
			float alpha, posx;	
			if(oldKeyCount < keyCount) {
				
				alpha = 1.0f - t;
				posx = POS_X - dw + dw*(1.0f-t);
			}
			else {
				
				alpha = t;
				posx = POS_X - dw + dw*t;
			}
			
			int index = oldKeyCount < keyCount ? oldKeyCount : oldKeyCount-1;
			
			
			g.setColor(1, 1, 1, alpha);
			g.drawScaledBitmapRegion(bmpCollectibles, 0, 0, 128, 128, 
					posx, START_Y + yoff*index, dw, dh, Flip.NONE);
			g.setColor();
		}
	}
	
	
	/**
	 * Constructor
	 */
	public StatusManager() {
		
		// ...
	}
	
	
	/**
	 * Set initial values
	 * @param stage Stage
	 */
	public void setInitial(Stage stage) {
		
		stageName = stage.getStageName();
		turnLimit = stage.getTurnLimit();
		stageIndex = stage.getStageIndex();
		
		keyCount = 0;
		turnCount = 0;
	}
	
	
	/**
	 * Update
	 * @param pl Player
	 * @param tman Time manager
	 */
	public void update(Player pl, TimeManager tman, float tm) {
		
		turnCount = tman.getTurn();
		
		// Check if more keys collected
		int oldKeys = keyCount;
		keyCount = pl.getKeyCount();
		if(keyCount != oldKeys) {
			
			oldKeyCount = oldKeys;
			keyAppearanceTimer = APPEARANCE_TIME;
		}
		
		// Update key appearance timer
		if(keyAppearanceTimer > 0.0f) {
			
			keyAppearanceTimer -= 1.0f * tm;
			if(keyAppearanceTimer <= 0.0f) {
				
				oldKeyCount = keyCount;
			}
		}
	}
	
	
	/**
	 * Draw
	 * @param g Graphics object
	 */
	public void draw(Graphics g) {
		
		final float SHADOW_OFF = 6.0f;
		
		// Draw shadow
		drawText(g, SHADOW_OFF, SHADOW_OFF, true);
		
		// Draw opaque
		drawText(g, 0, 0, false);
		
		g.setColor();
		
		// Draw items
		drawItems(g);
	}
}
