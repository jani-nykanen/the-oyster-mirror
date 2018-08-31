package core;

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
}
