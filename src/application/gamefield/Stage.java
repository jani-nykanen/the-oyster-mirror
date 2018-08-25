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
	static final int TILE_SIZE = 128;
	
	
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
	
	
	/**
	 * Get a tile value in the current tile data array
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Tile value
	 */
	private int getTile(int x, int y) {
		
		int index = y*width + x;
		if(index < 0 || index >= tileData.length)
			return 1;
		
		return tileData[index];
	}
	
	
	/**
	 * Draw a piece of wall
	 * TODO: This is so very ugly...
	 * @param g Graphics object
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 */
	private void drawWall(Graphics g, int x, int y) {
		
		float dx = x * TILE_SIZE;
		float dy = y * TILE_SIZE;
		
		// Draw background white
		g.setColor();
		g.fillRect(dx, dy, TILE_SIZE, TILE_SIZE);
		
		// Bottom-right corner
		if(getTile(x,y+1) != 1 && getTile(x+1,y) != 1) {
					
			g.drawBitmapRegion(bmpStatic, 64, 64, 64, 64, dx+64, dy+64, Flip.NONE);
		}
		else if(getTile(x+1,y) == 1 && getTile(x,y+1) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 32, 64, 64, 64, dx+64, dy+64, Flip.NONE);
		}
		else if(getTile(x,y+1) == 1 && getTile(x+1,y) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 64, 32, 64, 64, dx+64, dy+64, Flip.NONE);
		}
		
		
		
		// Bottom-left
		if(getTile(x,y+1) != 1 && getTile(x-1,y) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 0, 64, 64, 64, dx, dy+64, Flip.NONE);
		}
		else if(getTile(x-1,y) == 1 && getTile(x,y+1) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 32, 64, 64, 64, dx, dy+64, Flip.NONE);
		}
		else if(getTile(x,y+1) == 1 && getTile(x-1,y) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 0, 32, 64, 64, dx, dy+64, Flip.NONE);
		}
		
		
		// Upper-right
		if(getTile(x,y-1) != 1 && getTile(x+1,y) != 1) {
					
			g.drawBitmapRegion(bmpStatic, 64, 0, 64, 64, dx+64, dy, Flip.NONE);
		}
		else if(getTile(x+1,y) == 1 && getTile(x,y-1) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 32, 0, 64, 64, dx+64, dy, Flip.NONE);
		}
		else if(getTile(x,y-1) == 1 && getTile(x+1,y) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 64, 32, 64, 64, dx+64, dy, Flip.NONE);
		}
		
		
		// Upper-left
		if(getTile(x,y-1) != 1 && getTile(x-1,y) != 1) {
							
			g.drawBitmapRegion(bmpStatic, 0, 0, 64, 64, dx, dy, Flip.NONE);
		}
		else if(getTile(x-1,y) == 1 && getTile(x,y-1) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 32, 0, 64, 64, dx, dy, Flip.NONE);
		}
		else if(getTile(x,y-1) == 1 && getTile(x-1,y) != 1) {
			
			g.drawBitmapRegion(bmpStatic, 0, 32, 64, 64, dx, dy, Flip.NONE);
		}
		
		
		
		// Bottom-right corner empty, but tiles close to it not
		if(getTile(x,y+1) == 1 && getTile(x+1,y) == 1
		&& getTile(x+1,y+1) != 1) {
							
			g.drawBitmapRegion(bmpStatic, 128+ 64, 64, 64, 64, dx+64, dy+64, Flip.NONE);
		}
		// Bottom-left corner empty ...
		if(getTile(x,y+1) == 1 && getTile(x-1,y) == 1
		&& getTile(x-1,y+1) != 1) {
									
			g.drawBitmapRegion(bmpStatic, 128, 64, 64, 64, dx, dy+64, Flip.NONE);
		}
		// Upper-right ...
		if(getTile(x,y-1) == 1 && getTile(x+1,y) == 1
		&& getTile(x+1,y-1) != 1) {
							
			g.drawBitmapRegion(bmpStatic, 128+ 64, 0, 64, 64, dx+64, dy, Flip.NONE);
		}
		// Upper-left ...
		if(getTile(x,y-1) == 1 && getTile(x-1,y) == 1
		&& getTile(x-1,y-1) != 1) {
									
			g.drawBitmapRegion(bmpStatic, 128, 0, 64, 64, dx, dy, Flip.NONE);
		}
		
	}
	
	
	/**
	 * Draw a piece of floor
	 * @param g Graphics object
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 */
	private void drawFloor(Graphics g, int x, int y) {
		
		g.setColor(0.75f,0.75f,0.75f);
		g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	
	/**
	 * Draw a single tile
	 * @param g Graphics object
	 * @param tile Tile index
	 * @param x X coordinate in tiles
	 * @param y Y coordinate in tiles
	 */
	private void drawTile(Graphics g, int tile, int x, int y) {
		
		switch(tile) {

		// Wall
		case 1:
			drawWall(g, x, y);
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
		tileData = map.copyLayer(0);
	}
	
	
	/**
	 * Update the stage
	 * @param tm Time multiplier
	 */
	public void update(float tm) {
		
		// ...
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
}
