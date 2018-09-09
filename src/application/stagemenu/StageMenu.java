package application.stagemenu;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.SaveManager;
import application.global.Transition;
import application.global.Transition.Mode;
import application.ui.Button;
import application.ui.VerticalButtonList;
import core.State;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;
import core.utility.RGBFloat;
import core.utility.Tilemap;
import core.utility.VoidCallback;

/**
 * Stage menu scene
 * @author Jani Nykänen
 *
 */
public class StageMenu extends Scene {

	/** Amount of buttons (= stage count + back button) */
	static public final int BUTTON_COUNT = 35 +1;
	
	static private final float BUTTONS_YPOS = 32;
	static private final float BUTTONS_YOFF = 56.0f;
	
	/** Font bitmap */
	private Bitmap bmpFont;
	
	/** Stage buttons */
	private VerticalButtonList stageButtons;
	/** Transitions */
	private Transition trans;
	/** Save game manager */
	private SaveManager saveMan;
	/** Mirror aka map */
	private Mirror mirror;
	
	/** Stage index */
	private int stageIndex = 0;
	/** Stage names */
	private String[] stageNames;
	/** Difficulties */
	private int[] difficulties;
	/** Is leaving */
	private boolean leaving = false;
	/** If entering (from main menu) */
	private boolean entering = false;
	/** Latest stage */
	private int latestStage = 1;
	
	
	/**
	 * Quit
	 */
	private void quit() {
		
		// Fade in and quit
		leaving = true;
		trans.activate(Transition.Mode.In, Transition.Type.Fade, 
				2.0f, new RGBFloat(1, 1, 1), 
		new VoidCallback() {
			@Override
			public void execute(int index) {
			
				// eventMan.quit();
				sceneMan.changeScene("title");
			}
		});
	}
	
	
	/**
	 * Calculate cursor y translation
	 * @return Translation
	 */
	private float calculateCursorTranslation() {
		
		float p = BUTTONS_YPOS + 
				BUTTONS_YOFF * (latestStage + (latestStage == BUTTON_COUNT-1 ? 1 : 2)) 
					-Global.DEFAULT_VIEW_HEIGHT;
		if(p < 0) p = 0;
		
		return p;
	}
	
	
	/**
	 * Reset buttons
	 */
	private void resetButtons() {
		
		latestStage = saveMan.getCurrentStageIndex();
		
		// Reset buttons
		Button b;
		for(int i = 1; i < BUTTON_COUNT; ++ i) {
			
			updateButtonText(i, saveMan.getCompletionInfo(i));
			
			b = stageButtons.getButton(i);
			if(i > latestStage) {
				
				b.disable();
			}
		}
		
		// Set cursor position
		stageButtons.setCursorPos(latestStage);
		
		// Calculate cursor position
		stageButtons.setYTranslation(-calculateCursorTranslation());
		
	}
	
	
	/**
	 * Get difficulty string (i.e stars)
	 * @param dif Difficulty level (from 1 to n)
	 * @return Difficulty string
	 */
	private String getDifficultyString(int dif) {
		
		String str = "";
		
		// Add full stars
		for(int i = 0; i < dif/2; ++ i) {
			
			str += "~";
		}
		
		// Add a half star, if needed
		if(dif % 2 == 1) {
			
			str += Character.toString((char)('~'+1));
		}
		
		return str;
	}
	
	
	/**
	 * Change to the game scene
	 */
	private void changeToGame() {
		
		final float STAGE_CHANGE_FADE_SPEED = 2.0f;
		
		// Fade in and change scene
		trans.activate(Transition.Mode.In, Transition.Type.Fade, 
				STAGE_CHANGE_FADE_SPEED, new RGBFloat(1, 1, 1), 
		new VoidCallback() {
			@Override
			public void execute(int index) {
				
				sceneMan.changeScene("game", 
						(Object)new int[] {stageIndex});	
			}
		});
	}
	
	
	/**
	 * Get the amount of active buttons (i.e amount of stages
	 * that exist). NOTE: If 1 & 2 exist, and 3 not, it does not 
	 * matter if 4 exists, this returns 2.
	 * @param assets Asset pack
	 * @return Active button count
	 */
	private int getActiveButtonCount(AssetPack assets) {
		
		int count = 0;
		for(int i = 1; i < BUTTON_COUNT; ++ i) {
			
			if(assets.getTilemap(Integer.toString(i)) == null) {
				
				break;
			}
			++ count;
		}
		
		return count;
	}
	
	
	/**
	 * Update button text
	 * @param index Button index
	 * @param value Value
	 */
	private void updateButtonText(int index, int value) {
		
		String s = "Stage " + Integer.toString(index) + " ";
		if(value == 1) s += "}";
		if(value == 2) s += "|";
		
		Button b = stageButtons.getButton(index);
		b.setText(s);
	}
	
	
	/**
	 * Draw the stage buttons (plus "Back")
	 * @param g Graphics object
	 */
	private void drawStageButtons(Graphics g) {
		
		final float XPOS = 32;
		final float XOFF = -28.0f;
		final float TEXT_SCALE = 0.80f;
		final float SHADOW_OFF = 8.0f;
		
		// Draw buttons
		stageButtons.draw(g, bmpFont, XPOS, BUTTONS_YPOS, XOFF, BUTTONS_YOFF, TEXT_SCALE, true, SHADOW_OFF);
	}
	
	
	/**
	 * Draw title
	 * @param g Graphics object
	 */
	private void drawTitle(Graphics g) {
		
		final String TEXT = "Choose a stage";
		final float X_PLUS = -32.0f;
		final float YPOS = 16.0f;
		final float XOFF = -26.0f;
		final float TEXT_SCALE = 0.90f;
		final float SHADOW_OFF = 8.0f;
		final float SHADOW_ALPHA = 0.5f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float xpos = X_PLUS + view.x - (TEXT.length() +1) * ((64.0f+XOFF) * TEXT_SCALE);
		
		// Draw shadow
		g.setColor(0, 0, 0, SHADOW_ALPHA);
		g.drawText(bmpFont, TEXT, xpos + SHADOW_OFF, YPOS + SHADOW_OFF,
				XOFF, 0.0f, false, TEXT_SCALE);
		
		// Draw base text
		g.setColor(1, 1, 0);
		g.drawText(bmpFont, TEXT, xpos, YPOS, XOFF, 0.0f, false, TEXT_SCALE);
	}
	
	
	/**
	 * Draw info box text
	 * @param g Graphics object
	 * @param cpos Cursor position
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	private void drawInfoBoxText(Graphics g, int cpos, float x, float y) {
		
		final float NAME_X = 8.0f;
		final float NAME_Y = 16.0f;
		final float XOFF = -28.0f;
		final float XOFF_STARS = -16.0f;
		final float YOFF = 56.0f;
		final float TEXT_SCALE = 0.60f;
		final float STAR_SCALE = 0.70f;
		
		// Draw stage name
		g.drawText(bmpFont, "NAME: " + stageNames[cpos-1], 
				x + NAME_X, y + NAME_Y,
				XOFF, 0.0f, false, TEXT_SCALE);
				
		// Draw difficulty text
		String dif = "DIFFICULTY: ";
		g.drawText(bmpFont, dif, 
				x + NAME_X, y + NAME_Y + YOFF,
				XOFF, 0.0f, false, TEXT_SCALE);
		
		// Draw star
		String stars = getDifficultyString(difficulties[cpos-1]);
		x += (64.0f+XOFF) * TEXT_SCALE * (dif.length());
		y -= (64.0f * STAR_SCALE) - (64.0f * TEXT_SCALE);
		g.drawText(bmpFont, stars, 
				x + NAME_X, y + NAME_Y + YOFF,
				XOFF_STARS, 0.0f, false, STAR_SCALE);
	}
	
	
	/**
	 * Draw an info box
	 * @param g Graphics object
	 */
	private void drawInfoBox(Graphics g) {
		
		final float WIDTH_P = 0.60f; // 60%
		final float XPOS = 32.0f;
		final float HEIGHT = 128.0f;
		final float YPOS = 32.0f;
		final float SHADOW_OFF = 4.0f;
		final float SHADOW_ALPHA = 0.25f;
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
	
		float w = WIDTH_P * view.x;
		float x = view.x - XPOS - w;
		float y = view.y - YPOS - HEIGHT;
		
		// Draw box
		g.setColor(0, 0, 0, 0.25f);
		g.fillRect(x, y, w, HEIGHT);
		
		// Get cursor position
		int cpos = stageButtons.getCursorPos();
		// If names not available, stop here
		if(cpos == 0 || cpos > latestStage)
			return;
		
		// Draw info box text, shadow
		g.setColor(0, 0, 0, SHADOW_ALPHA);
		drawInfoBoxText(g, cpos, x + SHADOW_OFF, y + SHADOW_OFF);
		
		// Draw info box text, base
		g.setColor(1, 1, 0);
		drawInfoBoxText(g, cpos, x, y);
	}
	
	
	@Override
	public void init(AssetPack assets) throws Exception {

		final float MIRROR_WIDTH = 384.0f;
		final float MIRROR_HEIGHT = 384.0f;
		
		// Set name
		name = "stagemenu";
		
		// Initialize components
		Mirror.init(assets);

		// Create mirror
		mirror = new Mirror(MIRROR_WIDTH, MIRROR_HEIGHT);
		
		// Get assets
		bmpFont = assets.getBitmap("font");
		
		// Create button list
		stageButtons = new VerticalButtonList();
		
		// Get active button count
		int bcount = getActiveButtonCount(assets);
		
		// Get stage names & difficulties
		stageNames = new String[bcount];
		difficulties = new int[bcount];
		Tilemap map;
		for(int i = 1; i <= bcount; ++ i) {
			
			map = assets.getTilemap(Integer.toString(i));
			stageNames[i -1] = map.getProperty("name");
			difficulties[i -1] = Integer.parseInt(map.getProperty("difficulty"));
		}
		
		// Create buttons 
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		cbs[0] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				quit();
			}
		};
		String name = "";
		for(int i = 0; i < BUTTON_COUNT; ++ i) {
			
			Button b = new Button();
			
			// Set stage button callbacks
			if(i > 0) {
				
				cbs[i] = new VoidCallback() {
					@Override
					public void execute(int index) {
						
						stageIndex = index;
						changeToGame();
					}
				};
			}
			b.setCallback(cbs[i]);
			
			// Set name
			name = i == 0 ? "Back" 
					: "Stage " + Integer.toString(i);
			b.setText(name);
			
			// Disable, if not yet ready
			if(i > bcount) {
				
				b.disable();
			}
				
			// Add button
			stageButtons.addButton(b);
		}
		
		
		// Get global objects
		Global g = (Global)sceneMan.getGlobalScene();
		trans = g.getTransition();
		saveMan = g.getSaveManager();
		
		// Get current stage info
		latestStage = saveMan.getCurrentStageIndex();
		for(int i = 1; i < BUTTON_COUNT; ++ i) {
			
			updateButtonText(i, saveMan.getCompletionInfo(i));
			if(i > latestStage) {
				
				stageButtons.getButton(i).disable();
			}
		}
		stageButtons.setCursorPos(latestStage);
		
		// Calculate button menu y translation
		stageButtons.setYTranslation(-calculateCursorTranslation());
	}

	
	@Override
	public void update(Gamepad vpad, float tm) {
		
		// If fading, wait
		if(trans.isActive())
			return;
		else
			entering = false;
		
		// Update buttons
		stageButtons.update(vpad, tm);
		// Update mirror cursor position
		int c = stageButtons.getCursorPos();
		if(c > 0 && c <= latestStage) {
			
			mirror.calculateCursorPosition(BUTTON_COUNT -1, c);
		}
	
		// Update mirror
		mirror.update(tm);
		
		// Check if quit button pressed
		if(vpad.getButtonByName("quit") == State.Pressed) {
			
			quit();
		}
	}

	
	@Override
	public void draw(Graphics g) {
		
		final float FADE_SCALE = 0.25f;
		
		// Clear to white
		g.clearScreen(1.0f, 1.0f, 1.0f);
		
		// Set screen transformations
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		tr.fitViewHeight(Global.DEFAULT_VIEW_HEIGHT);
		tr.identity();
		
		// Scale, if "transitioning"
		if(trans.isActive()) {
			
			float scale = 1.0f;
			if(leaving) {
				
				scale = 1.0f - FADE_SCALE* (1.0f-trans.getTimer());
			}
			else if(entering) {
				
				scale = 1.0f - FADE_SCALE* trans.getTimer();
			}
			else {
				
				if(trans.getMode() == Mode.Out)
					scale = 1.0f + FADE_SCALE* trans.getTimer();
				else
					scale = 1.0f + FADE_SCALE* (1.0f-trans.getTimer());
			}
			
			tr.translate(view.x/2, view.y/2);
			tr.scale(scale, scale);
			tr.translate(-view.x/2, -view.y/2);
			
		}
		tr.use();

		// Draw buttons
		drawStageButtons(g);
		
		// Draw title
		drawTitle(g);
		
		// Draw mirror
		mirror.draw(g);
		
		// Draw info box
		drawInfoBox(g);
	}

	
	@Override
	public void destroy() {
		
		// ...
	}

	
	@Override
	public void changeTo() {
		
		final int REQUIRED_INFO_LENGTH = 3;
		
		leaving = false;
		entering = false;

		
		// Get stage completion information
		int[] info = (int[])param;
		
		if(info != null && info.length == 1 && info[0] == 1) {
			
			// Reset buttons and set the entering flag
			resetButtons();
			entering = true;
		}
		else if(info != null && info.length >= REQUIRED_INFO_LENGTH) {

			boolean toNext = info[2] == 1;
			
			// Save game
			try {
				
				if(saveMan.updateCompletionData(info[0], info[1])) {
					
					// Save game
					updateButtonText(info[0], info[1]);
					
					// Check if new stage unlocked
					if(latestStage < BUTTON_COUNT-1) {
						
						if(info[0] == latestStage) {
							
							++ latestStage;
							stageButtons.getButton(latestStage).enable();
							
							saveMan.increaseStageIndex();
						}
					}

					saveMan.saveGame(Global.SAVE_DATA_PATH);
				}
			}
			catch(Exception e) {
				
				e.printStackTrace();
			}
			
			// Move to the next stage
			if(toNext && stageIndex < BUTTON_COUNT -1) {
				
				++ stageIndex;
				
				// Move cursor
				stageButtons.setCursorPos(stageButtons.getCursorPos() +1);
				
				sceneMan.changeScene("game", 
						(Object)new int[] {stageIndex});	
			}
		}

	}

}
