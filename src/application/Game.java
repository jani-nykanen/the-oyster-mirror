package application;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

import core.ApplicationListener;
import core.Configuration;
import core.State;

/**
 * Base game class
 * @author Jani Nykänen
 *
 */
public class Game extends ApplicationListener {
	
	
	/** Temporary timer, for testing */
	private float tempTimer = 0.0f;
	
	
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
		
		Configuration conf = new Configuration();;
		
		// Create configuration
		try {
			
			conf.parseXML("/config.xml");
		}
		catch(Exception e) {
			
			System.out.println("Failed to read configuration file: " +
				e.getMessage());
			
			// If fails, set default parameters
			conf.setParameter("window_width", "800");
			conf.setParameter("window_height", "450");
			conf.setParameter("window_caption", "Game");
			conf.setParameter("frame_rate", "30");
			conf.setParameter("full_screen", "0");
		
		}
		
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
		
		final float TIMER_SPEED = 0.05f;
		
		// Check default key commands
		defaultKeyCommands();
		
		// Update timer
		tempTimer += TIMER_SPEED * tm;
	}
	
	
	@Override
	protected void onDraw() {
		
		// Clear screen
		glClearColor(0.5f + 0.5f*(float)Math.sin(tempTimer), 0.75f, 0.75f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	
	@Override
	protected void onDestroy() {
		
		System.out.println("KABOOM!");
	}
}
