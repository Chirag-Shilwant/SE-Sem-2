import java.util.Vector;
import java.io.*;

public class drive {

	public static void main(String[] args) {

		int numLanes = 3; // Seems to initialize the number of lanes which is passed to the Alley Object
		int maxPatronsPerParty = 5; // Seems to initialize the maximum number of patrons per game

		Alley a = new Alley(numLanes); // Creates an instance of the Alley class with the parameter numLanes
		ControlDesk controlDesk = a.getControlDesk(); // Control Desk - ? 

		ControlDeskView cdv = new ControlDeskView( controlDesk, maxPatronsPerParty);
		controlDesk.addControlView( cdv );

	}
}
