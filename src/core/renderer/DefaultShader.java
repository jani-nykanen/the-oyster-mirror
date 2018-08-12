package core.renderer;

/**
 * Includes default shaders. Required, so external
 * shaders are not necessary.
 * @author Jani Nykänen
 *
 */
public final class DefaultShader {
	
	/** Default vertex shader */
	static public final String VERTEX = "#version 120\n" + 
			"attribute vec2 vertexPos;\n" + 
			"attribute vec2 vertexUV;\n" + 
			"	   \n" + 
			"uniform mat3 project;\n" + 
			"	   \n" + 
			"uniform vec2 pos;\n" + 
			"uniform vec2 size;\n" + 
			"	   \n" + 
			"varying vec2 uv;\n" + 
			"	   \n" + 
			"// Main\n" + 
			"void main() {\n" + 
			"	   \n" + 
			"	vec2 p = vertexPos.xy;\n" + 
			"	   \n" + 
			"	// Set screen coordinates\n" + 
			"	p.x *= size.x;\n" + 
			"	p.y *= size.y;\n" + 
			"	p += pos;\n" + 
			"	   \n" + 
			"	// Position\n" + 
			"	gl_Position = vec4(project * vec3(p.x, p.y, 1), 1);\n" + 
			"	\n" + 
			"	// Texture coordinates\n" + 
			"	uv = vertexUV;\n" + 
			"}";
	
	
	/** Default fragment shader */
	static public final String FRAGMENT = "#version 120\n" + 
			" \n" + 
			"varying vec2 uv;\n" + 
			"uniform sampler2D texSampler;\n" + 
			"\n" + 
			"// Main\n" + 
			"void main() {\n" + 
			"\n" + 
			"    // Set color\n" + 
			"    gl_FragColor = texture2D(texSampler, uv);\n" + 
			"}";
	
}
