package core;

import org.lwjgl.glfw.*;

import core.types.Vector2;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;


/**
 * Event listener
 * @author Jani Nykänen
 *
 */
public abstract class EventListener {

	/** Joystick button buffer. We have it here because we want
	 * to emulate event-driven joystick handling that GLFW
	 * does not support yet. */
	private int[] joyButtonBuffer = new int[InputManager.MAX_JOY_BUTTON];
	
	
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
	 * Joystick axis moved event
	 * @param x X axis
	 * @param y Y axis
	 */
	protected abstract void eventJoyAxis(float x, float y);
	
	
	/**
	 * Joystick button pressed event
	 * @param button Button
	 */
	protected abstract void eventJoyDown(int button);
	
	
	/**
	 * Joystick button released event
	 * @param button Button
	 */
	protected abstract void eventJoyUp(int button);
	
	
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
	 * Update joystick, try to emulate
	 * event-driven joystick input handling.
	 */
	protected void updateJoyEvents() {
		
		// If not present, ignore
		if(!(glfwJoystickPresent(GLFW_JOYSTICK_1))) return;
		
		// Get joystick axes & buttons & hats
		FloatBuffer abuf = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
		ByteBuffer bbuf = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
		ByteBuffer hbuf = glfwGetJoystickHats(GLFW_JOYSTICK_1);
		try {
			
			// Send axis event request
			if(abuf != null) {
				
				// Get first two axes
				eventJoyAxis(abuf.get(), abuf.get());
			}
			
			// Convert hats to axis and send axis event request
			if(hbuf != null) {
				
				Vector2 axis = new Vector2();
				boolean changed = false;
				int state = hbuf.get();
				// TODO: Bitwise operators!
				if(state == GLFW_HAT_UP || state == GLFW_HAT_RIGHT_UP || state == GLFW_HAT_LEFT_UP) {
					
					changed = true; 
					axis.y = -1.0f; 
				}
				if(state == GLFW_HAT_DOWN || state == GLFW_HAT_RIGHT_DOWN  || state == GLFW_HAT_LEFT_DOWN) {
					
					changed = true; 
					axis.y = 1.0f; 
				}
				if(state == GLFW_HAT_RIGHT || state == GLFW_HAT_RIGHT_UP || state == GLFW_HAT_RIGHT_DOWN) {
					
					changed = true; 
					axis.x = 1.0f; 
				}
				if(state == GLFW_HAT_LEFT || state == GLFW_HAT_LEFT_UP || state == GLFW_HAT_LEFT_DOWN) {
					
					changed = true;  
					axis.x = -1.0f; 
				}
				
				if(changed)
					eventJoyAxis(axis.x, axis.y);
				
			}
			
			// Send button event request
			if(bbuf != null) {
				
				// Get buttons
				int state = 0;
				for(int i = 0; i < InputManager.MAX_JOY_BUTTON; ++ i) {
					
					// Compare to the previous 
					state = (int)bbuf.get();
					if(state != joyButtonBuffer[i]) {
						
						if(state == 1)
							eventJoyDown(i);
						
						else if(state == 0)
							eventJoyUp(i);
					}
					joyButtonBuffer[i] = state;
				}
			}
		}
		catch(Exception e) {}
		
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
		
		// Clear joystick buffer
		for(int i = 0; i < joyButtonBuffer.length; ++ i) {
			
			joyButtonBuffer[i] = 0;
		}
		
	}
}
