package application.gamefield;

import application.global.Transition;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;
import core.utility.Tilemap;

/**
 * Game stage. Handles object creation &
 * some rendering (walls, floors, lava etc.)
 * @author Jani Nykänen.
 *
 */
public class Stage {

	/** An assumed tile size */
	static public final int INITIAL_TILE_SIZE = 128;
	
	/** Static solid tiles */
	static final boolean[] STATIC_SOLID_TILES = new boolean[] {
		true,false,true,true,false,false,true	
	};
	/** Solid tiles */
	static final boolean[] SOLID_TILES = new boolean[] {
		true,true,true,true,false,false,true	
	};
	
	
	/** Stage map */
	private Tilemap map;
	
	/** Current tile data */
	private int[] tileData;
	/** Solid data */
	private int[] solidData;
	
	/** Stage width in tiles */
	private int width;
	/** Stage height in tiles */
	private int height;
	
	/** Stage index */
	private int stageIndex;
	/** Stage name */
	private String stageName = "";
	/** Turn limit */
	private int turnLimit = 0;
	
	/** Static tile bitmap */
	private Bitmap bmpStatic;
	/** Lava bitmap */
	private Bitmap bmpLava;
	
	/** Lava phase */
	private float lavaPhase = 0.0f;
	
	/** Are purple tiles fading */
	private boolean purpleFading;
	/** Purple tile fading timer */
	private float purpleFadingTimer;
	/** Purple tile fading initial time */
	private float purpleInitialTime;
	
	/** Tile size*/
	private int tileSize = INITIAL_TILE_SIZE;
	
