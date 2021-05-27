/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.util.*;
import java.io.*;

public class ScoreHistoryFile {

	private static String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

	public static void addScore(String nick, String date, String score)
		throws IOException, FileNotFoundException {

		String data = nick + "\t" + date + "\t" + score + "\n";

		RandomAccessFile out = new RandomAccessFile(SCOREHISTORY_DAT, "rw"); // Random Access the file
		out.skipBytes((int) out.length()); // Go to the end of the file
		out.writeBytes(data); // Write the data
		out.close(); // Close the file
	}

	public static Vector getScores(String nick)
		throws IOException, FileNotFoundException { // Get the score of the nick
		Vector scores = new Vector(); // It is a vector because the scorehistory can be of multiple games, it has to find out all of them and add them

		BufferedReader in =
			new BufferedReader(new FileReader(SCOREHISTORY_DAT)); // Buffered reading
		String data;
		while ((data = in.readLine()) != null) { // Until you reach the end
			// File format is nick\tfname\te-mail
			String[] scoredata = data.split("\t"); // Split the data by tabs
			//"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
			if (nick.equals(scoredata[0])) { // If the data is of the person in context
				scores.add(new Score(scoredata[0], scoredata[1], scoredata[2])); // Return the score
			}
		}
		return scores; // Return the scores,
	}

}
