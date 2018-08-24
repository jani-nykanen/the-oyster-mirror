package core.renderer;


import static org.lwjgl.opengl.GL20.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

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
	 * @throws IOException If file is not found
	 */
	public Bitmap(String path) throws IOException {
		
		// Load image
		BufferedImage imgBuf = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(path));
		
		// Get bytes
		WritableRaster raster = imgBuf.getRaster();
		DataBufferByte data  = (DataBufferByte) raster.getDataBuffer();
		byte[] bytes = data.getData();
		
		// Reorder the bytes
		byte[] ordered = new byte[bytes.length];
		for(int i = 0; i < bytes.length; i += 4) {
			
			ordered[i] = bytes[i+3];
			ordered[i+1] = bytes[i+2];
			ordered[i+2] = bytes[i+1];
			ordered[i+3] = bytes[i];
		}
		
		createTexture(ordered, imgBuf.getWidth(), imgBuf.getHeight());
		
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
