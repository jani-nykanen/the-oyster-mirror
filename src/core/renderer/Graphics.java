package core.renderer;


/**
 * Graphics context. Handles everything
 * needed for rendering content
 * @author Jani Nykänen
 *
 */
public final class Graphics {

	/** Default shader */
	private Shader shaderDefault;
	
	/** Transformations */
	private Transformations transf;
	
	/** White bitmap for rectangle rendering */
	private Bitmap bmpWhite;
	
	
	/**
	 * Initialize graphics
	 * @throws Exception If something goes wrong
	 */
	public void init() throws Exception {
		
		// Create the default shader
		shaderDefault = new Shader(DefaultShader.VERTEX, DefaultShader.FRAGMENT);
		
		// Create components
		transf = new Transformations();
		bmpWhite = new Bitmap(new byte[] {
				(byte)255,(byte)255,(byte)255,(byte)255
				}, 1, 1);
	}
	
	
	/**
	 * Constructor
	 */
	public Graphics() throws Exception {
		
		// Initialize
		init();
	}
}
