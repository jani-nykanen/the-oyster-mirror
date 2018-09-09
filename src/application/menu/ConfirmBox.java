package application.menu;

import application.Gamepad;
import application.global.Global;
import application.global.SaveManager;
import application.ui.Button;
import application.ui.MenuContainer;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.VoidCallback;

public class ConfirmBox extends MenuContainer {

	/** Button count */
	static private final int BUTTON_COUNT = 2;
	
	/** Phase */
	private int phase = 0;
	
	
	@Override
	protected void activationEvent() { 
		
		phase = 0;
		buttons.setCursorPos(1);
	}
	
	
	/**
	 * Draw title
	 * @param g Graphics object
	 * @param text Title text
	 * @param height Box height
	 * @param ypos Y position
	 * @param scale Scale
	 */
	private void drawTitle(Graphics g, String text, float height, float ypos, 
			float scale, float shadowOff) {
		
		float XOFF = -26.0f;
		float TEXT_SCALE = 0.80f;
		final float SHADOW_ALPHA = 0.5f;
		
		// Draw shadow
		g.setColor(0, 0, 0, SHADOW_ALPHA);
		g.drawText(bmpFont, text, shadowOff,
					-height/2 + ypos + shadowOff, XOFF, 0.0f, true, TEXT_SCALE);
		
		// Draw actual title
		g.setColor(1, 1, 0);
		g.drawText(bmpFont, text, 0.0f,
				-height/2 + ypos, XOFF, 0.0f, true, TEXT_SCALE);
	}
	
	
	/**
	 * Constructor
	 * @param saveMan Save manager
	 */
	public ConfirmBox(SaveManager saveMan) {
		
		final String[] BUTTON_TEXT = new String[] {
			"Yes", "No",	
		};
		
		// Create callbacks for buttons
		VoidCallback[] cbs = new VoidCallback[BUTTON_COUNT];
		
		// Yes
		cbs[0] = new VoidCallback() {

			@Override
			public void execute(int index) {
				
				saveMan.reset();
				try {
					
					saveMan.saveGame(Global.SAVE_DATA_PATH);
				}
				catch(Exception e) {}
				
				phase = 1;
			}
		};
		// No
		cbs[1] = new VoidCallback() {

			@Override
			public void execute(int index) {
				
				timer = TRANSITION_TIME;
				leaving = true;
			}
		};
		
		// Create buttons
		for(int i = 0; i < BUTTON_COUNT; ++ i) {
			
			buttons.addButton(new Button(BUTTON_TEXT[i], cbs[i]));
		}
	}
	
	
	@Override
	protected void updateEvent(Gamepad vpad, float tm) {
		
		if(phase == 0) {
			// Update buttons
			buttons.update(vpad, tm);
		
		}
		else {
			
			// Check if any pressed, then quit
			if(vpad.anyButtonPressed()) {
				
				timer = TRANSITION_TIME;
				leaving = true;
			}
		}
	}
	

	@Override
	public void draw(Graphics g) {
		
		final float BOX_WIDTH = 512;
		final float BOX_HEIGHT = 256;
		final float SHADOW_OFFSET = 16;
		final float TEXT_X = 96.0f;
		final float TEXT_Y = 96.0f;
		final float XOFF = -26.0f;
		final float YOFF = 64.0f;
		final float TEXT_SCALE = 0.80f;
		final float SHADOW_OFF = 8.0f;
		final float CONFIRM_OFF = 16.0f;
		final float DONE_OFF = 128 - 32*TEXT_SCALE;
		final String CONFIRM_TEXT = "Are you sure?";
		final String DONE_TEXT = "Data cleared.";
		
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
		
		// Draw title
		drawTitle(g, phase == 0  ? CONFIRM_TEXT : DONE_TEXT, 
				BOX_HEIGHT, phase == 0 ? CONFIRM_OFF : DONE_OFF, TEXT_SCALE,
				SHADOW_OFF);
		
		// Draw buttons
		if(phase == 0) {
			
			buttons.draw(g, bmpFont, -BOX_WIDTH/2 + TEXT_X, -BOX_HEIGHT/2 + TEXT_Y, 
					XOFF, YOFF, TEXT_SCALE, false, SHADOW_OFF);
		}
				
		g.setGlobalAlpha();
		tr.pop();
	}

}
