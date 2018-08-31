package application.gamefield;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.Transition;
import core.State;
import core.renderer.Graphics;
import core.renderer.Transformations;
import core.utility.AssetPack;
import core.utility.RGBFloat;
import core.utility.VoidCallback;


/**
 * A game field scene.
 * @author Jani Nykänen
 *
 */
public class GameField extends Scene {

	/** Scene name */
	public String name = "game";

	/** Stage manager */
	private Stage stage;
	/** Time manager */
	private TimeManager timeMan;
	/** Object manager */
	private ObjectManager objMan;
	/** Status manager */
	private StatusManager statMan;
	/** Transitions */
	private Transition trans;
	/** Pause object */
	private Pause pause;
	/** Stage clear screen object */
	private StageClearScreen stageClear;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Create stage
		stage = new Stage(assets);
		stage.loadMap(1);
		
		// Initialize object manager
		ObjectManager.init(assets);
		// Create object manager & objects
		objMan = new ObjectManager();
		stage.createObjects(objMan);
		
		// Create time manager
		timeMan = new TimeManager();
		
		// Create status manager
		StatusManager.init(assets);
		statMan = new StatusManager();
		statMan.setInitial(stage);
		
		// Get global transition manager
		Scene global = sceneMan.getGlobalScene();
		if(global != null)
			trans = ( (Global)global ).getTransition();
				
		// Create pause
		Pause.init(assets);
		pause = new Pause(this);
		
		// Create "Stage clear" object
		StageClearScreen.init(assets);
		stageClear = new StageClearScreen(this);
		
		// Fade in
		trans.activate(Transition.Mode.Out, Transition.Type.Fade, 2.0f, new RGBFloat(), null);
	}
	

	@Override
	public void update(Gamepad vpad, float tm) {
		
		// No updating if fading, except possibly
		// fading pause screen
		if(trans.isActive()) {
			
			pause.fadeOnly(tm);
			return;
		}
		
		// If stage clear screen active, update it and
		// ignore the rest
		if(stageClear.isActive()) {
			
			stageClear.update(vpad, tm);
			return;
		}
		
		// If pause is active, update pause
		if(pause.isActive()) {
			
			pause.update(vpad, tm);
			return;
		}
		// If not, check if pause button pressed
		else if(vpad.getButtonByName("confirm") == State.Pressed) {
			
			pause.activate();
			return;
		}
		
		// Update stage
		stage.update(tm);
		// Update time manager
		timeMan.update(tm);
		// Update game objects
		objMan.update(vpad, timeMan, stage, statMan, tm);
		
		// Reset button
		if(vpad.getButtonByName("reset") == State.Pressed) {
			
			fadeAndReset();
		}
		
		// Check if the stage has ended
		if(stage.hasStageEnded()) {
			
			stageClear.activate(timeMan.getTurn() <= stage.getTurnLimit());
		}

	}
	

	@Override
	public void draw(Graphics g) {
		
		// TODO: Put this elsewhere
		final float DEFAULT_VIEW_HEIGHT = 720.0f;

		// Clear screen
		g.clearScreen(1,1,1);
		
		// Set transformations
		Transformations tr = g.transform();

		// Draw stage
		if(!stageClear.isActive()) {
			
			stage.setTransform(g, trans.isActive(), trans.getMode(), trans.getTimer());
		}
		else if(stageClear.isFadingIn()) {
			
			stage.setTransform(g, true, Transition.Mode.In, 
					stageClear.getFadeValue());
		}
		
		// If not fading, but stage clear is active, then we don't have to draw
		// the stage or the objects
		if(! (stageClear.isActive() && !stageClear.isFadingIn()) ) {
			
			stage.draw(g);
			
			// Draw game objects
			objMan.draw(g);
		}
		
		// Set screen transformations
		tr.fitViewHeight(DEFAULT_VIEW_HEIGHT);
		tr.identity();
		tr.use();
		
		// Draw status manager
		statMan.draw(g);
		
		// Draw pause
		pause.draw(g);
		
		// Draw "Stage Clear" screen
		stageClear.draw(g, trans);
	}
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void changeTo() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Reset game
	 */
	public void reset() {
		
		// Reset stage
		stage.resetMap();
		
		// Create object manager & objects
		objMan = new ObjectManager();
		stage.createObjects(objMan);
		
		// Create time manager
		timeMan = new TimeManager();
		
		// Create status manager
		statMan = new StatusManager();
		statMan.setInitial(stage);
		
		// Reset stage clear object
		stageClear.reset();
	}
	
	
	/**
	 * Fade in and reset
	 */
	public void fadeAndReset() {
		
		// Fade in and reset
		trans.activate(Transition.Mode.In, Transition.Type.Fade, 2.0f, new RGBFloat(), 
			new VoidCallback() {
				@Override
				public void execute() {
					reset();	
				}
		});
	}
	
	
	/**
	 * Terminate application, "in style"
	 */
	public void quit() {

		// Fade in and quit
		trans.activate(Transition.Mode.In, Transition.Type.Fade, 2.0f, new RGBFloat(0, 0, 0), 
			new VoidCallback() {
				@Override
				public void execute() {
					
					eventMan.quit();	
				}
		});
	}
	
}
