package core.utility;

/**
 * 
 * A key-value pair
 * @author Jani Nykänen
 *
 */
public class StringPair {
	
	/** Key */
	private String key;
	/** Value */
	private String value;
	

	/**
	 * Constructor
	 * @param a Key
	 * @param b Value
	 */
	public StringPair(String a, String b) {
		
		key = a;
		value = b;
	}
	
	
	/**
	 * Get key
	 * @return Key
	 */
	public String getKey() {
		
		return key;
	}
	
	
	/**
	 * Get value
	 * @return Value
	 */
	public String getValue() {
		
		return value;
	}
	
	
	/**
	 * Set a new value
	 * @param val New value
	 */
	public void setValue(String val) {
		
		value = val;
	}
}