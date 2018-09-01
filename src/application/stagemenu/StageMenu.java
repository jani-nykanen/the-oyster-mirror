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
	/** A global index pointer (read: i). Required for callbacks */
	private int indexPointer = 0;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {

		final float STAGE_CHANGE_FADE_SPEED = 2.0f;
		
		// Set name
		name = "stagemenu";
		
		// Get assets
		bmpFont = assets.getBitmap("font");
		
		// Create button list
		stageButtons = new VerticalButtonList();
		
		// Create buttons 
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		cbs[0] = new VoidCallback() {
			@Override
			public void execute(int index) {
				
				eventMan.quit();
			}
		};
		String name = "";
		for(indexPointer = 0; indexPointer < BUTTON_COUNT; ++ indexPointer) {
			
			// Set stage button callbacks
			if(indexPointer > 0) {
				
				cbs[indexPointer] = new VoidCallback() {
					@Override
					public void execute(int index) {
						
						// Fade in and change scene
						trans.activate(Transition.Mode.In, Transition.Type.Fade, 
								STAGE_CHANGE_FADE_SPEED, new RGBFloat(1, 1, 1), 
						new VoidCallback() {
							@Override
							public void execute(int index) {
								
								System.out.println(index);
								sceneMan.changeScene("game", 
										(Object)new Integer(index));	
							}
						}, index);
					}
				};
			}
			
			name = indexPointer == 0 ? "Back" 
					: "Stage " + Integer.toString(indexPointer);
			stageButtons.addButton(new Button(name, cbs[indexPointer]));
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
