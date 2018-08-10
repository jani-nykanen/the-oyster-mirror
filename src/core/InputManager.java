package core;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Input manager
 * @author Jani Nykänen
 *
 */
public class InputManager {

	/** The maximum amount of keys */
	static private final int KEY_COUNT = GLFW_KEY_LAST;
	
	/** Keyboard key states */
	private State[] keyStates = new State[KEY_COUNT];
	
	
	/**
	 * Update a state array
	 * @param arr State array
	 */
	private void updateStateArray(State[] arr) {
		
		for(int i = 0; i < arr.length; ++ i) {
			
			if(arr[i] == State.Pressed)
				arr[i] = State.Down;
			
			else if(arr[i] == State.Released)
				arr[i] = State.Up;
		}
	}
	
	
	/**
	 * Initialize a state array
	 * @param arr State array
	 */
	private void initStateArray(State[] arr) {
		
		for(int i = 0; i < arr.length; ++ i) {
			
			arr[i] = State.Up;
		}
	}
	
	
	/**
	 * Get a state array value
	 * @param arr State array
	 * @param index Value index
	 * @return State
	 */
	private State getStateArrayValue(State[] arr, int index) {
		
		if(index < 0 || index >= arr.length)
			return State.Up;
		
		return arr[index];
	}
	
	
	/**
	 * Handle key pressed event
	 * @param key Key
	 */
	public void onKeyPressed(int key) {
		
		// If key is out of range or already down, ignore
		if(key < 0 || key >= KEY_COUNT || keyStates[key] == State.Down) 
			return;
		
		keyStates[key] = State.Pressed;
	}
	
	
	/**
	 * Handle key released event
	 * @param key Key
	 */
	public void onKeyReleased(int key) {
		
		// If key is out of range or already up, ignore
		if(key < 0 || key >= KEY_COUNT || keyStates[key] == State.Up) 
			return;
		
		keyStates[key] = State.Released;
	}
	
	
	/**
	 * Update states
	 */
	public void update() {
		
		// Update keyboard states
		updateStateArray(keyStates);
	}


	/**
	 * Get a key state
	 * @param key Key
	 */
	public void getKeyState(int key) {
		
		getStateArrayValue(keyStates, key);
	}
	
	
	/**
	 * Constructor
	 */
	public InputManager() {
		
		// Initialize all input state arrays
		initStateArray(keyStates);
	}
}
