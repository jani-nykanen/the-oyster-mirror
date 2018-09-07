package application.global;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import application.Gamepad;
import application.ui.Button;
import application.ui.MenuContainer;
import core.WeakEventManager;
import core.audio.AudioManager;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.CSVParser;
import core.utility.VoidCallback;

/**
 * Settings
 * @author Jani Nykänen
 *
 */
public class Settings extends MenuContainer {

	/** Default settings path */
	static public final String DEFAULT_PATH = "settings.dat";
	/** Amount of buttons */
	static final private int BUTTON_COUNT = 5;
	
	/** Button texts */
	static final private String[] BUTTON_TEXT = new String[] {
		
		"Toggle Fullscreen",
		"Music Volume: ",
		"Sound volume: ",
		"Framerate: ",
		"Back"
	};
	
	/** Reference to the event manager */
	private WeakEventManager eventMan;
	
	
	/**
	 * Update volume button text
	 * @param b Button
	 * @param index Index
	 * @param vol Volume
	 */
	private void updateVolumeButtonText(Button b, int index, int vol) {
		
		b.setText(BUTTON_TEXT[index] + Integer.toString(vol*10) + "%");
	}
	
	
	
	/**
	 * Save settings
	 * @param path Path
	 * @throws Exception If fails to close writer
	 */
	private void saveSettings(String path) throws Exception {
		
		Writer writer = null;
		try {
			
			// Open/create file for writing
			writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(path), "utf-8"));
			
