/* ControlDesk.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 * 		
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's Party" to party names.
 * 		
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 * 		
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 * 		
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 * 		
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the ControlDeskView.
 * 		
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 * 		
 * 
 */

/**
 * Class that represents control desk
 *
 */

import java.util.*;

class ControlDesk extends Thread {// Extends threads - wil provide its own thread.run method and used for multithreading

	private final PartyQueue partyQueue = new PartyQueue();
	/** The collection of Lanes */
	private HashSet<Lane> lanes;

	/** The number of lanes represented */
	private int numLanes;
	
	/** The collection of subscribers */
	private ControlDeskObserver cdView;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numLanes	the numbler of lanes to be represented
     *
     */

	public ControlDesk(int numLanes) {
		this.numLanes = numLanes;
		lanes = new HashSet<>(numLanes);
		partyQueue.partyQueue = new Queue();

		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane()); // Adds the lanes to the HashSet
		}
		
		this.start(); // Probably the thread.start implementation

	}
	
	/**
	 * Main loop for ControlDesk's thread
	 * `
	 */
	public void run() { // Custom run function for the main thread
		while (true) { // Infinite loop 
			
			assignLane(); // It keeps assigning lanes to parties if lanes are available 
			
			try {
				sleep(250);
			} catch (Exception e) {}
		}
	}

    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     *
     */

	public void assignLane() {
		Iterator<Lane> it = lanes.iterator();

		while (it.hasNext() && partyQueue.getPartyQueue().hasMoreElements()) {
			Lane curLane = it.next();

			if (!curLane.isPartyAssigned()) {
				System.out.println("ok... assigning this party");
				curLane.assignParty(((Party) partyQueue.getPartyQueue().next()));
			}
		}
		if (cdView != null)
			cdView.receiveControlDeskEvent(new ControlDeskEvent(partyQueue.getPartyQueueDisplay()));
	}

	public void assignLane(LaneEvent le){
		Iterator<Lane> it = lanes.iterator();

		while (it.hasNext()) {
			Lane curLane = it.next();

			if (!curLane.isPartyAssigned()) {
				System.out.println("ok... assigning this party");
				curLane.loadLane(le);
				break;
			}
		}
		if (cdView != null)
			cdView.receiveControlDeskEvent(new ControlDeskEvent(partyQueue.getPartyQueueDisplay()));
	}
    /**
     */

	public void viewScores(Lane ln) {
		// TODO: attach a LaneScoreView object to that lane
	}

    /**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks	A Vector of NickNames
     *
     */

	public void addPartyQueue(Vector partyNicks) {
		partyQueue.addPartyQueue(partyNicks);
		if (cdView != null)
			cdView.receiveControlDeskEvent(new ControlDeskEvent(partyQueue.getPartyQueueDisplay()));
	}

    /**
     * Accessor for the number of lanes represented by the ControlDesk
     * 
     * @return an int containing the number of lanes represented
     *
     */

	// Okay to have this because numLanes is a private variable
	public int getNumLanes() {
		return numLanes;
	}

    /**
     * Allows objects to subscribe as observers
     * 
     * @param cdo the ControlDeskObserver that will be subscribed
     *
     */

	public void addControlView(ControlDeskObserver cdo) {
		if(cdView == null)
			cdView = cdo;
	}

    /**
     * Accessor method for lanes
     * 
     * @return a HashSet of Lanes
     *
     */

	public HashSet<Lane> getLanes() {
		return lanes;
	}
}
