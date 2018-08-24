package core.utility;


/**
 * An asset (e.g bitmap, tilemap..)
 * @author Jani Nykänen
 *
 */
public class Asset<T extends Object> {

	/** Asset */
	private T asset;
	
	/** Asset name */
	private String name;
	
	
	/**
	 * Constructor
	 * @param asset Asset
	 * @param name Asset name
	 */
	public Asset(T asset, String name) {
		
		this.asset = asset;
		this.name = name;
	}
	
	
	/**
	 * Get name
	 * @return Name
	 */
	public String getName() {
		
		return name;
	}
	
	
	/**
	 * Get asset
	 * @return Asset
	 */
	public T getAsset() {
		
		return asset;
	}
}
