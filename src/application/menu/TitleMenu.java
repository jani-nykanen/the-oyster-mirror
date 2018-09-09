package application.menu;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.SaveManager;
import application.global.Settings;
import application.global.Transition;
import application.global.Transition.Mode;
import application.global.Transition.Type;
import application.ui.Button;
import application.ui.MenuContainer;
import application.ui.VerticalButtonList;
import core.State;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;
import core.utility.RGBAFloat;
import core.utility.RGBFloat;
import core.utility.VoidCallback;


/**
 * Title screen menu scene
 * @author Jani Nykänen
 *
 */
public class TitleMenu extends Scene {

	/** Initial phase timer */
	static private final float INITIAL_PHASE_TIME = 30.0f;
	/** Button count */
	static private final int BUTTON_COUNT = 4;
	
	/** Logo bitmap */
	private Bitmap bmpLogo;
	/** Font bitmap */
	private Bitmap bmpFont;
	
	/** Text alpha factor */
	private float textAlphaFactor = (float)Math.PI + (float)Math.PI/2.0f;
	
	/** Save manager */
	private SaveManager saveMan;
	/** Transition object */
	private Transition trans;
	/** Settings */
	private Settings settings;
	/** Confirmation box */
	private ConfirmBox confBox;
	
	/** Is leaving */
	private boolean leaving = false;
	/** If entering for the first time */
	private boolean enteringFirstTime = true;
	
	/** Title phase */
	private int phase = 0;
	/** Phase timer */
	private float phaseTimer = INITIAL_PHASE_TIME;
	
