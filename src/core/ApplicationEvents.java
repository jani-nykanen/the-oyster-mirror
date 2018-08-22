package core;

/**
 * Application events handler
 * @author Jani Nykänen
 *
 */
public class ApplicationEvents extends EventListener {

	
	/**
	 * Is the application running
	 */
	private boolean isRunning = true;
	

	/**
	 * Called before the application is initialized
	 */
	protected void onStart() {};
	
	
	/**
	 * Called when the application is initialized
	 */
	protected void onInit() {};
	
	
	/**
	 * Called when the data is loaded
	 * @throws Exception 
	 */
	protected void onLoaded() throws Exception {};
	
	
	/**
	 * Called when the application is updated
	 * @param tm Time multiplier
	 */
	protected void onUpdate(float tm) {};
	
	
	/**
	 * Called when the application is rendered
	 */
	protected void onDraw() {};
	
	
	/**
	 * Called when the application is destroyed
	 */
	protected void onDestroy() {};
	
	
	@Override
	protected void eventResize(int w, int h) { }


	@Override
	protected void eventKeyDown(int key) { }


	@Override
	protected void eventKeyUp(int key) { }
	
	
	/**
	 * Is the application running
	 * @return True, if running
	 */
	protected boolean isRunning() {
		
		return isRunning;
	}
	
	
	/**
	 * Terminate application
	 */
	public void terminate() {
		
		isRunning = false;
	}

	
}
