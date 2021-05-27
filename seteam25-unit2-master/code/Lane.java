
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 *
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Lane extends Thread implements PinsetterObserver {
	private final ScoreCalculator scoreCalculator = new ScoreCalculator();
	private Party party;
	private Pinsetter setter;
	private HashMap scores;
	private Vector subscribers;

	private boolean gameIsHalted;

	private boolean partyAssigned;
	private boolean gameFinished;
	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;

//	private int[] curScores;
	private int[][] cumulScores;
	private boolean canThrowAgain;

	private int[][] finalScores;
	private int gameNumber;
	private int second_highest;
	private int highest;

	private Bowler currentThrower;			// = the thrower who just took a throw

	/** Lane()
	 *
	 * Constructs a new lane and starts its thread
	 *
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() {
		setter = new Pinsetter();
		scores = new HashMap();
		subscribers = new Vector();

		gameIsHalted = false;
		partyAssigned = false;

		gameNumber = 0;

		setter.subscribe( this );

		this.start();
	}
	public void loadLane(LaneEvent le) {
		party = le.getParty();
		assignParty(party);
		resetBowlerIterator();
		partyAssigned = true;
		bowlIndex = le.getIndex();
		currentThrower = le.getBowler();
		cumulScores = le.getCumulScore();
		scores = le.getScore();
		gameNumber=0;
		ball = le.getBall();
		gameIsHalted = false;
		gameFinished = false;
		frameNumber = le.getFrameNum() -1;

	}

	/** run()
	 *
	 * entry point for execution of this lane
	 */
	public void run() {

		while (true) {
			if (partyAssigned && !gameFinished) {	// we have a party on this lane,
				// so next bower can take a throw

				simulate();
			} 
			else if(frameNumber == 10 && party.getMembers().size() > 1) {
					Vector<Integer> vec = new Vector<Integer>(); 
					for(int i=0;i<party.getMembers().size();++i) {
						// System.out.format("highest_i:%d",i);
						vec.add(cumulScores[i][9]);
					}
					Collections.sort(vec);
					int second_highest_score,highest_score;
					second_highest_score = vec.get(party.getMembers().size()-2);
					highest_score =  vec.get(party.getMembers().size()-1);
					for(int i=0;i<party.getMembers().size();++i) {
						if(second_highest_score == cumulScores[i][9])
							second_highest = i;
						if(highest_score == cumulScores[i][9])
							highest = i;
					}
					System.out.format("highest:%s",((Bowler) party.getMembers().get(highest)).getNickName());
					System.out.format("Second Highest:%s",((Bowler) party.getMembers().get(second_highest)).getNickName());
					NewLane newLane = new NewLane();
					Vector<String> vec2 = new Vector();
					vec2.add(((Bowler) party.getMembers().get(highest)).getNickName());
					vec2.add(((Bowler) party.getMembers().get(second_highest)).getNickName());
					newLane.receiveLaneEvent(vec2);
					newLane.show();
					frameNumber++;
					canThrowAgain = true;
					tenthFrameStrike = false;
					ball = -1;
					int bowlerID = 1;
					int frame_no = 0;
					boolean value = true;
					int[] score_val;
					score_val = new int[2];
					boolean[] prev_strike;
					prev_strike = new boolean[2];
					prev_strike[0] = false;
					prev_strike[1] = false;
					int[] cell_no;
					cell_no = new int[2];
					cell_no[0] = 1;
					cell_no[1] = 0;
					 boolean[] pins;
					 boolean foul;
					 Random rnd;
					 pins = new boolean[10];
					 rnd = new Random();
					for (int i=0; i <= 9; i++) {
						pins[i] = true; // pins[i] == true --> Pins are standing
					}
					while(value && frame_no<3) {
						int count = 0; // Initializing count
						foul = false;
						double skill = rnd.nextDouble(); // skill? Another random variable
						

						Throw new_throw = new Throw();
						int result = new_throw.getResult();
						new_throw.destroy();

						for (int i=0; i <= 9; i++) {
							if (pins[i]) { // If pins[i] is still not knocked down
								double pinluck = rnd.nextDouble();
								if (pinluck <= .04){ 
									foul = true; // Puts foul here... But how is the foul per pin. And if it is a foul shouldn't the rest of the pins not be calculated?
								}
								if ( ((skill + pinluck)/2.0 * 1.2) > .5 ){ // Improve the calc here
									pins[i] = false;  // Some random math to put the pin down
								} 
								if (!pins[i]) {		// this pin just knocked down
									count++; // Increaes the count
								}
							}
						}
						if(count == 10) {
							Emoticon emoticon = new Emoticon(0);
							emoticon = null;	
						}
						if(count == 0) {
							Emoticon emoticon = new Emoticon(1);
							emoticon = null;	
						}
						System.out.format("Count:%d\n",count);
						if(ball == -1) {
							newLane.changeScore(bowlerID,second_highest_score+count,frame_no);
							newLane.changeBall(bowlerID,count,cell_no[1]++);
						}else {
							
							newLane.changeBall(bowlerID,count,cell_no[bowlerID]++);
						}
						score_val[bowlerID] += count;
						if(second_highest_score+count > highest_score && ball == -1) {
							score_val[bowlerID] = second_highest_score;
							score_val[bowlerID] += count;

							if(bowlerID == 1)
								bowlerID = 0;
							score_val[bowlerID] = highest_score;
							ball = 0;

						}else if(ball == -1)
							value = false;
						else {
							ball++;
							if(ball == 2)
							{
								for (int i=0; i <= 9; i++) {
									pins[i] = true; // pins[i] == true --> Pins are standing
								}
								newLane.changeScore(bowlerID,score_val[bowlerID],frame_no);
								ball = 0;
								if(bowlerID == 1)
									frame_no++;
								if(bowlerID == 0)
									bowlerID = 1;
								else
									bowlerID = 0;
							}
						}
						
						try {
							sleep(100);
						} catch (Exception e) {}
					}
					if(ball != -1) {
						cumulScores[highest][9] = score_val[0];
						cumulScores[second_highest][9] = score_val[1];
					}


				}
			else if(partyAssigned && gameFinished) {
				endGame();
			}

			try {
				sleep(10);
			} catch (Exception e) {}
		}
	}


	private void endGame() {
		EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
		int result = egp.getResult();
		egp.distroy();
		egp = null;


		System.out.println("result was: " + result);

		// TODO: send record of scores to control desk
		if (result == 1) {					// yes, want to play again
			resetScores();
			resetBowlerIterator();

		} else if (result == 2) {// no, dont want to play another game
			Vector printVector;
			EndGameReport egr = new EndGameReport( ((Bowler)party.getMembers().get(0)).getNickName() + "'s Party", party);
			printVector = egr.getResult();
			partyAssigned = false;
			Iterator scoreIt = party.getMembers().iterator();
			party = null;
			partyAssigned = false;

			publish(lanePublish());

			int myIndex = 0;
			while (scoreIt.hasNext()){
				Bowler thisBowler = (Bowler)scoreIt.next();
				ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
				sr.sendEmail(thisBowler.getEmail());
				Iterator printIt = printVector.iterator();
				while (printIt.hasNext()){
					if (thisBowler.getNick() == (String)printIt.next()){
						System.out.println("Printing " + thisBowler.getNick());
						sr.sendPrintout();
					}
				}

			}
		}
	}

	private void simulate() {
		while (gameIsHalted) {
			try {
				sleep(10);
			} catch (Exception e) {}
		}

		simulateThrow();
	}

	private void simulateThrow() {
		if (bowlerIterator.hasNext()) {
			currentThrower = (Bowler)bowlerIterator.next();

			canThrowAgain = true;
			tenthFrameStrike = false;
			ball = 0;
			while (canThrowAgain) {
				Throw new_throw = new Throw();
				int result = new_throw.getResult();
				new_throw.destroy();
				setter.ballThrown();		// simulate the thrower's ball hiting
				ball++;
			}

			if (frameNumber == 9){
				finalScores[bowlIndex][gameNumber] = cumulScores[bowlIndex][9];
				try{
					Date date = new Date();
					String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
					ScoreHistoryFile.addScore(currentThrower.getNick(), dateString, new Integer(cumulScores[bowlIndex][9]).toString());
				} catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
			}


			setter.reset();
			bowlIndex++;

		} else {
			frameNumber++;
			resetBowlerIterator();
			bowlIndex = 0;
			if (frameNumber > 9) {
				gameFinished = true;
				gameNumber++;
			}
		}
	}

	/** recievePinsetterEvent()
	 *
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 *
	 * @param pe 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {

		if (pe.pinsDownOnThisThrow() >=  0) {			// this is a real throw
//			ThisIsARealThrow(pe);
			markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());

			// next logic handles the ?: what conditions dont allow them another throw?
			// handle the case of 10th frame first
			if (frameNumber == 9) {
				rpe_2(pe);
			} else { // its not the 10th frame

				if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
					canThrowAgain = false;
					//publish( lanePublish() );
				} else if (pe.getThrowNumber() == 2) {
					canThrowAgain = false;
					//publish( lanePublish() );
				} else if (pe.getThrowNumber() == 3)
					System.out.println("I'm here...");
			}
		}
	}



	private void rpe_2(PinsetterEvent pe) {
		if (pe.totalPinsDown() == 10) {
			setter.resetPins();
			if(pe.getThrowNumber() == 1) {
				tenthFrameStrike = true;
			}
		}

		if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && !tenthFrameStrike)) {
			canThrowAgain = false;
			//publish( lanePublish() );
		}

		if (pe.getThrowNumber() == 3) {
			canThrowAgain = false;
			//publish( lanePublish() );
		}
	}

	/** resetBowlerIterator()
	 *
	 * sets the current bower iterator back to the first bowler
	 *
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	private void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}

	/** resetScores()
	 *
	 * resets the scoring mechanism, must be called before scoring starts
	 *
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	private void resetScores() {
		Iterator bowlIt = (party.getMembers()).iterator();

		while ( bowlIt.hasNext() ) {
			int[] toPut = new int[25];
			for ( int i = 0; i != 25; i++){
				toPut[i] = -1;
			}
			scores.put( bowlIt.next(), toPut );
		}



		gameFinished = false;
		frameNumber = 0;
	}

	/** assignParty()
	 *
	 * assigns a party to this lane
	 *
	 * @pre none
	 * @post the party has been assigned to the lane
	 *
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;

//		curScores = new int[party.getMembers().size()];
		cumulScores = new int[party.getMembers().size()][10];
		finalScores = new int[party.getMembers().size()][128]; //Hardcoding a max of 128 games, bite me.
		gameNumber = 0;

		resetScores();
	}

	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 *
	 * @param Cur		The current bowler
	 * @param frame	The frame that bowler is on
	 * @param ball		The ball the bowler is on
	 * @param score	The bowler's score 
	 */
	private void markScore( Bowler Cur, int frame, int ball, int score ){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + ball);

		curScore = (int[]) scores.get(Cur);


		curScore[ index - 1] = score;
		scores.put(Cur, curScore);
		cumulScores[bowlIndex] = scoreCalculator.getScore(frame, curScore, bowlIndex, ball);
		publish( lanePublish() );
	}

	/** lanePublish()
	 *
	 * Method that creates and returns a newly created laneEvent
	 *
	 * @return		The new lane event
	 */
	private LaneEvent lanePublish(  ) {
		return new LaneEvent(party, bowlIndex, currentThrower, cumulScores, scores, frameNumber+1, (int[])scores.get(currentThrower),  ball, gameIsHalted);
	}

	/** isPartyAssigned()
	 *
	 * checks if a party is assigned to this lane
	 *
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/** subscribe
	 *
	 * Method that will add a subscriber
	 *
//	 * @param subscribe	Observer that is to be added
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** publish
	 *
	 * Method that publishes an event to subscribers
	 *
	 * @param event	Event that is to be published
	 */

	public void publish( LaneEvent event ) {
		if( subscribers.size() > 0 ) {
			Iterator eventIterator = subscribers.iterator();

			while ( eventIterator.hasNext() ) {
				( (LaneObserver) eventIterator.next()).receiveLaneEvent( event );
			}
		}
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 *
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		publish(lanePublish());
	}

	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		publish(lanePublish());
	}

	public void saveGame(){
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String formattedDate = myDateObj.format(myFormatObj);
		Bowler b = (Bowler) party.getMembers().get(0);
		String saveTitle = b.getNick() + "'s party" + formattedDate;
		SaveFile.addSaveState(lanePublish());
	}
}