	/** Buttons */
	private VerticalButtonList buttons;
	
	
	/**
	 * Quit
	 */
	private void quit() {
		
		leaving = true;
		
		// Fade in and quit
		trans.activate(Transition.Mode.In, Transition.Type.Fade, 
				2.0f, new RGBFloat(0, 0, 0), 
		new VoidCallback() {
			@Override
			public void execute(int index) {
			
				eventMan.quit();
			}
		});
	}
	
	
	/**
	 * Go to the stage menu
	 */
	private void goToStageMenu() {
		
		trans.activate(Mode.In, Type.Fade, 2.0f, new RGBFloat(), 
			new VoidCallback() {
				@Override
				public void execute(int index) {

					// Change scene
					sceneMan.changeScene("stagemenu", new int[] {1});
				}
		});
		
	}
	
	
	/**
	 * Draw logo
	 * @param g Graphics object
	 */
	private void drawLogo(Graphics g) {

		final float SCALE1 = 1.25f;
		final float SCALE2 = 1.0f;
		final float YOFF1 = -64.0f;
		final float YOFF2 = -128.0f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		// Calculate scale & y position
		float scale = SCALE1;
		float yoff = YOFF1;
		if(phase == 1 && phaseTimer > 0.0f) {
			
			float t = phaseTimer / INITIAL_PHASE_TIME;
			scale = SCALE1 * t + (1-t) * SCALE2;
			yoff = YOFF1 * t + (1-t) * YOFF2;
		}
		else if(phase >= 2) {
			
			scale = SCALE2;
			yoff = YOFF2;
		}
		
		float w = bmpLogo.getWidth() * scale;
		float h = bmpLogo.getHeight() * scale;
		
		// Calculate position
		float x = view.x/2 - w/2;
		float y = view.y/2 - h/2 + yoff;
		
		// Draw
		g.toggleAutocrop(false);
		g.setColor();
		g.drawScaledBitmapRegion(bmpLogo, 0, 0, 
				bmpLogo.getWidth(), bmpLogo.getHeight(), x, y, 
				w,h, Flip.NONE);
		g.toggleAutocrop(true);
		
	}
	
	
	/**
	 * Draw a shadowed text with certain fixed properties
	 * @param g Graphics object
	 * @param text Text
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param scale Scale
	 * @param color Color
	 * @param center Whether to center the text
	 */
	private void drawShadowedText(Graphics g, String text, float x, float y, float scale, RGBAFloat color, boolean center) {
	
		final float XOFF = -26.0f;
		final float SHADOW_OFF = 8.0f;
		final float SHADOW_ALPHA = 0.30f;
		
		// Draw shadow
		g.setColor(0, 0, 0, SHADOW_ALPHA);
		g.drawText(bmpFont, text, x + SHADOW_OFF, y + SHADOW_OFF, 
				XOFF, 0.0f, center, scale);
		
		// Draw base text
		g.setColor(color.r, color.g, color.b, color.a);
		g.drawText(bmpFont, text, x, y, XOFF, 0.0f, center, scale);
		g.setColor();
	}
	
	
	/**
	 * Draw the "Press any key" text
	 * @param g Graphics object
	 */
	private void drawPressAnyKey(Graphics g) {
	
		final String TEXT = "Press Enter or #";
		final float YOFF = 200;
		final float BASE_SCALE = 0.75f;
		final float SCALE_FACTOR = 0.05f;

		// Calculate positions
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float x = view.x / 2;
		float y = view.y / 2 + YOFF;
		
		// Set alpha
		float s = (float)Math.sin(textAlphaFactor);

		// Set scale'
		float scale = BASE_SCALE * (1.0f + s * SCALE_FACTOR);
		
		// Draw text with shadow
		drawShadowedText(g, TEXT, x, y, scale, new RGBAFloat(1.0f, 1.0f, 0.0f), true);
	}
	
	
	/**
	 * Draw copyright
	 * @param g Graphics object
	 */
	private void drawCopyright(Graphics g) {
		
		final String TEXT = "(c) 2018 Jani Nyk{nen";
		final float YOFF = -64;
		final float SCALE = 0.75f;

		// Calculate positions
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float x = view.x / 2;
		float y = view.y + YOFF;
		
		// Draw text with shadow
		drawShadowedText(g, TEXT, x, y, SCALE, new RGBAFloat(0.90f, 0.90f, 0.90f), true);
	}
	
	
	/**
	 * Draw buttons
	 * @param g Graphics object
	 */
	private void drawButtons(Graphics g) {
		
		final float X_POS = -192;
		final float Y_POS = 64;
		final float XOFF = -26;
		final float YOFF = 56;
		final float SCALE = 0.75f;
		final float SHADOW_OFF = 8.0f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		buttons.draw(g, bmpFont, view.x/2 + X_POS, view.y/2+Y_POS, 
				XOFF, YOFF, SCALE, false, SHADOW_OFF);
	}
	
	
	/**
	 * Draw dark background
	 * @param g Graphics object
	 * @param cont Container object (e.g Settings, ConfirmBox etc)
	 */
	private void drawMenuContainer(Graphics g, MenuContainer cont) {
		
		final float BG_ALPHA = 0.5f;
		
		if(!cont.isActive()) return;
		
		Vector2 view = g.transform().getViewport();
		
		// Draw background
		float t = 1.0f - cont.getTimerValue();
		g.setColor(0, 0, 0, t * BG_ALPHA);
		g.fillRect(0, 0, view.x, view.y);
		g.setColor();
		
		// Draw container
		cont.draw(g);
	}
	
	
	/**
	 * Update different phases
	 * @param vpad Gamepad
	 * @param tm Time mul.
	 */
	private void updatePhases(Gamepad vpad, float tm) {

	    // Update phases
		switch(phase) {
			
		case 1:	
			
			// Check if enter pressed
			if(vpad.getButtonByName("confirm") == State.Pressed) {
									
				phaseTimer = INITIAL_PHASE_TIME;
			}
					
			break;
				
		case 2:	
			
			// Update buttons
			buttons.update(vpad, tm);

			break;
				
		default:
			break;
			
		}
	}
	
	
	/**
	 * Create buttons
	 */
	private void createButtons() {
	
		final String[] BUTTON_NAMES = new String[] {
				
			"Start game",
			"Settings",
			"Clear data",
			"Quit"
		};
		
		buttons = new VerticalButtonList();
		
		// Set callbacks
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		// "Start game"
		cbs[0] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				goToStageMenu();
			}
		};
		// "Settings"
		cbs[1] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				settings.activate();
			}
		};
		// "Clear data"
		cbs[2] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				confBox.activate();
			}
		};
		// "Quit"
		cbs[3] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				quit();
			}
		};
		
		// Add buttons
		for(int i = 0; i < BUTTON_COUNT; ++ i) {
			
			buttons.addButton(new Button(
					BUTTON_NAMES[i],
					cbs[i]));
		}
	}
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Get bitmaps
		bmpLogo = assets.getBitmap("logo");
		bmpFont = assets.getBitmap("font");
		
		// Set name
		name = "title";

		// Get global objects
		Global global = (Global)sceneMan.getGlobalScene();
		trans = global.getTransition();
		saveMan = global.getSaveManager();
		
		// Create components
		settings = new Settings(eventMan);
		confBox = new ConfirmBox(saveMan);
		
		// Create buttons
		createButtons();

	}

	
	@Override
	public void update(Gamepad vpad, float tm) {
		
		final float TEXT_ALPHA_SPEED = 0.05f;
		
		// If transition active, do nothing
		if(trans.isActive()) {
			
			return;
		}
		else {
			
			enteringFirstTime = false;
		}
		
		// If settings active, update it and ignore
		// the rest
		if(settings.isActive()) {
			
			settings.update(vpad, tm);
			return;
		}
		
		// If confirm box active, update and ignore
		// the rest
		if(confBox.isActive()) {
			
			confBox.update(vpad, tm);
			return;
		}		
		
		// Update phase timer
		if(phaseTimer > 0.0f) {
			
			phaseTimer -= 1.0f * tm;
			if(phaseTimer <= 0.0f) {
				
				++ phase;
			}
			return;
		}
		
		
		// Update text alpha
		textAlphaFactor += TEXT_ALPHA_SPEED * tm;
		
		// If quit button pressed, quit
		if(vpad.getButtonByName("quit") == State.Pressed) {
						
			quit();
		}
		
		// Update phases
		updatePhases(vpad, tm);
		
	}
	

	@Override
	public void draw(Graphics g) {
		
		final float FADE_SCALE_IN = 0.5f;
		final float FADE_SCALE_IN_FIRST = 1.0f;
		final float FADE_SCALE_OUT = 0.25f;

		g.clearScreen(1, 1, 1);
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		tr.fitViewHeight(Global.DEFAULT_VIEW_HEIGHT);
		tr.identity();
		
		// If fading in, scale
		if(trans.isActive()) {
		
			float scale = 1.0f;
			if(trans.getMode() == Mode.In) {
				
				if(leaving) 
					scale = 1.0f - FADE_SCALE_OUT* (1.0f-trans.getTimer());
				
				else
					scale = 1.0f + FADE_SCALE_IN* (1.0f-trans.getTimer());
			}
			else if(enteringFirstTime) {
				
				scale = 1.0f + FADE_SCALE_IN_FIRST* (trans.getTimer());
			}
			
			tr.translate(view.x / 2, view.y/2);
			tr.scale(scale, scale);
			tr.translate(-view.x / 2, -view.y/2);
		}
		tr.use();
		
		// Draw logo
		drawLogo(g);
		
		// Draw no further, if the first phase
		// and fading
		if(phase == 0 && trans.isActive()) return;
		
		// Set transparency
		float alpha = 1.0f;
		if(phaseTimer > 0.0f) {
			
			alpha = 1.0f - 1.0f / INITIAL_PHASE_TIME * phaseTimer;
		}
		
		
		if(phase == 0) {
			
			g.setGlobalAlpha(alpha);
		}
			
		// Draw copyright
		drawCopyright(g);

		// Draw "Press any key"
		if(phase < 2 
				&& !(phase == 1 && phaseTimer > 0.0f)) {
			
			drawPressAnyKey(g);
		}
		
		// Draw buttons
		if(phase == 2 || (phase == 1 && phaseTimer > 0.0f)) {
			
			// Set alpha
			g.setGlobalAlpha(alpha);
			
			// Draw buttons
			drawButtons(g);
		}
		
		g.setGlobalAlpha();
		
		// Draw settings
		drawMenuContainer(g, settings);
		
		// Draw confirm box
		drawMenuContainer(g, confBox);
	}

	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void changeTo() {
		
		// Set defaults
		textAlphaFactor = 0.0f;
	}

}