	/** Has the stage ended */
	private boolean stageEnded;
	
	
	/**
	 * Is the tile in (X,Y) solid in static sense
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return True or false
	 */
	private boolean isStaticSolid(int x, int y) {
		
		int t = getTile(x, y);
		if(t <= 0 || t-1 >= STATIC_SOLID_TILES.length) return false;
		
		return STATIC_SOLID_TILES[t-1];
	}
	
	
	/**
	 * Draw a piece of wall
	 * TODO: Missing things, do it! (...what?)
	 * @param g Graphics object
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 * @param value Value where to compare
	 * @param sw Tile width
	 * @param th Tile height
	 */
	private void drawConnectedTile(Graphics g, int x, int y, int value, int sw, int sh) {
		
		float dx = x * sw;
		float dy = y * sh;
		
		// Bottom-right corner
		if(getTile(x,y+1) != value && getTile(x+1,y) != value) {
					
			g.drawBitmapRegion(bmpStatic, sw / 2, sh / 2, sw / 2, sh / 2, dx+sw/2, dy+sh/2, Flip.NONE);
		}
		else if(getTile(x+1,y) == value && getTile(x,y+1) != value) {
			
			g.drawBitmapRegion(bmpStatic, sw / 4, sw / 2, sw / 2, sh/2, dx+sw/2, dy+sh/2, Flip.NONE);
		}
		else if(getTile(x,y+1) == value && getTile(x+1,y) != value) {
			
			g.drawBitmapRegion(bmpStatic, sw / 2, sh / 4, sw / 2, sh / 2, dx+sw/2, dy+sh/2, Flip.NONE);
		}
		
		
		
		// Bottom-left
		if(getTile(x,y+1) != value && getTile(x-1,y) != value) {
			
			g.drawBitmapRegion(bmpStatic, 0, sh / 2, sw / 2, sh / 2, dx, dy+sh/2, Flip.NONE);
		}
		else if(getTile(x-1,y) == value && getTile(x,y+1) != value) {
			
			g.drawBitmapRegion(bmpStatic, sw / 4, sh / 2, sw / 2, sh / 2, dx, dy+sh/2, Flip.NONE);
		}
		else if(getTile(x,y+1) == value && getTile(x-1,y) != value) {
			
			g.drawBitmapRegion(bmpStatic, 0, sh / 4, sw / 2, sh / 2, dx, dy+sh/2, Flip.NONE);
		}
		
		
		// Upper-right
		if(getTile(x,y-1) != value && getTile(x+1,y) != value) {
					
			g.drawBitmapRegion(bmpStatic, sw / 2, 0, sw / 2, sh / 2, dx+sw/2, dy, Flip.NONE);
		}
		else if(getTile(x+1,y) == value && getTile(x,y-1) != value) {
			
			g.drawBitmapRegion(bmpStatic, sw / 4, 0, sw / 2, sh / 2, dx+sw/2, dy, Flip.NONE);
		}
		else if(getTile(x,y-1) == value && getTile(x+1,y) != value) {
			
			g.drawBitmapRegion(bmpStatic, sw / 2, sh / 4, sw / 2, sh / 2, dx+sw/2, dy, Flip.NONE);
		}
		
		
		// Upper-left
		if(getTile(x,y-1) != value && getTile(x-1,y) != value) {
							
			g.drawBitmapRegion(bmpStatic, 0, 0, sw / 2, sh / 2, dx, dy, Flip.NONE);
		}
		else if(getTile(x-1,y) == value && getTile(x,y-1) != value) {
			
			g.drawBitmapRegion(bmpStatic, sw / 4, 0, sw / 2, sh / 2, dx, dy, Flip.NONE);
		}
		else if(getTile(x,y-1) == value && getTile(x-1,y) != value) {
			
			g.drawBitmapRegion(bmpStatic, 0, sh / 4, sw / 2, sh / 2, dx, dy, Flip.NONE);
		}
		
		
		
		// Bottom-right corner empty, but tiles close to it not
		if(getTile(x,y+1) == value && getTile(x+1,y) == value
		&& getTile(x+1,y+1) != value) {
							
			g.drawBitmapRegion(bmpStatic, sw + sw/2, sh / 2, sw / 2, sh / 2, dx+sw/2, dy+sh/2, Flip.NONE);
		}
		// Bottom-left corner empty ...
		if(getTile(x,y+1) == value && getTile(x-1,y) == value
		&& getTile(x-1,y+1) != value) {
									
			g.drawBitmapRegion(bmpStatic, sw, sh  / 2, sw / 2, sh / 2, dx, dy+sh/2, Flip.NONE);
		}
		// Upper-right ...
		if(getTile(x,y-1) == value && getTile(x+1,y) == value
		&& getTile(x+1,y-1) != value) {
							
			g.drawBitmapRegion(bmpStatic, sw + sw/2, 0, sw / 2, sh / 2, dx+sw/2, dy, Flip.NONE);
		}
		// Upper-left ...
		if(getTile(x,y-1) == value && getTile(x-1,y) == value
		&& getTile(x-1,y-1) != value) {
									
			g.drawBitmapRegion(bmpStatic, sw, 0, sw / 2, sh / 2, dx, dy, Flip.NONE);
		}
	}
	
	
	
