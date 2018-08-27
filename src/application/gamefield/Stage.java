package application.gamefield;

import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
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
	static public final int TILE_SIZE = 128;
	
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
	
	/** Stage width in tiles */
	private int width;
	/** Stage height in tiles */
	private int height;
	
	/** Stage index */
	private int stageIndex;
	
	/** Static tile bitmap */
	private Bitmap bmpStatic;
	/** Lava bitmap */
	private Bitmap bmpLava;
	
	/** Lava phase */
	private float lavaPhase = 0.0f;
	
	
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
				x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, Flip.NONE);
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
				x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE, Flip.NONE);
		
		// Draw borders
		g.setColor(0.40f,0.0f,0.0f);
		drawBorders(g, x, y, TILE_SIZE, TILE_SIZE, BORDER_WIDTH);
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
		
		float dx = x * TILE_SIZE;
		float dy = y * TILE_SIZE;
		
		g.setColor();
		switch(tile) {

		// Wall
		case 1:
			
			// Draw background white
			g.fillRect(dx, dy, TILE_SIZE, TILE_SIZE);
			
			// Draw borders
			drawConnectedTile(g, x, y, 1, TILE_SIZE, TILE_SIZE);
			
			break;
			
		// Lava
		case 3:
			drawLava(g, x, y, lavaPhase);
			break;
			
		// Lock
		case 4:
			
			// Tile background
			g.drawScaledBitmapRegion(bmpStatic, 256, 128, 128, 128, 
					dx, dy, TILE_SIZE, TILE_SIZE, Flip.NONE);
			
			// Borders
			g.setColor(0.25f,0.10f,0.0f);
			drawBorders(g, x, y, TILE_SIZE, TILE_SIZE, LOCK_BORDER_WIDTH);
			
			break;
			
		// Purple wall, not active
		case 6:
			
			
			g.drawScaledBitmapRegion(bmpStatic, 128, 128, 128, 128, 
					dx, dy, TILE_SIZE, TILE_SIZE, Flip.NONE);
			break;
			
		// Purple wall, active
		case 7:
			
			// Draw background
			g.drawScaledBitmapRegion(bmpStatic, 288, 32, 64, 64, 
					dx, dy, TILE_SIZE, TILE_SIZE, Flip.NONE);

			// Draw borders
			g.setSourceTranslation(256, 0);
			drawConnectedTile(g, x, y, 7, TILE_SIZE, TILE_SIZE);
			g.setSourceTranslation(0, 0);

			break;
			
		// Floor
		default:
			
			drawFloor(g, x, y);
			break;
		}
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
	 * Load a map from a file
	 * @param index Stage index
	 * @throws Exception If does not exist
	 */
	public void loadMap(int index) throws Exception {
	
		stageIndex = index;
		
		// Open file
		String path = "assets/tilemaps/" + index + ".tmx";
		map = new Tilemap(path);
		
		// Get required information
		width = map.getWidth();
		height = map.getHeight();
		
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
	}
	
	
	/**
	 * Update the stage
	 * @param tm Time multiplier
	 */
	public void update(float tm) {
		
		final float LAVA_SPEED = 0.005f;
		
		// Update lava
		lavaPhase += LAVA_SPEED * tm;
		if(lavaPhase >= 1.0f)
			lavaPhase -= 1.0f;
	}
	
	
	/**
	 * Set stage transformations
	 * @param g Graphics object
	 */
	public void setTransform(Graphics g) {
		
		// Set camera
		Transformations tr = g.transform();
		tr.identity();
		// Fit tilemap height to the screen
		tr.fitViewHeight(height * TILE_SIZE);
				
		// Calculate x translation
		float xtrans = tr.getViewport().x / 2 - width*TILE_SIZE/2;
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
		
		if(y*width +x < 0 || y*width+x >= width*height) return;
		
		tileData[y*width+x] = value;
	}
	
	
	/**
	 * Is the tile in (X,Y) solid
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return True or false
	 */
	public boolean isSolid(int x, int y) {
		
		int t = getTile(x, y);
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
		
		int t = getTile(x,y);
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
}
