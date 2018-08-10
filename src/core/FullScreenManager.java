package core;

import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 * Manages transition from full screen to window
 * and vice versa
 * @author Jani Nykänen
 *
 */
public class FullScreenManager {

	/** Window pointer */
	private long window;
	
	/** Is in the windowed mode */
	private boolean windowed;
	
	/** The previous window size */
	private int[] previousWindowSize = new int[2];
	
	/** The previous window position */
	private int[] previousWindowPos = new int[2];
	
	/** Current window size */
	private int[] currentWindowSize = new int[2];
	
	
	/**
	 * Get window properties
	 */
	private void getWindowProp() {
		
		try ( MemoryStack stack = stackPush() ) {
			
			IntBuffer pX = stack.mallocInt(1);
			IntBuffer pY = stack.mallocInt(1);

			// Get window position
			glfwGetWindowPos(window, pX, pY);
			previousWindowPos[0] = pX.get(0);
			previousWindowPos[1] = pY.get(0);
			
			// Get window size
			glfwGetWindowSize(window, pX, pY);
			previousWindowSize[0] = pX.get(0);
			previousWindowSize[1] = pY.get(0);
		}
	}
	
	
	/**
	 * Constructor
	 * @param window Window
	 * @param windowed Is windowed
	 */
	public FullScreenManager(long window, boolean windowed) {
		
		this.window = window;
		if(!windowed) {
			
			this.windowed = !windowed;
			toggleFullScreen();
		}
		else {
		
			this.windowed = windowed;
		}
	}
	
	
	/**
	 * Toggle full screen
	 */
	public void toggleFullScreen() {
		
		// Toggle state
		windowed = !windowed;
		if(windowed) {
			
			// Reset window
			glfwSetWindowMonitor(window, NULL,
				previousWindowPos[0], previousWindowPos[1],
				previousWindowSize[0], previousWindowSize[1],
				GLFW_DONT_CARE);
			
			// Store current window size
			currentWindowSize[0] = previousWindowSize[0];
			currentWindowSize[1] = previousWindowSize[1];
		}
		else {
			
			// Store window information
			getWindowProp();
			
			// Get monitor pointer & video mode
			long monitor = glfwGetPrimaryMonitor();
			GLFWVidMode video = glfwGetVideoMode(monitor);
			// Store full screen window size
			currentWindowSize[0] = video.width();
			currentWindowSize[1] = video.height();
			
			// Enable full screen mode
			glfwSetWindowMonitor(window, monitor, 0, 0, video.width(), video.height(), GLFW_DONT_CARE );

		}
	}
	
}
