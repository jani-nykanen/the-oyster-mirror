package application.ending;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.Transition;
import application.global.Transition.Mode;
import application.global.Transition.Type;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.types.Vector2;
import core.utility.AssetPack;
import core.utility.RGBFloat;
import core.utility.VoidCallback;

/**
 * The ending scene
 * @author Jani Nykänen
 *
 */
public class Ending extends Scene {

	/** Bad ending text */
	static private final String[] BAD_ENDING = new String[] {
		"Congratulations!",
		"",
		"You beat the game. Kind of.",
		"But the real ending is in",
		"another castle!",
	};
	
	/** Good ending text */
	static private final String[] GOOD_ENDING = new String[] {
		"Congratulations! (For real)",
		"",
		"We would like to show you",
		"the real ending. But...",
		" we don't have any.",
		"It really wasn't worth it.",
	};
	
	/** Letter time */
	static private final float PHASE_TIME = 90.0f;
	
	/** Special phase time */
	static private final float SPECIAL_PHASE_TIME = 120.0f;
	
	
	/** Ending mode type */
	public enum EndingMode {
		
		Bad, Good
	};
	
	/** Font bitmap */
	private Bitmap bmpFont;
	/** Ending text bitmap */
	private Bitmap bmpEnding ;
	
	/** Transition object */
	private Transition trans;
	
	/** Ending mode */
	private EndingMode mode = EndingMode.Bad;
	/** Ending text */
	private String[] endingText;
	
	/** Ending timer */
	private float timer;
	/** Phase */
	private int phase;
	/** Maximum phase */
	private int phaseMax;
	/** If special phase */
	private boolean specialPhase;
	/** Special phase timer */
	private float specialPhaseTimer;
	
	
	/**
	 * Leave for the stage screen
	 * @param mode If to leave to the main menu
	 */
	private void leave(boolean mode) {
		
		trans.activate(Mode.In, Type.Fade, 1.0f, new RGBFloat(), new
				VoidCallback() {
					@Override
					public void execute(int index) {
						
						sceneMan.changeScene(mode ? "title" : "stagemenu", null);
					}
		});
	}
	
	
	/**
	 * Draw the ending logo
	 * @param g Graphics object
	 */
	private void drawEndLogo(Graphics g) {
		
		final float SCALE = 2.0f;
		
		float w = bmpEnding.getWidth() * SCALE;
		float h = bmpEnding.getHeight() * SCALE;
		
		float a = 1.0f;
		if(specialPhaseTimer > 0.0f) {
			
			a = 1.0f - specialPhaseTimer / (SPECIAL_PHASE_TIME/2.0f);
		}
		g.setGlobalAlpha(a);
		g.setColor();
		g.drawScaledBitmap(bmpEnding,-w/2, -h/2, w, h, Flip.NONE);
		g.setGlobalAlpha();
	}
	
	
	@Override
	public void init(AssetPack assets) throws Exception {

		name = "ending";
		
		// Get bitmaps
		bmpFont = assets.getBitmap("font");
		bmpEnding = assets.getBitmap("ending");
		
		// Get transition object
		Global g = (Global)sceneMan.getGlobalScene();
		trans = g.getTransition();
	}

	
	@Override
	public void update(Gamepad vpad, float tm) {

		if(trans.isActive()) return;
		
		// Update special phase
		if(specialPhase) {
			
			if(specialPhaseTimer > 0.0f) {
				
				specialPhaseTimer -= 1.0f * tm;
			}
			else {
				
				if(vpad.anyButtonPressed()) {
					// Leave
					leave(true);
				}
			}
			return;
		}
		
		// Update timer
		if(phase < phaseMax) {
			
			timer += 1.0f * tm;
			if(timer >= PHASE_TIME) {
				
				++ phase;
				timer -= PHASE_TIME;

			}
		}
		// Else wait for key press
		else {
			
			if(vpad.anyButtonPressed()) {
				
				if(mode == EndingMode.Good) {
					
					specialPhase = true;
					specialPhaseTimer = SPECIAL_PHASE_TIME;
				}
				else {
				
					// Bye bye
					leave(false);
				}
			}
		}
	}

	
	@Override
	public void draw(Graphics g) {

		final float TEXT_SCALE = 0.75f;
		final float YOFF = 64.0f;
		final float XOFF = -26.0f;
		final float SHADOW_ALPHA = 0.5f;
		final float SHADOW_OFF = 8.0f;

		// Calculate starting y
		float y = -phaseMax * YOFF / 2;
		
		// Set view
		Transformations tr = g.transform();
		Vector2 view = tr.getViewport();
		
		tr.fitViewHeight(Global.DEFAULT_VIEW_HEIGHT);
		tr.identity();
		tr.translate(view.x/2, view.y/2);
		tr.use();
		
		g.clearScreen(1, 1, 1);
		
		// Draw ending logo
		float spcTarget = SPECIAL_PHASE_TIME/2.0f;
		if(specialPhase) {
			
			if(specialPhaseTimer < spcTarget) {
				
				drawEndLogo(g);
				return;
			}
			else {
				
				// Fade the text out
				float t = (specialPhaseTimer - spcTarget) / spcTarget;
				g.setGlobalAlpha(t);
			}
		}

		// Draw text
		for(int i = 0; i < (int)Math.min(phase, endingText.length); ++ i) {
			
			// Calculate alpha
			if(!specialPhase && i == phase -1) {
				
				float t = timer / PHASE_TIME;
				g.setGlobalAlpha(t);
			}
			
			// Shadow
			g.setColor(0, 0, 0, SHADOW_ALPHA);
			g.drawText(bmpFont, endingText[i], SHADOW_OFF, 
					y + i * YOFF + SHADOW_OFF, 
					XOFF, 0.0f, true, TEXT_SCALE);
			
			// Base text
			g.setColor(1, 1, 0);
			g.drawText(bmpFont, endingText[i], 0.0f, y + i * YOFF, XOFF, 0.0f, true, TEXT_SCALE);
			
			if(!specialPhase)
				g.setGlobalAlpha();
		}
		
		g.setGlobalAlpha();
	}

	
	@Override
	public void destroy() {

		// ...
	}

	
	@Override
	public void changeTo() {

		// Get ending mode (if any given)
		int[] p = (int[])param;
		if(p != null) {
			
			mode = p[0] == 0 ? EndingMode.Bad : EndingMode.Good;
			
			endingText = mode == EndingMode.Bad ? BAD_ENDING :
				GOOD_ENDING;
			phaseMax = endingText.length +1;
		}
		
		phase = 0;
		timer = 0.0f;
		specialPhase = false;
		specialPhaseTimer = 0.0f;
	}

}
