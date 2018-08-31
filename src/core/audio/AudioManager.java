package core.audio;


/**
 * A general audio manager that handles interaction
 * with audio components
 * @author Jani Nykänen
 *
 */
public class AudioManager {

	/** Music volume, from 0 to 10 */
	private int musicVol;
	/** Sound volume, from 0 to 10 */
	private int soundVol;
	
	
	/**
	 * Constructor
	 */
	public AudioManager() {
		
		musicVol = 10;
		soundVol = 10;
	}
	
	
	/**
	 * Set music volume
	 * @param vol New volume
	 */
	public void setMusicVolume(int vol) {
		
		if(vol < 0) vol = 0;
		if(vol > 10) vol = 10;
		
		musicVol = vol;
	}
	
	
	/**
	 * Set sound volume
	 * @param vol Sound volume
	 */
	public void setSoundVolume(int vol) {
		
		if(vol < 0) vol = 0;
		if(vol > 10) vol = 10;
		
		soundVol = vol;
	}
	
	
	
	/**
	 * Get music volume
	 * @return Volume
	 */
	public int getMusicVolume() {
		
		return musicVol;
	}
	
	
	/**
	 * Get sound effect volume
	 * @return Volume
	 */
	public int getSoundVolume() {
		
		return soundVol;
	}

}
