package application.gamefield;

import application.Gamepad;
import application.Scene;
import application.global.Global;
import application.global.Transition;
import application.global.Transition.Mode;
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
	/** A reference to assets (needed for change to event) */
	private AssetPack assets;
	
	/** Is leaving */
	private boolean leaving = false;
	
	
	@Override
	public void init(AssetPack assets) throws Exception {
		
		// Store reference to assets (TEMPORARY?)
		this.assets = assets;
		
		// Set name
		name = "game";
		
		// Create stage
		stage = new Stage(assets);
		stage.setStage(1, assets);
		
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
		
		// Clear screen
		g.clearScreen(1,1,1);
		
		// Set transformations
		Transformations tr = g.transform();

		// Draw stage
		if(!stageClear.isActive()) {
			
			Mode m = leaving ? Mode.Out : trans.getMode();
			float time = leaving ? 1.0f - trans.getTimer() : trans.getTimer();
			stage.setTransform(g, trans.isActive(), m, time);
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
		tr.fitViewHeight(Global.DEFAULT_VIEW_HEIGHT);
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
		
		Integer i = (Integer)param;
		int stageIndex = i != null ? i.intValue() : 1;
		
		leaving = false;
		
		// Load map
		stage.setStage(stageIndex, assets);
		// Reset
		reset();
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
				public void execute(int index) {
					reset();	
				}
		});
	}
	
	
	/**
	 * Go back to the stage menu
	 */
	public void quit() {

		leaving = true;
		
		// Fade in and quit
		trans.activate(Transition.Mode.In, Transition.Type.Fade, 2.0f, new RGBFloat(1, 1, 1), 
			new VoidCallback() {
				@Override
				public void execute(int index) {
					
					sceneMan.changeScene("stagemenu");	
				}
		});
	}
	
}
