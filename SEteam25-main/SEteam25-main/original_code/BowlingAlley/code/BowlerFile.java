/* BowlerFile.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: BowlerFile.java,v $
 * 		Revision 1.5  2003/02/02 17:36:45  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for interfacing with Bowler database
 *
 */

import java.util.*;
import java.io.*;

class BowlerFile {

	/** The location of the bowelr database */
	private static String BOWLER_DAT = "BOWLERS.DAT"; // The location of the bowler database

    /**
     * Retrieves bowler information from the database and returns a Bowler objects with populated fields.
     *
     * @param nickName	the nickName of the bolwer to retrieve
     *
     * @return a Bowler object
     * 
     */

	public static Bowler getBowlerInfo(String nickName)
		throws IOException, FileNotFoundException { // Reads the data from the file and checks with the corresponding nickNames

		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT)); // BufferedReader reads file
		String data;
		while ((data = in.readLine()) != null) { // Iterates through all the data files
			// File format is nick\tfname\te-mail
			String[] bowler = data.split("\t"); // Splits the data by tab
			if (nickName.equals(bowler[0])) { // If the bowler nickname is in the database
				System.out.println(
					"Nick: "
						+ bowler[0]
						+ " Full: "
						+ bowler[1]
						+ " email: "
						+ bowler[2]);
				return (new Bowler(bowler[0], bowler[1], bowler[2])); // Returns the bowler
			}
		}
		System.out.println("Nick not found..."); // Returns null + error
		return null;
	}

    /**
     * Stores a Bowler in the database
     *
     * @param nickName	the NickName of the Bowler
     * @param fullName	the FullName of the Bowler
     * @param email	the E-mail Address of the Bowler
     *
     */

	public static void putBowlerInfo(
		String nickName,
		String fullName,
		String email)
		throws IOException, FileNotFoundException {

		String data = nickName + "\t" + fullName + "\t" + email + "\n";

		RandomAccessFile out = new RandomAccessFile(BOWLER_DAT, "rw"); // Random Accessing is called here
		out.skipBytes((int) out.length()); // Goes to the end of the file
		out.writeBytes(data); // Writes the data into the file there
		out.close(); // Closes the files.
	}

	public static Bowler registerPatron(String nickName) {
		Bowler patron = null;

		try {
			// only one patron / nick.... no dupes, no checks

			patron = getBowlerInfo(nickName);

		} catch (Exception e) {
			System.err.println("Error..." + e);
		}

		return patron;
	}
    /**
     * Retrieves a list of nicknames in the bowler database
     *
     * @return a Vector of Strings
     * 
     */

	public static Vector getBowlers()
		throws IOException, FileNotFoundException {

		Vector allBowlers = new Vector(); // Vector of all the bowlers

		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT)); // Buffered Reading of the Bowlers
		String data;
		while ((data = in.readLine()) != null) { // Reads lines
			// File format is nick\tfname\te-mail
			String[] bowler = data.split("\t");  // Split by tabs
			//"Nick: bowler[0] Full: bowler[1] email: bowler[2]
			allBowlers.add(bowler[0]); // Add nickname to the allbowler
		}
		return allBowlers; // Returns the allbowler
	}

}