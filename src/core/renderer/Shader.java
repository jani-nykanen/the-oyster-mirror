package core.renderer;

import static org.lwjgl.opengl.GL20.*;

import core.types.Matrix3;


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
	
	/** Uniforms */
	private int unifModel, unifView, unifPos, unifSize, unifTexPos, unifTexSize, unifColor;
	
	
	/**
	 * Get error log content
	 * @param object Object (i.e. shader)
	 * @return Error info
	 */
	private String getErrorLog(int object) {
		
		String log = glGetShaderInfoLog(object, glGetShaderi(object, GL_INFO_LOG_LENGTH));
		
		return log;
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
		int shader = glCreateShader(shaderType);
		if(shader == 0) {
			
			throw new RuntimeException("Failed to create a shader!");
		}
		
		// Attach source
		glShaderSource(shader, src);
		
		// Compile shader
		glCompileShader(shader);
		
		// Check for errors
		if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			
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
		int vertex = createShader(vertexSrc, GL_VERTEX_SHADER);
		int fragment = createShader(fragmentSrc, GL_FRAGMENT_SHADER);
		
		// Create shader program
		program = glCreateProgram();
		// Attach shader components
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		
		// Bind attribute locations
		glBindAttribLocation(program, 0, POS_LOCATION_NAME);
		glBindAttribLocation(program, 1, UV_LOCATION_NAME);
		
		// Link program
		glLinkProgram(program);
		// Check for errors
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			
			throw new RuntimeException("SHADER ERROR: " + getErrorLog(program));
        }
		
		// Validate
		glValidateProgram(program);
		// Check for errors
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			
			throw new RuntimeException("SHADER ERROR: " + getErrorLog(program));
        }
        
	}
	
	
	
	/**
	 * Get uniforms
	 */
	private void getUniforms() {
		
		unifModel = glGetUniformLocation(program, "model");
	    unifView = glGetUniformLocation(program, "project");
	    unifPos = glGetUniformLocation(program, "pos");
	    unifSize = glGetUniformLocation(program, "size");
	    unifTexPos = glGetUniformLocation(program, "texPos");
	    unifTexSize = glGetUniformLocation(program, "texSize");
	    unifColor = glGetUniformLocation(program, "color");
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
		glUseProgram(program);
		
		// Get uniforms
		getUniforms();
		
		// Set default uniforms
		setTransformationUniforms(new Matrix3(), new Matrix3());
	}
	
	
	/**
	 * Set transformation uniforms
	 * @param mat
	 */
	public void setTransformationUniforms(Matrix3 model, Matrix3 view) {
		
		glUniformMatrix3fv(unifModel, false, model.toArray());
	    glUniformMatrix3fv(unifView, false, view.toArray());
	}
	
}
