package application.gamefield;

import application.Gamepad;
import core.renderer.Bitmap;
import core.renderer.Flip;
import core.renderer.Graphics;
import core.types.Direction;
import core.types.Point;
import core.utility.AssetPack;

/**
 * A movable crate
 * @author Jani Nykänen
 *
 */
public class Crate extends NonPlayerFieldObject {

	/** A bitmap of movable object */
	private static Bitmap bmpMovable;
	
	/** Is the player character next to the crate */
	private boolean isPlayerClose = false;
	/** Player character direction (from player's perspective) */
	private Direction playerDir;
	
	
	/**
	 * Initialize global crate content
	 * (read: get assets)
	 * @param assets Assets
	 */
	static public void init(AssetPack assets) {
		
		// Get assets
		bmpMovable = assets.getBitmap("movable");
	}
	
	
	/**
	 * Move crate
	 * @param vpad Virtual gamepad
	 */
	private void move(Gamepad vpad, Stage stage) {
	
		if(playerDir == Direction.None ||
			vpad.getDirection() != playerDir) return;
		
		// TODO: Repeating code from Player. Add
		// universal method?
		// Check direction
		Point t = pos.clone();
		switch(playerDir) {
		
		case Down:		
			++ t.y;
			break;
			
		case Left:
			-- t.x;
			break;
			
		case Right:
			++ t.x;
			break;
			
		case Up:
			-- t.y;
			break;
			
		default:
			break;
		
		}
		
		boolean doMove = true;
		// Check if not out of bounds
		if(t.x < 0 || t.y < 0 
			|| t.x >= stage.getWidth() || t.y >= stage.getHeight()) {
			
			doMove = false;
		}
		
		// Check if a free tile
		if(stage.isSolidExcludeLava(t.x, t.y))
			doMove = false;
		
		// If no obstacles, move
		if(doMove) {
			
			target.x = t.x;
			target.y = t.y;
			moving = true;

			// Update solid data
			stage.updateTileData(pos.x, pos.y, 0);
		}
		
	}
	
	
	/**
	 * Constructor
	 * @param pos Target position
	 */
	public Crate(Point pos) {
		
		super(pos);
	}


	@Override
	public void update(Gamepad vpad, TimeManager tman, Stage stage, float tm) {
		
		if(!exist) return;
	}
	

	@Override
	public void draw(Graphics g) {
		
		if(!exist) return;
		
		g.drawScaledBitmapRegion(bmpMovable,0,0,128,128,
				vpos.x, vpos.y, scaleValue.x, scaleValue.y, Flip.NONE);
			
	}


	@Override
	public void playerCollision(Player pl, Gamepad vpad, Stage stage, TimeManager tman) {
		
		if(!exist || tman.isWaiting() || pl.isMoving()) return;
		
		Point p = pl.getPosition();
		
		// Check if next to this object
		if((int)Math.abs(p.x-pos.x) + (int)Math.abs(p.y-pos.y) == 1) {
			
			// Check direction
			// NOTE: Player direction is actually direction from player's
			// perspective
			if(p.y < pos.y) playerDir = Direction.Down;
			else if(p.y > pos.y) playerDir = Direction.Up;
			else if(p.x < pos.x) playerDir = Direction.Right;
			else if(p.x > pos.x) playerDir = Direction.Left;
			
			// Move
			move(vpad, stage);
		}
		else {
			
			playerDir = Direction.None;
		}
	}

	
	@Override
	public void onMovingStopped(Stage stage) {
		
		// If lava, die
		if(stage.getTile(pos.x, pos.y) == 3) {
			
			exist = false;
			stage.updateTileData(pos.x, pos.y, 0);
		}
		else {
			
			stage.updateTileData(pos.x, pos.y, 2);
		}
	};
}
