package core;

import core.audio.AudioManager;

/**
 * A very weak event manager. Can
 * shut down the application and hardly
 * anything else
 * @author Jani Nykänen
 *
 */
public class WeakEventManager {

	/** A reference to the application listener */
	private ApplicationListener app;
	
	
	/**
	 * Constructor
	 * @param app The main application listener
	 */
	public WeakEventManager(ApplicationListener app) {
		
		this.app = app;
	}
	
	
	/**
	 * Terminate application
	 */
	public void quit() {
		
		app.terminate();
	}
	
	
	/**
	 * Toggle full screen mode
	 */
	public void toggleFullscreen() {
		
		app.toggleFullScreen();
	}
	
	
	/**
	 * Get frame limit
	 * @return Frame limit
	 */
	public int getFrameRate() {
		
		return app.getFrameRate();
	}
	
	
	/**
	 * Set a new frame limit
	 * @param fps New frame limit
	 */
	public void setFrameRate(int fps) {
		
		app.setFrameRate(fps);
	}
	
	
	/**
	 * Get audio manager
	 * @return Audio manager
	 */
	public AudioManager getAudioManager() {
		
		return app.getAudioManager();
	}
}
