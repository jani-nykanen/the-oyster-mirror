package core.utility;

public class CSVParser {

	/**
	 * Constructor
	 */
	public CSVParser() { /* ... */}
	
	
	/**
	 * Convert CSV string to an array of integers
	 * @param data Śtring data
	 * @return Integer array
	 */
	public int[] parseInt(String data) {
		
		// Remove whitespaces
		data = data.replaceAll("\\s+","");
		
		// Split to an array of string
		String[] dataArr = data.split(",");
		// Convert to integers and put to an array
		int[] ret = new int[dataArr.length];
		for(int i = 0; i < dataArr.length; ++ i) {
			
			ret[i] = Integer.parseInt(dataArr[i]);
		}
		return ret;
	}
}
