package application;

import java.util.ArrayList;
import java.util.List;

import core.InputManager;
import core.State;
import core.types.Vector2;

import static org.lwjgl.glfw.GLFW.*;


/**
 * A virtual gamepad that combines
 * keyboard and joystick input to a single
 * object.
 * @author Jani Nykänen
 *
 */
public class Gamepad {

	/** Gamepad button */
	private final class Button {
		
		/** Key */
		public int key;
		/** Joystick button */
		public int button;
		/** State */
		public State state;

		
		/**
		 * Constructor
		 * @param key Key
		 * @param button Joystick button
		 */
		public Button(int key, int button) {
			
			this.key = key;
			this.button = button;
			this.state = State.Up;
		}
	}
	
	
	/** A list of buttons */
	private List<Button> buttons;
	
	/** "Analogue" stick */
	private Vector2 stick;
	
	
	/**
	 * Constructor
	 */
	public Gamepad() {
		
		// Initialize components
		buttons = new ArrayList<Button> ();
		stick = new Vector2();
	}
	
	
	/**
	 * Add a new button
	 * @param key Keyboard key
	 * @param joybutton Joystick button
	 */
	public void addButton(int key, int joybutton) {
		
		buttons.add(new Button(key, joybutton));
	}
	
	
	/**
	 * Update gamepad
	 * @param input Input manager
	 */
	public void update(InputManager input) {
;
		
		// Go through every button and update
		// state
		Button b;
		for(int i = 0; i < buttons.size(); ++ i) {
			
			b = buttons.get(i);
			b.state = input.getKeyState(b.key);
		}
		
		// Update stick
		stick.x = 0.0f;
		stick.y = 0.0f;
		
		if(input.getKeyState(GLFW_KEY_LEFT) == State.Down)
			stick.x = -1.0f;
		
		else if(input.getKeyState(GLFW_KEY_RIGHT) == State.Down)
			stick.x = 1.0f;
		
		if(input.getKeyState(GLFW_KEY_UP) == State.Down)
			stick.y = -1.0f;
		
		else if(input.getKeyState(GLFW_KEY_DOWN) == State.Down)
			stick.y = 1.0f;
		
		// Normalize if needed
		float dist = (float)Math.hypot(stick.x, stick.y);
		if(dist >= 1.0f) {
			
			stick.x /= dist;
			stick.y /= dist;
		}
	}
	
	
	/**
	 * Get a button state
	 * @param id Button ID
	 * @return Button state
	 */
	public State getButtonState(int id) {
		
		// If out of range, return up
		if(id < 0 || id >= buttons.size())
			return State.Up;
		
		return buttons.get(id).state;
	}
	
	
	/**
	 * Get the "analogue" stick
	 * @return Stick
	 */
	public Vector2 getStick() {
		
		return stick.clone();
	}
	
}
