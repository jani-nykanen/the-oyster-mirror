package application;

import static org.lwjgl.glfw.GLFW.*;

import core.ApplicationListener;
import core.Configuration;
import core.State;

/**
 * Base game class
 * @author Jani Nykänen
 *
 */
public class Game extends ApplicationListener {
	
	
	/**
	 * Check default key commands (quit, full screen etc.)
	 */
	private void defaultKeyCommands() {
		
		// Terminate application, always
		if(input.getKeyState(GLFW_KEY_LEFT_CONTROL) == State.Down &&
			input.getKeyState(GLFW_KEY_Q) == State.Pressed) {
			
			terminate();
		}
		
		// Full screen
		if(input.getKeyState(GLFW_KEY_F4) == State.Pressed) {
			
			toggleFullScreen();
		}
	}
	
	
	@Override
	protected void onStart() {
		
		System.out.println("Starting...");
		
		// Create configuration
		Configuration conf = new Configuration();
		// Set parameters
		conf.setParameter("window_width", "800");
		conf.setParameter("window_height", "450");
		conf.setParameter("window_caption", "Game");
		conf.setParameter("frame_rate", "30");
		
		// Bind configuration
		bindConfiguration(conf);
	}
	
	
	@Override
	protected void onInit() {
		
		System.out.println("Initializing...");
	}
	
	
	@Override
	protected void onLoaded() {
		
		System.out.println("'Loading'...");
	}
	
	
	@Override
	protected void onUpdate(float tm) {
		
		// Check default key commands
		defaultKeyCommands();
	}
	
	
	@Override
	protected void onDraw() {
		

	}
	
	
	@Override
	protected void onDestroy() {
		
		System.out.println("KABOOM!");
	}
}
