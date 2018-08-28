package core;

import static org.lwjgl.glfw.GLFW.*;

import core.types.Vector2;

/**
 * Input manager
 * @author Jani Nykänen
 *
 */
public class InputManager {

	/** The maximum amount of keys */
	static private final int KEY_COUNT = GLFW_KEY_LAST;
	/** The maximum allowed joystick button index */
	static public final int MAX_JOY_BUTTON = 16;
	
	/** Keyboard key states */
	private State[] keyStates = new State[KEY_COUNT];
	
	/** Joystick axis */
	private Vector2 joyAxis = new Vector2();
	/** Joystick buttons */
	private State[] joyStates = new State[MAX_JOY_BUTTON];
	
	
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
	 * Handle joystick axis changed event
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void onJoyAxis(float x, float y) {
		
		joyAxis.x = x;
		joyAxis.y = y;
	}
	
	
	/**
	 * Handle joystick button pressed event
	 * @param button
	 */
	public void onJoyPressed(int button) {
		
		// If the button is out of range or already down, ignore
		if(button < 0 || button >= MAX_JOY_BUTTON || joyStates[button] == State.Down) 
			return;
				
		joyStates[button] = State.Pressed;
	}
	
	
	/**
	 * Handle joystick button released event
	 * @param button
	 */
	public void onJoyReleased(int button) {
		
		// If the button is out of range or already down, ignore
		if(button < 0 || button >= MAX_JOY_BUTTON || joyStates[button] == State.Up) 
			return;
				
		joyStates[button] = State.Released;
	}
	
	
	/**
	 * Update states
	 */
	public void update() {
		
		// Update keyboard states
		updateStateArray(keyStates);
		// Update joystick button states
		updateStateArray(joyStates);
	}


	/**
	 * Get a key state
	 * @param key Key
	 * @return Key state
	 */
	public State getKeyState(int key) {
		
		return getStateArrayValue(keyStates, key);
	}
	
	
	/**
	 * Get joystick axes
	 * @return Axes
	 */
	public Vector2 getJoyAxes() {
		
		return joyAxis;
	}
	
	
	/**
	 * Get joystick button state
	 * @param button Button
	 * @return Button state
	 */
	public State getButtonState(int button) {
		
		return getStateArrayValue(joyStates, button);
	}
	
	
	/**
	 * Constructor
	 */
	public InputManager() {
		
		// Initialize all input state arrays
		initStateArray(keyStates);
		
		// Initialize other components
		joyAxis = new Vector2();
	}
}
