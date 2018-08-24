package core;

import java.util.ArrayList;
import java.util.List;

import core.utility.XMLParser;


/**
 * Configuration information
 * @author Jani Nykänen
 *
 */
public class Configuration implements Cloneable {

	/**
	 * A key-value pair, both in string format
	 */
	private class StringPair {
		
		public String key;
		public String value;
		

		/**
		 * Constructor
		 * @param a Key
		 * @param b Value
		 */
		public StringPair(String a, String b) {
			
			key = a;
			value = b;
		}
	}
	
	/** Configuration parameters and their values */
	private List<StringPair> parameters;
	
	
	/**
	 * Use key to find a parameter
	 * @param key Parameter key (= name)
	 * @return Parameter
	 */
	private StringPair findParam(String key) {
		
		for(StringPair p : parameters) {
			
			if(p.key.equals(key)) {
				
				return p;
			}
		}
		return null;
	}
	
	
	/**
	 * Constructor
	 */
	public Configuration() {
		
		// Create parameter list
		parameters = new ArrayList<StringPair> ();
	}
	
	
	/**
	 * Constructor, parses from XML file
	 * @param path XML file path
	 * @throws Exception If something goes wrong 
	 * (e.g. file does not exist)
	 */
	public Configuration(String path) throws Exception {
		
		super();
		parseXML(path);
	}
	
	
	/**
	 * Add (or change an existing) a parameter
	 * @param key Parameter name
	 * @param value Parameter value
	 */
	public void setParameter(String key, String value) {
		
		// Check if a parameter with the given key
		// already exist
		StringPair p = findParam(key);
		if(p != null) {
			
			// If so, just change the value
			p.value = value;
		}
		else {
		
			// Otherwise, add a new parameter
			parameters.add(new StringPair(key, value));
		}
	}
	
	
	/**
	 * Get parameter value in a string format
	 * @param key Parameter key (=name)
	 * @param def Default value, if does not exist
	 * @return Parameter value
	 */
	public String getParameterValueString(String key, String def) {
		
		StringPair p = findParam(key);
		if(p == null) 
			return def;
		
		else
			return p.value;
	}
	
	
	/**
	 * Get parameter value in an integer format
	 * @param key Parameter key (= name)
	 * @param def Default value, if parameter does not exist
	 * @return Value
	 */
	public int getParameterValueInt(String key, int def) {
		
		String v = getParameterValueString(key, null);
		if(v == null)
			return def;
		
		else
			return Integer.parseInt(v);
		
	}
	
	
	/**
	 * Read configuration data from an XML file
	 * @param path File path
	 * @throws Exception If something goes wrong
	 */
	public void parseXML(String path) 
			throws Exception {

		// Open an XML parser
		XMLParser parser = new XMLParser(this.getClass().getResourceAsStream(path));
		parser.getRoot();
		parser.readyNodeList("param");
				
		// Get content
		String key, value;
		while(parser.getNextParam()) {
					
			key = parser.getTextContent("key");
			value = parser.getTextContent("value");
					
			// Add parameter
			setParameter(key, value);
		}
		
		// Close
		parser.close();
	}
	
	
	@Override
	public Configuration clone() {
		
		Configuration c = new Configuration();
		for(StringPair p : parameters) {
			
			c.setParameter(p.key, p.value);
		}
		return c;
	}
} 
