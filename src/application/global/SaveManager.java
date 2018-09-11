package application.global;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import application.stagemenu.StageMenu;
import core.utility.CSVParser;

/**
 * Handles saved game data
 * @author Jani Nykänen
 *
 */
public class SaveManager {

	/** Completion data 
	 * 0 = none
	 * 1 = gray
	 * 2 = gold */
	private int[] completion = new int[StageMenu.BUTTON_COUNT -1];
	
	/** The "current" stage i.e the latest unlocked stage */
	private int currentStage = 1;
	
	/** Ending state (see StageMenu) */
	private int endingState = 0;
	
	
	/**
	 * Write CSV data to a file
	 * @param data Data
	 * @param writer Writer
	 * @throws Exception If something goes wrong
	 */
	static private void writeCSVData(int[] data, Writer writer) throws Exception {
		
		// Write data
		for(int i = 0; i < data.length; ++ i) {
			
			writer.write(Integer.toString(data[i]));
			if(i != data.length -1)
				writer.write(",");
		}
		// Line swap
		writer.write("\n");
	}
	
	
	/**
	 * Constructor
	 */
	public SaveManager() {
		
		reset();
	}
	
	
	/**
	 * Save the game
	 * @param filename Filename
	 * @throws Exception If fails to close the writer
	 */
	public void saveGame(String filename)  throws Exception {
		
		Writer writer = null;
		try {
			
			// Open/create file for writing
			writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(filename), "utf-8"));
			
			// Write completion data
			writeCSVData(completion, writer);
			// Write latest unlocked stage
			writer.write(Integer.toString(currentStage) + "\n");
			// Write ending state
			writer.write(Integer.toString(endingState) + "\n");
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		finally {
			
			writer.close();
		}
	}
	
	
	/**
	 * Load game
	 * @param filename Filename
	 * @throws Exception If file does not exist etc.
	 */
	public void loadGame(String filename) throws Exception {
		
		BufferedReader reader = null;
		try {
			
			reader = new BufferedReader(new FileReader(filename));
			// Read completion data
			completion = (new CSVParser()).parseInt(reader.readLine());
			// Read latest stage data
			currentStage = Integer.parseInt(reader.readLine());
			// Read the ending state
			String line = reader.readLine();
			if(line != null)
				endingState = Integer.parseInt(line);
			
		}
		catch(FileNotFoundException e) {
			
			System.out.println("Could not find " + filename 
					+ ", maybe because it does not exist yet?");
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		finally {
			
			reader.close();
		}
	}
	
	
	
	/**
	 * Reset data
	 */
	public void reset() {
		
		for(int i = 0; i < completion.length; ++ i)
			completion[i] = 0;
		
		currentStage = 1;
	}
	
	
	/**
	 * Update completion data
	 * @param index Stage index
	 * @param value New value
	 * @return True, if something changed (otherwise false)
	 */
	public boolean updateCompletionData(int index, int value) {
		
		int v = completion[index -1];
		if(v < value) {
			
			completion[index-1] = value;
			return true;
		}
		
		return false;
	}

	
	/**
	 * Get completion info of a single stage
	 * @param index Stage index
	 * @return Stage info
	 */
	public int getCompletionInfo(int index) {
		
		-- index;
		if(index < 0 || index >= completion.length)
			return -1;
		
		return completion[index];
	}
	
	
	/**
	 * Get the index of the latest unlocked stage
	 * @return Index
	 */
	public int getCurrentStageIndex() {
		
		return currentStage;
	}
	
	
	/**
	 * Increase the current stage index
	 */
	public void increaseStageIndex() {
		
		++ currentStage;
	}
	
	
	/**
	 * Get the ending state
	 * @return Ending state
	 */
	public int getEndingState() {
		
		return endingState;
	}


	/**
	 * Set the ending state
	 * @param value Value
	 */
	public void setEndingState(int value) {
		
		endingState = value;
	}
	
	
	/**
	 * Get the amount of golden stars
	 * @return The amount of golden stars
	 */
	public int getGoldenStarCount() {
		
		int c = 0;
		for(int compl : completion) {
			
			c += compl == 2 ? 1 : 0;
		}
		
		return c;
	}
}
