package application;

import core.ApplicationListener;
import core.Configuration;

/**
 * Base game class
 * @author Jani Nykänen
 *
 */
public class Game extends ApplicationListener {
	
	
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
		

	}
	
	
	@Override
	protected void onDraw() {
		

	}
	
	
	@Override
	protected void onDestroy() {
		
		System.out.println("KABOOM!");
	}
}
