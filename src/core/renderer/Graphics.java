package core.renderer;

import core.types.Vector2;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.*;

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
	
	/** Rectangle mesh */
	private Mesh2D meshRect;
	
	
	/**
	 * Initialize graphics
	 * @throws Exception If something goes wrong
	 */
	public void init() throws Exception {
		
		// Create the default shader
		shaderDefault = new Shader(DefaultShader.VERTEX, DefaultShader.FRAGMENT);
		
		// Create components
		transf = new Transformations();
		transf.bindShader(shaderDefault);
		
		// Create "white texture" used for rendering
		// rectangles
		bmpWhite = new Bitmap(new byte[] {
				(byte)255,(byte)255,(byte)255,(byte)255
				}, 1, 1);
		
		// Create rectangular mesh used for all kind of rendering
		meshRect = new Mesh2D(new float[] {
			0.0f, 0.0f,
		    1.0f, 0.0f,
		    1.0f, 1.0f,
		    0.0f, 1.0f
		},
		new float[] {
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f	 
		},
		new short[] {
			0,1,2, 
			2,3,0
		});
		
		// Enable GL related stuff
		glActiveTexture(GL_TEXTURE0);
	    glDisable(GL_DEPTH_TEST);
	    glEnable( GL_BLEND );
	    glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
	    
	    // Bind mesh
	    meshRect.bind();
	}
	
	
	/**
	 * Constructor
	 */
	public Graphics() throws Exception {
		
		// Initialize
		init();
	}
	
	
	/**
	 * Get transformation object
	 * @return Transformation object
	 */
	public Transformations transform() {
		
		return transf;
	}
	
	
	/**
	 * Draw a filled rectangle
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param w Width
	 * @param h Height
	 */
	public void fillRect(float x, float y, float w, float h) {
		
		// Bind texture
		bmpWhite.bind();
		
		// Pass position & dimension data to the shader
		shaderDefault.setVertexUniforms(new Vector2(x, y), new Vector2(w, h));
		shaderDefault.setUVUniforms(new Vector2(0, 0), new Vector2(1, 1));
		
		meshRect.draw();
	}
	
	
	/**
	 * Draw a scaled bitmap region
	 * @param bmp Bitmap
	 * @param sx Source x
	 * @param sy Source y
	 * @param sw Source width
	 * @param sh Source height
	 * @param dx Destination x
	 * @param dy Destination y
	 * @param dw Destination width
	 * @param dh Destination height
	 * @param flip Flipping flag
	 */
	public void drawScaledBitmapRegion(Bitmap bmp, int sx, int sy, int sw, int sh,
			float dx, float dy, float dw, float dh, int flip) {
		
		// Bind bitmap
		bmp.bind();
		
		// Flip
		float w = (float)bmp.getWidth();
	    float h = (float)bmp.getHeight();
	    if( (flip & Flip.HORIZONTAL) != 0) {

	        dx += dw;
	        dw *= -1;
	    }
	    if( (flip & Flip.VERTICAL) != 0) {

	        dy += dh;
	        dh *= -1;
	    }
	    
	    float deltaX = 1.0f / bmp.getWidth();
	    float deltaY = 1.0f / bmp.getHeight();
	    
	    // Pass size data to the shader
	    shaderDefault.setVertexUniforms(new Vector2(dx, dy), new Vector2(dw, dh));
	    shaderDefault.setUVUniforms(new Vector2(sx / w + deltaX, sy / h + deltaY), 
	    		new Vector2(sw / w - deltaX*2, sh / h- deltaY*2));
	    meshRect.draw();
	}
	
	
	/**
	 * Draw a bitmap region
	 * @param bmp Bitmap
	 * @param sx Source x
	 * @param sy Source y
	 * @param sw Source width
	 * @param sh Source height
	 * @param dx Destination x
	 * @param dy Destination y
	 * @param flip Flipping flag
	 */
	public void drawBitmapRegion(Bitmap bmp, int sx, int sy, int sw, int sh,
			float dx, float dy, int flip) {
		
		drawScaledBitmapRegion(bmp,sx,sy,sw,sh,dx,dy,sw,sh,flip);
	}
	
	
	/**
	 * Draw a scaled bitmap
	 * @param bmp Bitmap
	 * @param dx Destination x
	 * @param dy Destination y
	 * @param dx Destination width
	 * @param dy Destination height
	 * @param flip Flipping flag
	 */
	public void drawScaledBitmap(Bitmap bmp, float dx, float dy, 
			float dw, float dh, int flip) {
		
		int sw = bmp.getWidth();
		int sh = bmp.getHeight();
		drawScaledBitmapRegion(bmp,0,0,sw,sh,dx,dy,dw,dh,flip);
	}
	
	
	/**
	 * Draw a bitmap
	 * @param bmp Bitmap
	 * @param dx Destination x
	 * @param dy Destination y
	 * @param flip Flipping flag
	 */
	public void drawBitmap(Bitmap bmp, float dx, float dy, int flip) {
		
		int sw = bmp.getWidth();
		int sh = bmp.getHeight();
		drawScaledBitmapRegion(bmp,0,0,sw,sh,dx,dy,sw,sh,flip);
	}
	
	
	/**
	 * Draw text using a bitmap font
	 * @param bmp Bitmap
	 * @param text Text to be drawn
	 * @param dx Destination x
	 * @param dy Destination y
	 * @param xoff X offset
	 * @param yoff Y offset
	 * @param center Center the text or not
	 * @param scale Scale
	 */
	public void drawText(Bitmap bmp, String text, 
			float dx, float dy, float xoff, float yoff, boolean center, float scale) {
		
	    if(bmp == null) return;
	    
	    center = center == false ? false : center;

	    int cw = (bmp.getWidth()) / 16;
	    int ch = cw;
	    int len = text.length();
	    float x = dx;
	    float y = dy;
	    char c;
	    
	    int sx, sy;
	    
	    if(center) {

	        dx -= ( (len+1)/2.0f * (cw+xoff) * scale );
	        x = dx;
	    }

	    for(int i = 0; i < len;  ++ i) {

	        c = text.charAt(i);
	        if(c == '\n') {

	            x = dx;
	            y += (yoff + ch) * scale;
	            continue;
	        }

	        sx = c % 16;
	        sy = (c / 16) | 0;

	        drawScaledBitmapRegion(bmp,sx*cw,sy*ch,cw,ch,x,y, cw* scale, ch* scale, Flip.NONE);

	        x += (cw + xoff)* scale;
	    }
	}
	
	
	/**
	 * Draw text using a bitmap font
	 * @param bmp Bitmap
	 * @param text Text to be drawn
	 * @param dx Destination x
	 * @param dy Destination y
	 * @param xoff X offset
	 * @param yoff Y offset
	 * @param center Center the text or not
	 */
	public void drawText(Bitmap bmp, String text, 
			float dx, float dy, float xoff, float yoff, boolean center) {
		
		drawText(bmp, text, dx, dy, xoff, yoff, center, 1.0f);
	}
	
	
	/**
	 * Set global rendering color
	 * @param r Red channel
	 * @param g Green channel
	 * @param b Blue channel
	 * @param a Alpha channel
	 */
	public void setColor(float r, float g, float b, float a) {
		
		shaderDefault.setColorUniform(r, g, b, a);
	}

	
	/**
	 * Set global rendering color
	 * @param r Red channel
	 * @param g Green channel
	 * @param b Blue channel
	 */
	public void setColor(float r, float g, float b) {
		
		setColor(r, g, b, 1.0f);
	}
	
	
	/**
	 * Set global rendering color to opaque white
	 */
	public void setColor() {
		
		setColor(1,1,1,1);
	}
	
	
	/**
	 * Clear the screen with a color
	 * @param r Red channel
	 * @param g Green channel
	 * @param b Blue channel
	 */
	public void clearScreen(float r, float g, float b) {
		
		glClearColor(r,g,b,1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	
	/**
	 * Set viewport
	 * @param w Width
	 * @param h Height
	 */
	public void setViewport(int w, int h) {
		
		glViewport(0,0,w,h);
		transf.updateFrameBufferSize(w, h);
	}
}
