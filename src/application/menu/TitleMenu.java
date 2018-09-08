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
import core.utility.RGBAFloat;
import core.utility.RGBFloat;
import core.utility.VoidCallback;


/**
 * Title screen menu scene
 * @author Jani Nykänen
 *
 */
public class TitleMenu extends Scene {

	/** Logo bitmap */
	private Bitmap bmpLogo;
	/** Font bitmap */
	private Bitmap bmpFont;
	
	/** Text alpha factor */
	private float textAlphaFactor;
	
	/** Transition object */
	private Transition trans;
	
	/** Is leaving */
	private boolean leaving =false;
	
	
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

		final float SCALE = 1.25f;
		final float YOFF = -64.0f;
		
		// Calculate position & scale
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float w = bmpLogo.getWidth() * SCALE;
		float h = bmpLogo.getHeight() * SCALE;
		
		float x = view.x/2 - w/2;
		float y = view.y/2 - h/2 + YOFF;
		
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
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
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
	
		final String TEXT = "Press any key or button";
		final float YOFF = 200;
		final float BASE_SCALE = 0.75f;
		final float MIN_ALPHA = 0.25f;
		final float SCALE_FACTOR = 0.05f;

		// Calculate positions
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		float x = view.x / 2;
		float y = view.y / 2 + YOFF;
		
		// Set alpha
		float s = (float)Math.sin(textAlphaFactor);
		float t = (1.0f-MIN_ALPHA) / 2.0f;
		float alpha = MIN_ALPHA + (1.0f-t) + (float)Math.sin(textAlphaFactor) * t / 2.0f ;

		// Set scale'
		float scale = BASE_SCALE * (1.0f + s * SCALE_FACTOR);
		
		// Draw text with shadow
		drawShadowedText(g, TEXT, x, y, scale, new RGBAFloat(1.0f, 1.0f, 0.0f, alpha), true);
	
		g.setGlobalAlpha();
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
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Get bitmaps
		bmpLogo = assets.getBitmap("logo");
		bmpFont = assets.getBitmap("font");
		
		// Set name
		name = "title";
	
		// Set defaults
		textAlphaFactor = 0.0f;
		
		// Get global objects
		Global global = (Global)sceneMan.getGlobalScene();
		trans = global.getTransition();
	}

	
	@Override
	public void update(Gamepad vpad, float tm) {
		
		final float TEXT_ALPHA_SPEED = 0.05f;
		
		if(trans.isActive()) return;
		
		// Update text alpha
		textAlphaFactor += TEXT_ALPHA_SPEED * tm;
		
		// If quit button pressed, quit
		if(vpad.getButtonByName("quit") == State.Pressed) {
			
			quit();
		}
		// Check if any button was pressed
		else if(vpad.anyButtonPressed()) {
			
			// Go to the stage menu
			goToStageMenu();
		}
	}
	

	@Override
	public void draw(Graphics g) {
		
		final float FADE_SCALE_IN = 0.5f;
		final float FADE_SCALE_OUT = 0.25f;
		
		g.clearScreen(1, 1, 1);
		
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		tr.fitViewHeight(Global.DEFAULT_VIEW_HEIGHT);
		tr.identity();
		
		// If fading in, scale
		if(trans.isActive() && trans.getMode() == Mode.In) {
		
			float scale = 1.0f + FADE_SCALE_IN* (1.0f-trans.getTimer());
			if(leaving) {
				
				scale = 1.0f - FADE_SCALE_OUT* (1.0f-trans.getTimer());
			}
			
			tr.translate(view.x / 2, view.y/2);
			tr.scale(scale, scale);
			tr.translate(-view.x / 2, -view.y/2);
		}
		tr.use();
		
		// Draw logo
		drawLogo(g);
		// Draw copyright
		drawCopyright(g);
		// Draw "Press any key"
		drawPressAnyKey(g);
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
