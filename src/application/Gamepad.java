package application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
		
		/** Name */
		public String name;
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
		public Button(String name, int key, int button) {
			
			this.name = name;
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
	public void addButton(String name, int key, int joybutton) {
		
		buttons.add(new Button(name, key, joybutton));
	}
	
	
	/**
	 * Parse a key configuration file
	 * @param path File path
	 * @throws Exception If something goes wrong
	 * TODO: A lot of same code as in Configuration.
	 * A super class for reading stuff or something?
	 */
	public void parseXML(String path) throws Exception {
	
		// Open the file
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(path);
		// Ready for parsing
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(input);
				
		// Get root element
		Element root = doc.getDocumentElement();
		// Get parameters elements, if exist
		NodeList params = root.getElementsByTagName("button");
		// Go through every parameter and get their keys and values
		Node n;
		String name ,key, button;
		for(int i = 0; i < params.getLength(); ++ i) {
					
			n = params.item(i);
			name = n.getAttributes().getNamedItem("name").getTextContent();
			key = n.getAttributes().getNamedItem("key").getTextContent();
			button = n.getAttributes().getNamedItem("joy").getTextContent();
			
			// Add button
			addButton(name, Integer.parseInt(key), Integer.parseInt(button));
		}
				
		// Close file readers etc
		input.close();
	}
	
	
	/**
	 * Update gamepad
	 * @param input Input manager
	 */
	public void update(InputManager input) {

		final float JOY_DELTA = 0.1f;
		
		// Go through every button and update
		// state
		Button b;
		for(int i = 0; i < buttons.size(); ++ i) {
			
			b = buttons.get(i);
			b.state = input.getKeyState(b.key);
			if(b.state == State.Up) {
				
				b.state = input.getButtonState(b.button);
			}
		}
		
		// Update stick
		stick.x = 0.0f;
		stick.y = 0.0f;
		
		// Check joystick first
		Vector2 joy = input.getJoyAxes();
		if((float)Math.hypot(joy.x, joy.y) > JOY_DELTA) {
			
			stick.x = joy.x;
			stick.y = joy.y;
		}
		// If no joystick activity, check keyboard
		else {
		
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
	}
	
	
	/**
	 * Get a button state by button index
	 * @param id Button index
	 * @return Button state
	 */
	public State getButtonByIndex(int id) {
		
		// If out of range, return up
		if(id < 0 || id >= buttons.size())
			return State.Up;
		
		return buttons.get(id).state;
	}
	
	
	/**
	 * Get a button state by button name
	 * @param name Button name
	 * @return Button state
	 */
	public State getButtonByName(String name) {
		
		// Find the button with corresponding name.
		// If does not exist, return up
		for(Button b : buttons) {
			
			if(name.equals(b.name))
				return b.state;
		}
		
		return State.Up;
	}
	
	
	/**
	 * Get the "analogue" stick
	 * @return Stick
	 */
	public Vector2 getStick() {
		
		return stick.clone();
	}
	
}
