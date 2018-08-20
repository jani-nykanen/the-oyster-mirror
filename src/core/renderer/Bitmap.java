package core.renderer;


import static org.lwjgl.opengl.GL20.*;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

/**
 * Bitmap class
 * @author Jani Nykänen
 *
 */
public class Bitmap {

	/** Previously bound texture */
	static private int prevTex = -1;
	
	/** Bitmap width */
	private int width;
	/** Bitmap height */
	private int height;
	/** GL texture index */
	private int texture;
	
	
	/**
	 * Construct a bitmap from given data
	 * @param data Pixel data (in RGBA format)
	 * @param w Width
	 * @param h Height
	 */
	public Bitmap(byte data[], int w, int h) {
		
		// Store dimensions
		width = w;
		height = h;
		
		// Create texture
		texture = glGenTextures();
	    glBindTexture(GL_TEXTURE_2D, texture);

	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	    
	    // Set data to a buffer
	    ByteBuffer bytes = BufferUtils.createByteBuffer(data.length);
	    bytes.put(data);
	    bytes.flip();

	    // Pass data to the texture
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA,
	        GL_UNSIGNED_BYTE,bytes);
	    
	    
	}
	
	
	/**
	 * Get bitmap width
	 * @return Width
	 */
	public int getWidth() {
		
		return width;
	}
	
	
	/**
	 * Get bitmap height
	 * @return Height
	 */
	public int getHeight() {
		
		return height;
	}
	
	
	/**
	 * Bind this texture
	 */
	public void bind() {
		
		if(texture != prevTex) {
			
			prevTex = texture;
			glBindTexture(GL_TEXTURE_2D, texture);
		}
	}
	
}
