package application.gamefield;

import java.util.ArrayList;
import java.util.List;

import application.Gamepad;
import core.renderer.Graphics;
import core.types.Point;
import core.utility.AssetPack;

/**
 * Handles field object interaction & rendering
 * @author Jani Nykänen
 *
 */
public class ObjectManager {

	/** Non-player field objects */
	private List<NonPlayerFieldObject> objects;
	/** Player */
	private Player player = null;
	
	
	/**
	 * Initialize global content (=assets)
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		// Pass assets to all components
		NonPlayerFieldObject.setScaleValue(Stage.INITIAL_TILE_SIZE, Stage.INITIAL_TILE_SIZE);
		Crate.init(assets);
		Collectible.init(assets);
		Player.init(assets);
		Lock.init(assets);
		LavaContainer.init(assets);
	}
	
	
	/**
	 * Constructor
	 */
	public ObjectManager() {
		
		objects = new ArrayList<NonPlayerFieldObject> ();
	}
	
	
	/**
	 * Parse a tilemap layer & create objects
	 * @param data Data
	 */
	public void parseMap(Stage stage, int[] data, int w, int h) {

		Point p;
		int x, y;
		
		for(int i = 0; i < data.length; ++ i) {

			x = i % w;
			y = i / w;
			p = new Point(x, y);
			
			// Crate
			switch(data[i]) {
			
			// Crate
			case 2:
				
				objects.add(new Crate(p));
				stage.updateTileData(x, y, 2);
				break;
				
			// Lock
			case 4:
				
				objects.add(new Lock(p));
				break;
				
			// Key
			case 5:
				
				objects.add(new Key(p));
				break;
				
			// Gem
			case 8:
				
				objects.add(new Gem(p));
				break;
				
			// Purple crate
			case 11:
									
				objects.add(new PurpleCrate(p));
				stage.updateTileData(x, y, 2);
				break;	
				
			// Hammer
			case 12:
								
				objects.add(new Hammer(p));
				break;
				
			// Lava container
			case 13:
			case 14:
				
				objects.add(new LavaContainer(p, data[i] == 14));
				break;
				
			// Player
			case 17:
				player = new Player(p);
				break;
				
			// Star
			case 18:
				
				objects.add(new Star(p));
				break;
	
			default:
				break;
			}
		}
	}
	
	
	/**
	 * Update objects
	 * @param vpad Game pad
	 * @param tman Time manager
	 * @param stage Stage
	 * @param statMan Status manager
	 * @param tm Time mul.
	 */
	public void update(Gamepad vpad, TimeManager tman, Stage stage, StatusManager statMan, float tm) {
		
		// Update non-player field objects
		for(NonPlayerFieldObject o : objects) {
			
			// Update position
			o.updatePosition(tman, stage);
			o.playerCollision(player, vpad, stage, tman);
			
			// Update base
			o.update(vpad, tman, stage, tm);
			
		}
	
		// Stop player before doing another updates,
		// if an object is dying
		if(tman.hasStopped() && tman.isWaiting()) {
			
			player.stopMoving(stage, tman);
		}
		
		// Update player
		player.updatePosition(tman, stage);
		player.update(vpad, tman, stage, tm);
		
		// Update status manager
		statMan.update(player, tman, tm);
		
	}
	
	
	/**
	 * Draw non-player field object
	 * @param g Graphics object
	 */
	public void draw(Graphics g) {
		
		// Draw non-player field objects
		for(NonPlayerFieldObject o : objects) {
					
			o.draw(g);
		}
		
		// Draw player
		player.draw(g);
	}
}
