package core.utility;

import java.util.ArrayList;
import java.util.List;

import core.renderer.Bitmap;

/**
 * A collection of assets
 * @author Jani Nykänen
 *
 */
public class AssetPack {

	/** Bitmaps */
	private List< Asset<Bitmap> > bitmaps;
	/** Tilemaps */
	private List< Asset<Tilemap> > tilemaps;
	
	
	/**
	 * Constructor
	 */
	public AssetPack() {
		
		// Initialize components
		bitmaps = new ArrayList< Asset<Bitmap> > ();
		tilemaps = new ArrayList< Asset<Tilemap> > ();
	}
	
	
	
	/**
	 * Parse XML and load associated assets
	 * @param path File path
	 * @throws Exception If something goes wrong
	 */
	public void parseXML(String xmlPath) throws Exception {
		
		// Open an XML parser
		XMLParser parser = new XMLParser(xmlPath);
		parser.readyRoot();
				
		// Get root info
		String bmpPath = parser.getRootAttribute("bitmap_path", "assets/bitmaps/");
		String mapPath = parser.getRootAttribute("tilemap_path", "assets/bitmaps/");
		
		
		// Read bitmaps
		parser.readyNodeList("bitmap");
		String name, path;
		while(parser.getNextParam()) {
					
			name = parser.getTextContent("name");
			path = parser.getTextContent("path");
			
			// Load bitmap
			bitmaps.add(new Asset<Bitmap> (new Bitmap(bmpPath + path), name));
		}
		
		// Read tilemaps
		parser.readyNodeList("tilemap");
		while(parser.getNextParam()) {
							
			name = parser.getTextContent("name");
			path = parser.getTextContent("path");
					
			// Load bitmap
			tilemaps.add(new Asset<Tilemap> (new Tilemap(mapPath + path), name));
		}
				
		// Close
		parser.close();
	}
	
	
	/**
	 * Get a bitmap by name
	 * @param name Bitmap name
	 * @return Bitmap, null if does not exist
	 */
	public Bitmap getBitmap(String name) {
		
		for(Asset<Bitmap> b : bitmaps) {
			
			if(b.getName().equals(name)) {
				
				return b.getAsset();
			}
		}
		return null;
	}
}
