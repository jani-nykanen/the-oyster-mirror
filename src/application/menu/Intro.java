package application.menu;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.Transition;
import application.global.Transition.Mode;
import application.global.Transition.Type;
import core.State;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;
import core.utility.RGBFloat;
import core.utility.VoidCallback;

/**
 * "Create by" intro
 * @author Jani Nykänen
 *
 */
public class Intro extends Scene {

	/** Initial time */
	static private final float INITIAL_TIME = 90.0f;
	
	/** Font bitmap */
	private Bitmap bmpFont;
	/** Face bitmap */
	private Bitmap bmpFace;
	
	/** Transition object */
	private Transition trans;
	
	/** Timer */
	private float timer = INITIAL_TIME;
	
	
	/**
	 * Draw texts
	 * @param g Graphics object
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param shadow
	 */
	private void drawTexts(Graphics g, float x, float y, boolean shadow) {
		
		final float TEXT_Y_OFF = 40.0f;
		final float XOFF = -26.0f;
		final float SCALE1 = 0.625f;
		final float SCALE2 = 1.0f;
		final float SHADOW_ALPHA = 0.30f;
		
		if(shadow) {
			
			g.setColor(0, 0, 0, SHADOW_ALPHA);
		}
		else {
			
			g.setColor(1, 1, 0);
		}
		
		g.drawText(bmpFont, "Created by",x, y, XOFF, 0.0f, true, SCALE1);
		g.drawText(bmpFont, "Jani Nyk{nen", x, y+ TEXT_Y_OFF, 
				XOFF, 0.0f, true, SCALE2);
	}
	
	
	
	@Override
	public void init(AssetPack assets) throws Exception {

		name = "intro";
		
		// Get bitmaps
		bmpFont = assets.getBitmap("font");
		bmpFace = assets.getBitmap("face");
		
		// Get transition object
		trans = ((Global)sceneMan.getGlobalScene()).getTransition();
		
		// Fade in
		trans.activate(Transition.Mode.Out, Transition.Type.Fade, 1.0f, new RGBFloat(0, 0, 0), null);
	}
	

	@Override
	public void update(Gamepad vpad, float tm) {

		if(trans.isActive()) return;
		
		// Update timer
		timer -= 1.0f * tm;
		if(timer <= 0.0f || vpad.getButtonByName("confirm") == State.Pressed) {
			
			// Move to the title screen
			trans.activate(Mode.In, Type.Fade, 1.0f, new RGBFloat(1, 1, 1), 
					new VoidCallback() {
					@Override
					public void execute(int index) {
						
						sceneMan.changeScene("title");	
					}
				
			});
		}
	}

	
	@Override
	public void draw(Graphics g) {

		final float SCALE = 1.75f;
		final float YOFF = -128.0f;
		
		final float SHADOW_ALPHA = 0.3f;
		final float SHADOW_OFF = 8.0f;
		
		g.clearScreen(1, 1, 1);
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		tr.fitViewHeight(Global.DEFAULT_VIEW_HEIGHT);
		tr.identity();
		tr.use();
		
		// Calculate position & size
		float dw = bmpFace.getWidth() * SCALE;
		float dh = bmpFace.getHeight() * SCALE;
		
		float dx = view.x/2 - dw/2;
		float dy = view.y/2 - dh/2 + YOFF;
		
		// Draw face, shadow
		g.setColor(0, 0, 0, SHADOW_ALPHA);
		g.drawScaledBitmap(bmpFace, dx + SHADOW_OFF, dy + SHADOW_OFF,
				dw, dh, Flip.NONE);
		
		// Draw face, base
		g.setColor();
		g.drawScaledBitmap(bmpFace, dx, dy, dw, dh, Flip.NONE);
		
		// Draw texts, shadow
		drawTexts(g, view.x/2 + SHADOW_OFF, view.y/2 + SHADOW_OFF, true);
		
		// Draw texts, base
		drawTexts(g, view.x/2, view.y/2, false);
	}

	
	@Override
	public void destroy() {

	}

	
	@Override
	public void changeTo() {

	}
	
}
