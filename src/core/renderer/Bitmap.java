package core.renderer;


import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;;

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
	 * Create a texture
	 * @param data Byte data
	 * @param w Width
	 * @param h Height
	 */
	public void createTexture(byte data[], int w, int h) {
	
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
	 * Construct a bitmap from given data
	 * @param data Pixel data (in RGBA format)
	 * @param w Width
	 * @param h Height
	 */
	public Bitmap(byte data[], int w, int h) {
		
		createTexture(data, w, h);
	}
	
	
	/**
	 * Construct a bitmap by loading a file
	 * @param path File path
	 */
	public Bitmap(String path) {
		
		// Get the absolute path
		String absPath = this.getClass().getResource(path).getPath();

		// Create buffers
		IntBuffer width, height, components;
		ByteBuffer data;
		try ( MemoryStack stack = stackPush() ) {
			
			width = stack.mallocInt(1);
		    height = stack.mallocInt(1);
		    components = stack.mallocInt(1);
			
		    // Read file
			data = STBImage.stbi_load(absPath, width, height, components, 4);
			if(data == null) {
			
				throw new RuntimeException("STBI image error: " + STBImage.stbi_failure_reason());
			}
			
		}
		
		// Now, create the texture
		byte[] arr = new byte[data.remaining()];
		data.get(arr);
		createTexture(arr, width.get(0), height.get(0));
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
