package application.gamefield;


/**
 * Manager time (read: turns)
 * @author Jani Nykänen
 *
 */
public class TimeManager {

	/** Default turn length */
	static public final float DEFAULT_TURN_LENGTH = 16.0f;
	
	/** Current turn */
	private int turn;
	/** Turn length in frames */
	private float turnLength;
	/** Turn timer */
	private float turnTimer;
	/** Is turn waiting */
	private boolean waiting;
	
	
	/**
	 * Constructor
	 */
	public TimeManager() {
		
		turn = 0;
		turnLength = DEFAULT_TURN_LENGTH;
		turnTimer = 0.0f;
		waiting = false;
	}
	
	
	/**
	 * Update
	 * @param tm Time mul.
	 */
	public void update(float tm) {
		
		// Update turn timer, if waiting
		if(waiting) {
			
			turnTimer -= 1.0f * tm;
			if(turnTimer <= 0.0f) {
				
				turnTimer = 0.0f;
				waiting = false;
			}
		}
	}
	
	
	/**
	 * Set turn timer and make the time manager
	 * "wait"
	 * @param playTurn Whether turn counter is updated
	 * @param time Desired time
	 */
	public void setTimer(boolean playTurn, float time) {
		
		turnTimer = time;
		turnLength = time;
		waiting = true;
		if(playTurn)
			++ turn;
	}
	
	
	/**
	 * Set turn timer to the default value and
	 * make the time manager "wait"
	 * @param playTurn Whether turn counter is updated
	 */
	public void setTimer(boolean playTurn) {
		
		setTimer(playTurn, DEFAULT_TURN_LENGTH);
	}
	
	
	/**
	 * Get turn timer in [0,1] scale
	 * @return Timer value
	 */
	public float getTime() {
		
		return turnTimer / turnLength;
	}
	
	
	/**
	 * Is the time manager waiting
	 * i.e. is a turn being played
	 * @return True or false
	 */
	public boolean isWaiting() {
		
		return waiting;
	}
	
	
	/**
	 * Get turn counter value
	 * @return Turn count
	 */
	public int getTurn() {
		
		return turn;
	}
	
}
