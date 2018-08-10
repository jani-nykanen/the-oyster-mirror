package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.opengl.GL;


/**
 * Handles window-related behavior
 * @author Jani Nykänen
 *
 */
public class WindowListener extends ApplicationEvents {

	/** The main window */
	private long window;
	
	/** Configuration data */
	protected Configuration conf = new Configuration();
	
	/** Full screen manager */
	private FullScreenManager fsManager;

	
	/**
	 * Initialize window-related context
	 * @throws RuntimeException Exception
	 */
	public void initWindowContext() throws RuntimeException {
		
		final int DEFAULT_WIN_WIDTH = 640;
		final int DEFAULT_WIN_HEIGHT = 480;
		final String DEFAULT_WIN_CAPTION = "Application";
		
		// Initialize GLFW
		if(!glfwInit()) {
			
			throw new RuntimeException("Failed to initialize GLFW!");
		}
		
		// Set window creation flags
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		
		// Read window creation properties in the 
		// configuration data
		int winWidth = conf.getParameterValueInt("window_width", DEFAULT_WIN_WIDTH);
		int winHeight = conf.getParameterValueInt("window_height", DEFAULT_WIN_HEIGHT);
		String caption = conf.getParameterValueString("window_caption", DEFAULT_WIN_CAPTION);
		boolean windowed = conf.getParameterValueInt("full_screen", 0) == 0;
		
		// Create a window
		window = glfwCreateWindow(winWidth, winHeight, caption, NULL, NULL);
		if(window == NULL) {
			
			throw new RuntimeException("Failed to create a window!");
		}
		
		// Create full screen manager
		fsManager = new FullScreenManager(window, windowed);
		
		// Register event callbacks
		registerEvents(window);
		
		// Set OpenGL context to this window
		glfwMakeContextCurrent(window);
		// Enable VSync
		glfwSwapInterval(1);
		
		// Create capabilities
		GL.createCapabilities();
	}
	
	
	/**
	 * Destroy the window and its context
	 */
	public void destroyWindowContext() {
		
		glfwDestroyWindow(window);
	}


	/**
	 * Check if the window is supposed to be closed
	 * @return True, if window should close
	 */
	public boolean shouldClose() {
		
		return glfwWindowShouldClose(window);
	}
	
	
	/**
	 * Refresh frame
	 */
	public void refresh() {
		
		glfwSwapBuffers(window);
	}
	
	
	/**
	 * Pass configuration data object
	 * @param conf Configuration data
	 */
	public void bindConfiguration(Configuration conf) {
		
		this.conf = conf.clone();
	}
	
	
	/**
	 * Toggle full screen mode
	 */
	public void toggleFullScreen() {
		
		fsManager.toggleFullScreen();
	}
}
