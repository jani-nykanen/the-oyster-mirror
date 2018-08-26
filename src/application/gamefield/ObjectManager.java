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
	
	
	/**
	 * Initialize global content (=assets)
	 * @param assets Assets pack
	 */
	static public void init(AssetPack assets) {
		
		// Pass assets to all components
		NonPlayerFieldObject.setScaleValue(Stage.TILE_SIZE, Stage.TILE_SIZE);
		Crate.init(assets);
		Collectible.init(assets);
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
	public void parseMap(int[] data, int w, int h) {

		for(int i = 0; i < data.length; ++ i) {
			
			// Crate
			switch(data[i]) {
			
			case 2:
				
				objects.add(new Crate(new Point(i % w, i / w)));
				break;
				
			// Key
			case 5:
				
				objects.add(new Key(new Point(i % w, i / w)));
				break;
				
			// Gem
			case 8:
				
				objects.add(new Gem(new Point(i % w, i / w)));
				break;
				
			// Star
			case 18:
				
				objects.add(new Star(new Point(i % w, i / w)));
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
	 * @param tm Time mul.
	 */
	public void update(Gamepad vpad, TimeManager tman, float tm) {
		
		// Update non-player field objects
		for(NonPlayerFieldObject o : objects) {
			
			// Update base
			o.update(vpad, tman, tm);
			
			// Update position
			o.updatePosition(tman);
		}
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
	}
}
