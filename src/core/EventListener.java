package core;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;


/**
 * Event listener
 * @author Jani Nykänen
 *
 */
public abstract class EventListener {

	
	
	/**
	 * Framebuffer resized event
	 * @param w New width
	 * @param h New height
	 */
	protected abstract void eventResize(int w, int h);
	
	
	/**
	 * Keyboard key pressed event
	 * @param key
	 */
	protected abstract void eventKeyDown(int key);
	
	
	/**
	 * Keyboard key released event
	 * @param key
	 */
	protected abstract void eventKeyUp(int key);
	
	
	/**
	 * Handle keyboard event
	 * @param window Window
	 * @param key Key
	 * @param scancode Scancode
	 * @param action Action
	 * @param mods Mods
	 */
	private void keyboardEvent(long window, int key, int scancode,
			int action, int mods) {
		
		// Check if something was pressed or released
		if(action == GLFW_PRESS) {

			eventKeyDown(key);
		}
		else if(action == GLFW_RELEASE) {
			
			eventKeyUp(key);
		}
	}
	
	
	/**
	 * Handle resize event
	 * @param window Window 
	 * @param w New width
	 * @param h New height
	 */
	private void resizeEvent(long window, int w, int h) {
		
		eventResize(w, h);
	}
	
	
	/**
	 * Register event callbacks
	 * @param window Window
	 */
	protected void registerEvents(long window) {

		// Set keyboard callback
		glfwSetKeyCallback(window, new GLFWKeyCallbackI() {
			@Override
			public void invoke(long win, int key, int scancode, int action, int mods) {
				
				keyboardEvent(win, key, scancode, action, mods);
			}
		});
		
		// Set resize callback
		glfwSetFramebufferSizeCallback(window, new  GLFWFramebufferSizeCallbackI() {
			@Override
			public void invoke(long win, int w, int h) {
				
				resizeEvent(win, w, h);
			}
		});
		
	}
}