	/**
	 * Draw borders
	 * Draw lava tile
	 * @param g Graphics object
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 * @param sw Tile width
	 * @param th Tile height
	 * @param bsize Border size
	 */
	private void drawBorders(Graphics g, int x, int y, int sw, int sh, float bsize) {
		
		float dx = x * sw;
		float dy = y * sh;
		
		// Right
		if(!isStaticSolid(x+1,y)) {
			
			g.fillRect(dx + sw-bsize, dy, bsize, sh);
		}
		// Left
		if(!isStaticSolid(x-1,y)) {
			
			g.fillRect(dx, dy, bsize, sh);
		}
		// Bottom
		if(!isStaticSolid(x,y+1)) {
					
			g.fillRect(dx , dy + sh-bsize, sw, bsize);
		}
		// Up
		if(!isStaticSolid(x,y-1)) {
					
			g.fillRect(dx , dy, sw, bsize);
		}
		
		// Bottom-right
		if(!isStaticSolid(x+1,y+1)) {
			
			g.fillRect(dx + sw-bsize, dy + sh-bsize, bsize, bsize);
		}
		// Bottom-left
		if(!isStaticSolid(x-1,y+1)) {
			
			g.fillRect(dx, dy + sh-bsize, bsize, bsize);
		}
		// Top-right
		if(!isStaticSolid(x+1,y-1)) {
			
			g.fillRect(dx + sw-bsize, dy, bsize, bsize);
		}
		// Top-left
		if(!isStaticSolid(x-1,y-1) ) {
			
			g.fillRect(dx, dy, bsize, bsize);
		}	
	}
	
	
	/**
	 * Draw a piece of floor
	 * @param g Graphics object
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 */
	private void drawFloor(Graphics g, int x, int y) {
		
		g.drawScaledBitmapRegion(bmpStatic, 0, 128, 128, 128, 
				x*tileSize, y*tileSize, tileSize, tileSize, Flip.NONE);
	}
	
	
	/**
	 * Draw lava tile
	 * @param g Graphics object
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 * @param phase Movement phase
	 */
	private void drawLava(Graphics g, int x, int y, float phase) {
		
		final float COLOR_MOD = 0.125f;
		final float BORDER_WIDTH = 8.0f;
		
		// Draw lava tile
		int tw = (int) (phase * (bmpLava.getWidth() / 2.0f));
		int th = (int) ( (1.0f-phase) * (bmpLava.getHeight() / 2.0f));
		float color = 1.0f + COLOR_MOD * (float)Math.sin(phase * Math.PI * 2.0f);

		g.setColor(color, color, color);
		g.drawScaledBitmapRegion(bmpLava, tw, th, 128, 128, 
				x*tileSize, y*tileSize, tileSize, tileSize, Flip.NONE);
		
		// Draw borders
		g.setColor(0.40f,0.0f,0.0f);
		drawBorders(g, x, y, tileSize, tileSize, BORDER_WIDTH);
	}
	
	
	/**
	 * Draw a single tile
	 * @param g Graphics object
	 * @param tile Tile index
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 */
	private void drawTile(Graphics g, int tile, int x, int y) {
		
		final float LOCK_BORDER_WIDTH = 8.0f;
		
		float dx = x * tileSize;
		float dy = y * tileSize;
		
		// Calculate purple tile alpha
		float purpleAlpha = 1.0f;
		if(purpleFading) {
			
			purpleAlpha = purpleFadingTimer / purpleInitialTime;
		}
		
		g.setColor();
		switch(tile) {

		// Wall
		case 1:
			
			// Draw background white
			g.fillRect(dx, dy, tileSize, tileSize);
			
			// Draw borders
			drawConnectedTile(g, x, y, 1, tileSize, tileSize);
			
			break;
			
		// Lava
		case 3:
			drawLava(g, x, y, lavaPhase);
			break;
			
		// Lock
		case 4:
			
			// Tile background
			g.drawScaledBitmapRegion(bmpStatic, 256, 128, 128, 128, 
					dx, dy, tileSize, tileSize, Flip.NONE);
			
			// Borders
			g.setColor(0.25f,0.10f,0.0f);
			drawBorders(g, x, y, tileSize, tileSize, LOCK_BORDER_WIDTH);
			
			break;
			
		// Purple wall, deactive
		case 6:
			
			// Draw base floor tile
			g.drawScaledBitmapRegion(bmpStatic, 128, 128, 128, 128, 
					dx, dy, tileSize, tileSize, Flip.NONE);
			
			// If purple fading, fade in the replacing tile
			if(purpleFading) {
				
				g.setColor(1, 1, 1, purpleAlpha);
				
				// Draw background
				g.drawScaledBitmapRegion(bmpStatic, 288, 32, 64, 64, 
						dx, dy, tileSize, tileSize, Flip.NONE);

				// Draw borders
				g.setSourceTranslation(256, 0);
				drawConnectedTile(g, x, y, 6, tileSize, tileSize);
				g.setSourceTranslation(0, 0);
			}
			
			
			break;
			
		// Purple wall, active
		case 7:
			
			// If purple fading, draw the replacing tile
			if(purpleFading) {
				
				g.drawScaledBitmapRegion(bmpStatic, 128, 128, 128, 128, 
						dx, dy, tileSize, tileSize, Flip.NONE);
				
				g.setColor(1, 1, 1, 1.0f - purpleAlpha);
			}

			// Draw background
			g.drawScaledBitmapRegion(bmpStatic, 288, 32, 64, 64, 
					dx, dy, tileSize, tileSize, Flip.NONE);

			// Draw borders
			g.setSourceTranslation(256, 0);
			drawConnectedTile(g, x, y, 7, tileSize, tileSize);
			g.setSourceTranslation(0, 0);

			break;
			
		// Floor
		default:
			
			drawFloor(g, x, y);
			break;
		}
		
		g.setColor();
	}
	
	
	/**
	 * Constructor
	 * @param assets Asset package
	 */
	public Stage(AssetPack assets) {
	
		// Get assets
		bmpStatic = assets.getBitmap("static");
		bmpLava = assets.getBitmap("lava");
	}
	
	
	/**
	 * Reset a map
	 */
	public void resetMap() {
		
		// Get required information
		width = map.getWidth();
		height = map.getHeight();
		stageName = map.getProperty("name");
		String turnString = map.getProperty("turns");
		turnLimit = turnString == null ? 0 : Integer.parseInt(turnString);

		// Copy bottom layer to tile data
		// (note: copy only static data)
		tileData = new int[width*height];
		int tile = 0;
		for(int i = 0; i < width*height; ++ i) {
			
			tile = map.getTileValue(0, i % width, i / width);
			if(tile == 1 || tile == 3 || tile == 4 || tile == 6 || tile == 7)
				tileData[i] = tile;
			else
				tileData[i] = 0;
		}
		// Clone tile data to solid data
		solidData = tileData.clone();
		
		// Set flags
		stageEnded = false;
	}
	
	
	/**
	 * Set the stage tilemap ready to be used
	 * @param index Stage index
	 */
	public void setStage(int index, AssetPack assets) {
		
		stageIndex = index;
		
		// Get the tilemap
		map = assets.getTilemap(Integer.toString(index));
		
		resetMap();
	}
	
	
	/**
	 * Update the stage
	 * @param tm Time multiplier
	 * @param trans Transitions
	 */
	public void update(float tm) {
		
		final float LAVA_SPEED = 0.005f;
		
		// Update lava
		lavaPhase += LAVA_SPEED * tm;
		if(lavaPhase >= 1.0f)
			lavaPhase -= 1.0f;
	
		// Update purple tile fading
		if(purpleFading) {
			
			purpleFadingTimer -= 1.0f * tm;
			if(purpleFadingTimer <= 0.0f) {
				
				purpleFading = false;
			}
		}
	}
	
	
	/**
	 * Set stage transformations
	 * @param g Graphics object
	 * @param transActive Is transition active
	 * @param mode Transition mode
	 * @param transTime Transition time in scale [0, 1]
	 */
	public void setTransform(Graphics g, boolean transActive, Transition.Mode mode, float transTime) {
		
		final float SCALE_IN_FACTOR = 0.25f;
		final float SCALE_OUT_FACTOR = 0.75f;
		
		// Set camera
		Transformations tr = g.transform();
		tr.identity();
		// Fit tilemap height to the screen
		tr.fitViewHeight(height * tileSize);
				
		// Scale, if transitioning
		if(transActive) {
			
			float t = 1.0f-transTime;
			float scale = mode == Transition.Mode.Out 
					? (1.0f-SCALE_IN_FACTOR) + t*SCALE_IN_FACTOR :  
					   1.0f + t * SCALE_OUT_FACTOR;
			Vector2 center = tr.getViewport().clone();
			center.x /= 2;
			center.y /= 2;
			
			tr.translate(center.x, center.y);
			tr.scale(scale, scale);
			tr.translate(-center.x, -center.y);
		}
		
		// Calculate x translation
		float xtrans = tr.getViewport().x / 2 - width*tileSize/2;
		tr.translate(xtrans, 0);
		tr.use();
	}
	
	
	/**
	 * Draw the stage
	 * @param g Graphics object
	 */
	public void draw(Graphics g) {
		
		// Draw static tiles
		for(int y = 0; y < height; ++ y) {
			
			for(int x = 0; x < width; ++ x) {
				
				drawTile(g, getTile(x, y), x, y);
			}
		}
	}
	
	
	/**
	 * Create non-static object
	 * @param oman Object manager
	 */
	public void createObjects(ObjectManager oman) {
		
		oman.parseMap(this, map.copyLayer(0), width, height);
	}

	
	/**
	 * Get width
	 * @return Width
	 */
	public int getWidth() {
		
		return width;
	}
	
	
	/**
	 * Get height
	 * @return Height
	 */
	public int getHeight() {
		
		return height;
	}
	
	
	/**
	 * Update tile data
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param value New value
	 */
	public void updateTileData(int x, int y, int value) {
		
		updateTileData(y*width + x, value);
	}
	
	
	/**
	 * Update tile data
	 * @param i Tile index
	 * @param value New value
	 */
	public void updateTileData(int i, int value) {
		
		if(i < 0 || i >= tileData.length) return;
		
		tileData[i] = value;
		solidData[i] = value;
	}
	
	
	/**
	 * Update solid tile data
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param value New value
	 */
	public void updateSolidTileData(int x, int y, int value) {
		
		int i = y * width + x ;
		if(i < 0 || i >= solidData.length) return;
		
		solidData[i] = value;
	}
	
	
	/**
	 * Is the tile in (X,Y) solid
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return True or false
	 */
	public boolean isSolid(int x, int y) {
		
		int t = getSolidTile(x, y);
		if(t <= 0 || t-1 >= SOLID_TILES.length) return false;
		
		return SOLID_TILES[t-1];
	}
	
	
	/**
	 * Is the tile in (X,Y) solid (exclude lava)
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return True or false
	 */
	public boolean isSolidExcludeLava(int x, int y) {
		
		int t = getSolidTile(x,y);
		if(t <= 0 || t-1 >= SOLID_TILES.length) return false;
		
		if(t == 3) return false;
		
		return SOLID_TILES[t-1];
	}
	
	
	/**
	 * Get a tile value in the current tile data array
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Tile value
	 */
	public int getTile(int x, int y) {
		
		int index = y*width + x;
		if(index < 0 || index >= tileData.length)
			return 1;
		
		return tileData[index];
	}
	
	
	/**
	 * Get tile value in the current tile data array
	 * @param i Index
	 * @return Tile value
	 */
	public int getTile(int i) {
		
		if(i < 0 || i >= tileData.length)
			return i;
		
		return tileData[i];
	}
	
	
	/**
	 * Get a tile "solidity" value in the current tile data array
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Tile value
	 */
	public int getSolidTile(int x, int y) {
		
		int index = y*width + x;
		if(index < 0 || index >= solidData.length)
			return 1;
		
		return solidData[index];
	}
	
	
	/**
	 * Toggle purple blocks
	 */
	public void togglePurpleBlocks() {
		
		int tileID = 0;
		for(int i = 0; i < width*height; ++ i) {
			
			tileID = getTile(i);
			if(tileID == 6)
				updateTileData(i, 7);
			else if(tileID == 7)
				updateTileData(i, 6);
		}
	}
	
	
	/**
	 * Start purple fading
	 * @param time Time
	 */
	public void startPurpleTileFading(float time) {
		
		purpleFading = true;
		purpleFadingTimer = time;
		purpleInitialTime = time;
		
		togglePurpleBlocks();
	}
	
	
	/**
	 * Get stage index
	 * @return Stage index
	 */
	public int getStageIndex() {
		
		return stageIndex;
	}
	
	
	/**
	 * Get stage name
	 * @return Stage name 
	 */
	public String getStageName() {
		
		return stageName;
	}
	
	
	/**
	 * Get turn limit
	 * @return Turn limit
	 */
	public int getTurnLimit() {
		
		return turnLimit;
	}
	
	
	/**
	 * Toggle "stage ended" flag on
	 */
	public void endStage() {
		
		stageEnded = true;
	}
	
	
	/**
	 * Get the "stage ended" flag value and disable the flag
	 * @return True or false
	 */
	public boolean hasStageEnded() {
		
		boolean ret = stageEnded;
		stageEnded = false;
		return ret;
	}
}
