package core.renderer;

import static org.lwjgl.opengl.GL20.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;


/**
 * A two-dimensional mesh
 * @author Jani Nykänen
 *
 */
public class Mesh2D {

	/** Buffers */
	private int vertexBuffer,
		uvBuffer,
		indexBuffer;
	
	/** Index count */
	private int indexCount;
	
	
	/**
	 * Copy float array data to a float buffer
	 * @param arr Array
	 * @param buf Buffer
	 */
	private void copyToFloatBuffer(float[] arr, FloatBuffer buf) {
		
		for(int i = 0; i < arr.length; ++ i) {
			
			buf.put(i, arr[i]);
		}
	}
	
	
	/**
	 * Construct a 2D mesh from the given data
	 * @param vertices
	 * @param uvs
	 * @param indices
	 */
	public Mesh2D(float[] vertices, float[] uvs, short[] indices) {
		
		indexCount = indices.length;
		
		// Generate GL buffers
		vertexBuffer = glGenBuffers();
		uvBuffer = glGenBuffers();
		indexBuffer = glGenBuffers();
		
		// Generate byte buffers
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertices.length);
		vertexData.put(vertices);
		vertexData.flip();

		FloatBuffer uvData = BufferUtils.createFloatBuffer(uvs.length);
		uvData.put(uvs);
		uvData.flip();
		
		ShortBuffer indexData = BufferUtils.createShortBuffer(indices.length);
		indexData.put(indices);
		indexData.flip();
		
		// Set data
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
		glBufferData(GL_ARRAY_BUFFER, uvData, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
	}
	
	
	/**
	 * Bind for drawing
	 */
	public void bind() {

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glVertexAttribPointer( 0, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
		glVertexAttribPointer( 1, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	}
	
	
	/**
	 * Draw
	 */
	public void draw() {
		
		glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_SHORT, 0);
	}
}
