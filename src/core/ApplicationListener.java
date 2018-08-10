package core;

import static org.lwjgl.glfw.GLFW.*;


/**
 * Application listener, handles base application
 * behavior
 * @author Jani Nykänen
 *
 */
public class ApplicationListener extends WindowListener {


	/** Input manager, handles input */
	protected InputManager input;
	
	
	/**
	 * Initialize application
	 */
	private void init() {
		
		// Call user-defined starting method before
		// anything is initialized
		onStart();
		
		// Create components
		input = new InputManager();
		
		// Create a window and its context
		initWindowContext();
		
		// Call user-defined initialization method now
		onInit();
		
		// Load data
		// ...
		
		// Loading finished, call post-loading initialization
		// method
		onLoaded();
	}
	
	
	@Override
	protected void eventResize(int w, int h) { 
		
	}


	@Override
	protected void eventKeyDown(int key) { 
		
		input.onKeyPressed(key);
	}


	@Override
	protected void eventKeyUp(int key) { 
		
		input.onKeyReleased(key);
	}

	
	/**
	 * Update loop
	 * @param tm Time multiplier
	 */
	private void update(float tm) {
		
		// Call user-defined frame update method
		onUpdate(tm);
		
		// Update input
		input.update();
		
	}
	
	
	/**
	 * Draw application
	 */
	private void draw() {
		
		// Call user-defined frame rendering method
		onDraw();
	}
	
	
	/**
	 * Main loop
	 */
	private void loop() {
		
		boolean redraw = true;
		
		// If enough time passed, update frame
		// if(...) {
		update(0.0f);
		// }
		
		// Draw frame, if necessary
		if(redraw) {
			
			draw();
		}
		
		// If close button pressed
		if(shouldClose()) {
			
			terminate();
		}
		
		// Refresh frame
		refresh();
		
		// Poll events
		glfwPollEvents();
	}
	
	
	/**
	 * Destroy application
	 * @param success If there was an error
	 */
	private void destroy(boolean success) {
		
		// If no errors, we can assume everything was created
		// successfully and thus can be destroyed as well
		if(success) {
		
			// Destroy window
			destroyWindowContext();
			
			// Call user-defined destroy method before
			// destroying "critical" content
			onDestroy();
		}
	}
	
	
	/**
	 * Run application
	 * @param args Arguments
	 */
	public void run(String[] args) {
		
		boolean success = true;
		try {
			
			// Initialize
			init();
			
			// Start the main loop
			while(isRunning()) {
				
				loop();
			}
		}
		catch(Exception e) {
			
			System.out.println(e.getMessage());
			success = false;
			
		}
		finally {
			
			// Destroy application content
			destroy(success);
		}
	}
}
