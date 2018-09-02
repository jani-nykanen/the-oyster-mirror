package application.stagemenu;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.Transition;
import application.global.Transition.Mode;
import application.ui.Button;
import application.ui.VerticalButtonList;
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
	static private final int BUTTON_COUNT = 30 +1;
	
	/** Font bitmap */
	private Bitmap bmpFont;
	
	/** Stage buttons */
	private VerticalButtonList stageButtons;
	/** Transitions */
	private Transition trans;
	
	/** Stage index */
	private int stageIndex = 0;
	/** Stage names */
	private String[] stageNames;
	/** Difficulties */
	private int[] difficulties;
	/** Is leaving */
	private boolean leaving = false;
	
	
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
						(Object)new Integer(stageIndex));	
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
	 * Draw the stage buttons (plus "Back")
	 * @param g Graphics object
	 */
	private void drawStageButtons(Graphics g) {
		
		final float XPOS = 32;
		final float YPOS = 32;
		final float XOFF = -28.0f;
		final float YOFF = 56.0f;
		final float TEXT_SCALE = 0.80f;
		final float SHADOW_OFF = 8.0f;
		
		// Draw buttons
		stageButtons.draw(g, bmpFont, XPOS, YPOS, XOFF, YOFF, TEXT_SCALE, true, SHADOW_OFF);
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
		if(cpos == 0 || cpos > stageNames.length)
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

		// Set name
		name = "stagemenu";
		
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
				
				// Fade in and quit
				// TEMPORARY
				leaving = true;
				trans.activate(Transition.Mode.In, Transition.Type.Fade, 
						2.0f, new RGBFloat(0, 0, 0), 
				new VoidCallback() {
					@Override
					public void execute(int index) {
					
						eventMan.quit();
					}
				});
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
		// Set to the latest button
		stageButtons.setCursorPos(bcount);
		
		
		// Get transitions object
		trans = ((Global)sceneMan.getGlobalScene()).getTransition();
	}

	
	@Override
	public void update(Gamepad vpad, float tm) {
		
		// If fading, wait
		if(trans.isActive()) return;
		
		// Update buttons
		stageButtons.update(vpad, tm);
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
		
		// Draw info box
		drawInfoBox(g);
	}

	
	@Override
	public void destroy() {
		
		// ...
	}

	
	@Override
	public void changeTo() {
		
		// ...
	}

}
