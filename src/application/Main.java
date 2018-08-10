
package application;

/**
 * Main class, used only to launch the application
 * @author Jani Nykänen
 *
 */
public final class Main {

	/**
	 * Main function, called on launch
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		
		// Run game
		(new Game()).run(args);
	}

}
