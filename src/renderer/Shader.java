package renderer;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.ARBFragmentShader.*;
import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBVertexShader.*;


/**
 * An OpenGL shader
 * @author Jani Nykänen
 *
 */
public class Shader {

	
	/** Vertex position location name */
	static public final String POS_LOCATION_NAME = "vertexPos";
	/** Vertex UV location name */
	static public final String UV_LOCATION_NAME = "vertexUV";
	
	/** Shader program */
	private int program;
	
	
	/**
	 * Get error log content
	 * @param object Object (i.e. shader)
	 * @return Error info
	 */
	private String getErrorLog(int object) {
		
		return glGetInfoLogARB(object, 
				glGetObjectParameteriARB(object, GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	
	/**
	 * Create a shader
	 * @param src Source string
	 * @param shaderType Shader type (vertex or fragment)
	 * @return Shader
	 * @throws Exception If something goes wrong
	 */
	private int createShader(String src, int shaderType) throws Exception {
		
		// Create shader
		int shader = glCreateShaderObjectARB(shaderType);
		if(shader == 0) {
			
			throw new RuntimeException("Failed to create a shader!");
		}
		
		// Attach source
		glShaderSourceARB(shader, src);
		
		// Compile shader
		glCompileShaderARB(shader);
		
		// Check for errors
		if(glGetObjectParameteriARB(shader, GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
			
			// Delete shader and throw an error
			glDeleteObjectARB(shader);
			throw new RuntimeException("SHADER ERROR: " + getErrorLog(shader));
		}
		
		return shader;
	}
	
	
	/**
	 * Build shader 
	 * @param vertexSrc Vertex source
	 * @param fragmentSrc Fragment source
	 * @throws Exception If something goes wrong
	 */
	private void buildShader(String vertexSrc, String fragmentSrc) 
			throws Exception {
		
		// Create vertex & fragment shaders
		int vertex = createShader(vertexSrc, GL_VERTEX_SHADER_ARB);
		int fragment = createShader(fragmentSrc, GL_FRAGMENT_SHADER_ARB);
		
		// Create shader program
		program = glCreateProgramObjectARB();
		// Attach shader components
		glAttachObjectARB(program, vertex);
		glAttachObjectARB(program, fragment);
		
		// Bind attribute locations
		glBindAttribLocationARB(program, 0, POS_LOCATION_NAME);
		glBindAttribLocationARB(program, 1, UV_LOCATION_NAME);
		
		// Link program
		glLinkProgramARB(program);
		// Check for errors
		if (glGetObjectParameteriARB(program, GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE) {
			
			throw new RuntimeException("SHADER ERROR: " + getErrorLog(program));
        }
		
		// Validate
		glValidateProgramARB(program);
		// Check for errors
		if (glGetObjectParameteriARB(program, GL_OBJECT_VALIDATE_STATUS_ARB) == GL_FALSE) {
			
			throw new RuntimeException("SHADER ERROR: " + getErrorLog(program));
        }
	}
	
	

	/**
	 * Constructor
	 * @param vertexSrc Vertex source
	 * @param fragmentSrc Fragment source
	 * @throws Exception If something goes wrong
	 */
	public Shader(String vertexSrc, String fragmentSrc) throws Exception {
		
		// Build shader
		buildShader(vertexSrc, fragmentSrc);
		
		// Use program
		glUseProgramObjectARB(program);
		
		// Set default uniforms
		// TODO
	}
	
}
