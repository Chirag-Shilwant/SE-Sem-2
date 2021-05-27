import java.util.Vector;

public class PartyQueue {

    Queue partyQueue;

    public Queue getPartyQueue() {
        return partyQueue;
    }

    public PartyQueue() {
    }

    /**
     * Creates a party from a Vector of nickNames and adds them to the wait queue.
     *
     * @param partyNicks A Vector of NickNames
     */

    public void addPartyQueue(Vector partyNicks) {
        Vector<Bowler> partyBowlers = new Vector<>();
        for (Object partyNick : partyNicks) {
            Bowler newBowler = BowlerFile.registerPatron(((String) partyNick));
            partyBowlers.add(newBowler);
        }
        Party newParty = new Party(partyBowlers);
        partyQueue.add(newParty);

    }

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
     *
     * @return a Vector of Strings
     */

    public Vector<String> getPartyQueueDisplay() {
        Vector<String> displayPartyQueue = new Vector<>();
        for (int i = 0; i < (partyQueue.asVector()).size(); i++) {
            String nextParty =
                    ((Bowler) (((Party) partyQueue.asVector().get(i)).getMembers())
                            .get(0))
                            .getNickName() + "'s Party";
            displayPartyQueue.addElement(nextParty);
        }
        return displayPartyQueue;
    }

}
