package application.gamefield;

import core.types.Point;

/**
 * Goal star
 * @author Jani Nykänen
 *
 */
public class Star extends Collectible {

	
	/**
	 * Constructor
	 * @param pos Position
	 */
	public Star(Point pos) {
		
		super(pos);
		
		id = 2;
		animationMode = AnimationMode.Shrink;
	}
	
	
	@Override
	public void onUpdate(TimeManager tman, Stage stage) {
		
		if(!exist) return;
		
		// Check black hole
		checkBlackHole(stage);
		
		// If turn limit broken, change to a gray star
		if(tman.getTurn() > stage.getTurnLimit())
			id = 3;
	}
	

	@Override
	public void onPlayerCollision(Player pl,  Stage stage) {
		
		if(!exist) return;
		
		// End stage
		stage.endStage();
	}

}
