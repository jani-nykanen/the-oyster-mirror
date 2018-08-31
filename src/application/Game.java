package application;

import static org.lwjgl.glfw.GLFW.*; 

import application.gamefield.GameField;
import application.global.Global;
import application.ui.MenuContainer;
import application.ui.VerticalButtonList;
import core.ApplicationListener;
import core.Configuration;
import core.State;
import core.WeakEventManager;


/**
 * Base game class
 * @author Jani Nykänen
 *
 */
public class Game extends ApplicationListener {
	
	/** Scene manager */
	private SceneManager scenes;
	/** Weak event manager */
	private WeakEventManager eventMan;
	
	/** Virtual game pad */
	private Gamepad vpad;
	
	
	/**
	 * Check default key commands/shortcuts (quit, full screen etc.)
	 */
	private void defaultKeyCommands() {
		
		// Terminate application, always
		if(input.getKeyState(GLFW_KEY_LEFT_CONTROL) == State.Down &&
			input.getKeyState(GLFW_KEY_Q) == State.Pressed) {
			
			terminate();
		}
		
		// Full screen
		if(input.getKeyState(GLFW_KEY_F4) == State.Pressed) {
			
			toggleFullScreen();
		}
	}
	
	
	@Override
	protected void onStart() {
		
		System.out.println("Starting...");
		
		Configuration conf = new Configuration();;
		
		// Create configuration
		try {
			
			conf.parseXML("/config.xml");
		}
		catch(Exception e) {
			
			System.out.println("Failed to read configuration file: " +
				e.getMessage());
			
			// If fails, set default parameters
			conf.setParameter("window_width", "800");
			conf.setParameter("window_height", "450");
			conf.setParameter("window_caption", "Game");
			conf.setParameter("frame_rate", "30");
			conf.setParameter("full_screen", "0");
		
		}
		
		// Bind configuration
		bindConfiguration(conf);
		
	}
	
	
	@Override
	protected void onInit() {
		
		// Create the weak event manager
		eventMan = new WeakEventManager(this);
		
		// Create scene manager and add scenes
		scenes = new SceneManager();
		scenes.setWeakEventManager(eventMan);
		scenes.addGlobalScene(new Global());
		scenes.addScene(new GameField(), true);
		
		// Create virtual gamepad
		vpad = new Gamepad();
		try {
			vpad.parseXML("keyconfig.xml");
			
		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void onLoaded() throws Exception {
		
		// Initialize UI assets
		VerticalButtonList.init(assets);
		MenuContainer.init(assets);
		
		// Initialize scenes
		scenes.init(assets);
	}
	
	
	@Override
	protected void onUpdate(float tm) {
		
		// Check default key commands
		defaultKeyCommands();
	
		// Update the virtual gamepad
		vpad.update(input);
		
		// Update scene(s)
		scenes.update(vpad, tm);
	}
	
	
	@Override
	protected void onDraw() {
		
		// Draw scene(s)
		scenes.draw(graph);
	}
	
	
	@Override
	protected void onDestroy() {
		
		// Destroy scenes
		scenes.destroy();
	}
}
