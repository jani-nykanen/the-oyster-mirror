package application.gamefield;

import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.utility.AssetPack;

/**
 * Handles status managing, mostly drawing
 * status info (items, turns etc.)
 * @author Jani Nykänen
 *
 */
public class StatusManager {

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
		
		final float XOFF = -26.0f;
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
		g.drawText(bmpFont, "Stage " + Integer.toString(stageIndex) + ": " + stageName,
				tx + STAGE_X, ty + TEXT_Y, XOFF, YOFF, false, FONT_SCALE);
		

		// Create turn strings
		String turnStr1 = "Turn: ";
		String turnStr2 =  Integer.toString(turnCount);
		String turnStr3 = " (" + Integer.toString(turnLimit) + ")"; 	
		
		// Calculate positions
		float pos1 = getDrawnTextPos(g, turnStr1+turnStr2+turnStr3, bmpFont, XOFF, FONT_SCALE);
		float pos2 = getDrawnTextPos(g, turnStr2+turnStr3, bmpFont, XOFF, FONT_SCALE);
		float pos3 = getDrawnTextPos(g, turnStr3, bmpFont, XOFF, FONT_SCALE);

		// Draw texts
		g.drawText(bmpFont, turnStr1,
				tx + pos1, ty + TEXT_Y, XOFF, YOFF, false, FONT_SCALE);
		
		// Switch to special color if too many turns taken
		if(!shadow) {
			
			if(turnCount > turnLimit)
				g.setColor(1, 0.25f, 0);
		}
		
		g.drawText(bmpFont, turnStr2,
				tx + pos2, ty + TEXT_Y, XOFF, YOFF, false, FONT_SCALE);
		
		if(!shadow)
			g.setColor(1, 1, 0);
		g.drawText(bmpFont, turnStr3,
				tx + pos3 , ty + TEXT_Y, XOFF, YOFF, false, FONT_SCALE);
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
	public void update(Player pl, TimeManager tman) {
		
		turnCount = tman.getTurn();
		keyCount = pl.getKeyCount();
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
	}
}
