package application.stagemenu;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.Transition;
import application.ui.Button;
import application.ui.VerticalButtonList;
import core.renderer.Bitmap;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.utility.AssetPack;
import core.utility.RGBFloat;
import core.utility.VoidCallback;

/**
 * Stage menu scene
 * @author Jani Nykänen
 *
 */
public class StageMenu extends Scene {

	/** Amount of buttons (= stage count + back button) */
	static private final int BUTTON_COUNT = 10 +1;
	
	/** Font bitmap */
	private Bitmap bmpFont;
	
	/** Stage buttons */
	private VerticalButtonList stageButtons;
	/** Transitions */
	private Transition trans;
	
	/** Stage index */
	private int stageIndex = 0;
	
	
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
		
		// Create buttons 
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		cbs[0] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				eventMan.quit();
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
				
			
			// Set name
			
			stageButtons.addButton(b);
		}
		
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
		
		// Clear to white
		g.clearScreen(1.0f, 1.0f, 1.0f);
		
		// Set screen transformations
		Transformations tr = g.transform();
		tr.fitViewHeight(Global.DEFAULT_VIEW_HEIGHT);
		tr.identity();
		tr.use();

		// Draw buttons
		stageButtons.draw(g, bmpFont, 32, 32, -26, 56.0f, 0.80f, false, 8.0f);
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
