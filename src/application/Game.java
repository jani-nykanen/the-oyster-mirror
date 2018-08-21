package application;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

import core.ApplicationListener;
import core.Configuration;
import core.State;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Transformations;

/**
 * Base game class
 * @author Jani Nykänen
 *
 */
public class Game extends ApplicationListener {
	
	
	/** Temporary timer, for testing */
	private float tempTimer = 0.0f;
	
	/** Test bitmap */
	private Bitmap bmpTest;
	
	
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
		
		// Load test bitmap
		bmpTest = new Bitmap("/assets/bitmaps/test.png");
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
		graph.clearScreen(0.67f, 0.67f, 0.67f);
		
		// Set transformations
		float s1 = (float)Math.sin(tempTimer);
		float s2 = (float)Math.sin(tempTimer/2.0f);
		Transformations t = graph.transform();
		t.fitViewHeight(720.0f);
		t.identity();
		t.translate(320.0f + (1.0f+s1)*128.0f, 240.0f);
		t.rotate(tempTimer);
		t.scale(2.0f+s2,2.0f+s2);
		t.translate(-64, -64);
		
		t.use();
		
		graph.setColor(1,1,1,1);
		graph.fillRect(-16, -16, 128+32, 128+32);
		
		graph.drawScaledBitmapRegion(bmpTest, 0, 0, 256, 256, 0, 0, 128, 128, Flip.NONE);
	}
	
	
	@Override
	protected void onDestroy() {
		
		System.out.println("KABOOM!");
	}
}
