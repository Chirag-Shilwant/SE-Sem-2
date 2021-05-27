/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */

/**
 * Class for representation of the control desk
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

// An interface is an abstract "class" that is used to group related methods with "empty" bodies

// To access the interface methods, the interface must be "implemented" (kinda like inherited) by 
// another class with the implements keyword (instead of extends). The body of the interface method
//  is provided by the "implement" class:

public class ControlDeskView implements ActionListener, ControlDeskObserver {

	// Class for the Control Desk View
	private JButton addParty, finished, loadGame, search;
	private JFrame win;
	private JList partyList;

	/** The maximum  number of members in a party */
	private int maxMembers;

	private ControlDesk controlDesk;

	public void setLoadedLE(LaneEvent loadedLE) {
		controlDesk.assignLane(loadedLE);
	}

	/**
	 * Displays a GUI representation of the ControlDesk
	 *
	 */

	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.getNumLanes();

		win = new JFrame("Control Desk"); // Create a new JFrame 
		win.getContentPane().setLayout(new BorderLayout()); // Sets a borederlayout
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Controls Panel
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new GridLayout(3, 1));
		controlsPanel.setBorder(new TitledBorder("Controls"));

		addParty = new JButton("Add Party");
		JPanel addPartyPanel = new JPanel();
		addPartyPanel.setLayout(new FlowLayout());
		addParty.addActionListener(this);
		addPartyPanel.add(addParty);
		controlsPanel.add(addPartyPanel);

		search = new JButton("Search");
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		search.addActionListener(this);
		searchPanel.add(search);
		controlsPanel.add(searchPanel);

		loadGame = new JButton("Load Game");
		JPanel assignPanel = new JPanel();
		assignPanel.setLayout(new FlowLayout());
		loadGame.addActionListener(this);
		assignPanel.add(loadGame);
		controlsPanel.add(assignPanel);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);
		controlsPanel.add(finishedPanel);

		// Lane Status Panel
		JPanel laneStatusPanel = new JPanel();
		laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
		laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

		HashSet lanes=controlDesk.getLanes(); // Gets the lanes from the controldesk getter
		Iterator it = lanes.iterator(); // Creates an iterator for the lanes
		int laneCount=0;
		while (it.hasNext()) { // Iterate through the lanes HashSet
			Lane curLane = (Lane) it.next();
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1)); // LaneStatusView class
			curLane.subscribe(laneStat); // Subscribes the lanestat to the current lane
			((Pinsetter)curLane.getPinsetter()).subscribe(laneStat); // Gets the pinsetter object to subscribe to the lane status object
			JPanel lanePanel = laneStat.showLane(); // Shows ths lane status
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		// Party Queue Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Queue"));

		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(10);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);
		//		partyPanel.add(partyList);

		// Clean up main panel
		colPanel.add(controlsPanel, "East");
		colPanel.add(laneStatusPanel, "Center");
		colPanel.add(partyPanel, "West");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show(); // show() is depracated

	}

	/**
	 * Handler for actionEvents
	 *
	 * @param e	the ActionEvent that triggered the handler
	 *
	 */

	public void actionPerformed(ActionEvent e) { //ActionEvent ?  triggered
		if (e.getSource().equals(addParty)) {
			AddPartyView addPartyWin = new AddPartyView(this, maxMembers); // The view for the AddParty class
		}

		else if (e.getSource().equals(finished)) {
			win.hide(); // hide() is depracated
			System.exit(0);
		}
		else if (e.getSource().equals(loadGame)){
			System.out.println("Loading...");
			LoadSavedView ls = new LoadSavedView(this);
		}
		if (e.getSource().equals(search)) {
			SearchView searchView = new SearchView();
		}
	}


	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(AddPartyView addPartyView) {
		controlDesk.addPartyQueue(addPartyView.getParty());
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param ce	the ControlDeskEvent that triggered the handler
	 *
	 */

	public void receiveControlDeskEvent(ControlDeskEvent ce) {
		partyList.setListData(((Vector) ce.getPartyQueue()));
	}
}
