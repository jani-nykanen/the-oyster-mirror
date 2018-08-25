package core.utility;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A multilayer tilemap
 * @author Jani Nykänen
 */
public class Tilemap {

	/** Width */
	private int width;
	/** Height */
	private int height;
	/** Name */
	private String name;
	
	/** Layers */
	private List<int[]> data;
	
	
	/**
	 * Parse properties
	 */
	private void parseProperties(XMLParser parser, NodeList prop) {
	
		if(prop == null) return;
		
		Node n;
		String name, value;
		for(int i = 0; i < prop.getLength(); ++ i) {
			
			n = prop.item(i);
			
			name = parser.getTextContent(n, "name");
			value = parser.getTextContent(n, "value");
			
			// If "name"
			if(name.equals("name")) {
				
				// Set name
				this.name = value;
			}
		}
	}
	
	
	/**
	 * Construct a tilemap by parsing
	 * Tiled-map file
	 * @param path File path
	 * @throws Exception If something goes wrong
	 */
	public Tilemap(String path) throws Exception {
		
		// Initialize
		data = new ArrayList<int[]> ();
		
		// Open file
		XMLParser parser = new XMLParser(path);
		parser.readyRoot();
		
		// Read dimensions
		width = Integer.parseInt(parser.getRootAttribute("width", "0"));
		height = Integer.parseInt(parser.getRootAttribute("height", "0"));
		
		// From now on, we partially abandon XMLParser utility and do things
		// manually
		Element root = parser.getRoot();
		
		// Find name property
		NodeList prop = root.getElementsByTagName("property");
		parseProperties(parser, prop);
		
		// Create CSV parser
		CSVParser csv = new CSVParser();
		
		// Find layers
		NodeList layers = root.getElementsByTagName("data");
		String CSVdata = "";
		for(int i = 0; i < layers.getLength(); ++ i) {
			
			CSVdata = layers.item(i).getTextContent();
			data.add(csv.parseInt(CSVdata));
		}
		
		// Close
		parser.close();
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
	 * Get name
	 * @return Name
	 */
	public String getName() {
		
		return name;
	}
	
	/**
	 * Get a tile value in the given layer in the 
	 * given coordinate
	 * @param layer Layer index
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param def "Default tile value", returned if incorrect coordinates/layer
	 * @return Tile value, def if incorrect coordinate/layer
	 */
	public int getTileValue(int layer, int x, int y, int def) {
	
		if(layer < 0 || layer >= data.size() 
		|| x < 0 || x >= width || y < 0 || y >= height)
			return def;
		
		return data.get(layer) [y*width+x];
	}
	
	
	/**
	 * Get a tile value in the given layer in the 
	 * given coordinate
	 * @param layer Layer index
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Tile value, 0 if incorrect coordinate/layer
	 */
	public int getTileValue(int layer, int x, int y) {
		
		return getTileValue(layer, x, y, 0);
	}
	
	
	/**
	 * Get a copy of a layer
	 * @param index Layer index
	 * @return A copy
	 */
	public int[] copyLayer(int index) {
		
		if(index < 0 || index >= data.size())
			return data.size() > 0 ? data.get(0).clone() : null;
			
		return data.get(index).clone();
	}
	
}