			// Write full screen state
			writer.write(
					Integer.toString(eventMan.getFullscreenState() ? 1 : 0) + "\n");
			// Write music volume
			writer.write(Integer.toString(
					eventMan.getAudioManager().getMusicVolume()
					) + "\n");
			// Write sound volume
			writer.write(Integer.toString(
					eventMan.getAudioManager().getSoundVolume())
					+ "\n");
			// Write FPS
			writer.write(Integer.toString(
					eventMan.getFrameRate())
					+ "\n");

			
		} catch (Exception e) {

			e.printStackTrace();
		}
		finally {
			
			writer.close();
		}
	}
	
	
	/**
	 * Constructor
	 */
	public Settings(WeakEventManager eventMan) {
		
		super();
		
		// Store reference to the event manager
		// (we need it while saving/loading data)
		this.eventMan = eventMan;
		
		// Get audio manager
		AudioManager audioMan = eventMan.getAudioManager();
		
		// Create callbacks for buttons
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		// Directional callbacks
		VoidCallback[] leftCbs = new VoidCallback[BUTTON_COUNT];
		VoidCallback[] rightCbs = new VoidCallback[BUTTON_COUNT];
		// Fullscreen
		cbs[0] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				eventMan.toggleFullscreen();
			}
		};
		leftCbs[0] = null;
		rightCbs[0] = null;
		
		// Music volume
		cbs[1] = null;
		leftCbs[1] = new VoidCallback() {
			@Override
			public void execute(int index) {

				audioMan.setMusicVolume(audioMan.getMusicVolume() -1);
				updateVolumeButtonText(buttons.getButton(1), 1, audioMan.getMusicVolume());
			}
		};
		rightCbs[1] = new VoidCallback() {
			@Override
			public void execute(int index) {

				audioMan.setMusicVolume(audioMan.getMusicVolume() +1);
				updateVolumeButtonText(buttons.getButton(1), 1, audioMan.getMusicVolume());
			}
		};
		
		// Sound volume
		cbs[2] =  null;
		leftCbs[2] = new VoidCallback() {
			@Override
			public void execute(int index) {

				audioMan.setSoundVolume(audioMan.getSoundVolume() -1);
				updateVolumeButtonText(buttons.getButton(2), 2, audioMan.getSoundVolume());
			}
		};
		rightCbs[2] = new VoidCallback() {
			@Override
			public void execute(int index) {

				audioMan.setSoundVolume(audioMan.getSoundVolume() +1);
				updateVolumeButtonText(buttons.getButton(2), 2, audioMan.getSoundVolume());
			}
		};
		
		// FPS
		cbs[3]  = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				// Update frame rate
				int fps = eventMan.getFrameRate();
				fps = fps == 30 ? 60 : 30;				
				eventMan.setFrameRate(fps);
				
				// Update string
				buttons.getButton(3).setText(BUTTON_TEXT[3] + Integer.toString(fps) + " FPS");
			}
		};
		leftCbs[3] = cbs[3];
		rightCbs[3] = cbs[3];
		
		// Back
		cbs[4] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				timer = TRANSITION_TIME;
				leaving = true;
				
				// Save settings
				try {
					
					saveSettings(DEFAULT_PATH);
				}
				catch(Exception e) {}
			}
		};
		leftCbs[4] = null;
		rightCbs[4] = null;
		
		// Create buttons
		for(int i = 0; i < BUTTON_COUNT; ++ i) {
			
			buttons.addButton(new Button(BUTTON_TEXT[i], cbs[i], leftCbs[i], rightCbs[i]));
		}
		
		// Update button texts
		int fps = eventMan.getFrameRate();
		buttons.getButton(3).setText(BUTTON_TEXT[3] + Integer.toString(fps) + " FPS");
		updateVolumeButtonText(buttons.getButton(1), 1, audioMan.getMusicVolume());
		updateVolumeButtonText(buttons.getButton(2), 2, audioMan.getSoundVolume());
	}
	
	
	@Override
	protected void activationEvent() { 
		
		buttons.setCursorPos(BUTTON_COUNT-1);
		
		// Read settings
		try {
			
			load();
		}
		catch(Exception e) {}
	}
	
	
	@Override
	public void updateEvent(Gamepad vpad, float tm) {
		
		// Update buttons
		buttons.update(vpad, tm);
	}

	
	@Override
	public void draw(Graphics g) {
		
		final float BOX_WIDTH = 720;
		final float BOX_HEIGHT = 384;
		final float SHADOW_OFFSET = 16;
		final float TEXT_X = 48.0f;
		final float TEXT_Y = 32.0f;
		final float XOFF = -26.0f;
		final float YOFF = 64.0f;
		final float TEXT_SCALE = 0.80f;
		final float SHADOW_OFF = 8.0f;
		
		if(!active) return;
		
		// Center the screen
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		tr.push();
		tr.translate(view.x / 2, view.y / 2);
		tr.use();
		
		// Set global alpha
		float alpha = timer > 0.0f ? 1.0f - timer / TRANSITION_TIME : 1.0f;
		if(leaving)
			alpha = 1.0f - alpha;
		
		g.setGlobalAlpha(alpha);
				
		// Draw a box
		drawBox(g, BOX_WIDTH , BOX_HEIGHT, SHADOW_OFFSET);
		
		// Draw buttons
		buttons.draw(g, bmpFont, -BOX_WIDTH/2 + TEXT_X, -BOX_HEIGHT/2 + TEXT_Y, 
				XOFF, YOFF, TEXT_SCALE, false, SHADOW_OFF);
				
		g.setGlobalAlpha();
		tr.pop();
	}

	
	/**
	 * Load settings
	 * @param path Path
	 * @throws Exception If fails to close reader
	 */
	public void load(String path) throws Exception {
		
		BufferedReader reader = null;
		try {
			
			reader = new BufferedReader(new FileReader(path));
			
			// Read fullscreen
			boolean state = Integer.parseInt(reader.readLine()) == 1;
			if(state != eventMan.getFullscreenState())
				eventMan.toggleFullscreen();
			
			// Read music volume
			eventMan.getAudioManager().setMusicVolume(
					Integer.parseInt(reader.readLine())
					);
			
			// Read sound volume
			eventMan.getAudioManager().setSoundVolume(
					Integer.parseInt(reader.readLine())
					);
			
			// Read fps
			eventMan.setFrameRate(
					Integer.parseInt(reader.readLine())
					);
		}
		catch(FileNotFoundException e) {
			
			System.out.println("Could not find " + path 
					+ ", maybe because it does not exist yet?");
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		finally {
			
			reader.close();
		}
	}
	
	
	/**
	 * Load settings in the default location
	 * @throws Exception If fails to close the reader object
	 */
	public void load() throws Exception {
	
		load(DEFAULT_PATH);
	}
}
