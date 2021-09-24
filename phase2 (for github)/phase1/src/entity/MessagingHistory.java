package entity;

import java.io.Serializable;
import java.util.List;

public class MessagingHistory implements Serializable {

    private List<Integer> inMessages; // This is a log of all incoming/readable Messages
    private List<Integer> outMessages; // Same as before but outgoing/sent Messages

    /**
     * Constructs a Messaging History by ID of a particular person in the event system
     * @param inMessages List of Message IDs that are incoming or are meant to be read
     * @param outMessages List of Messages IDs that are outgoing or are already sent
     */
    public MessagingHistory(List<Integer> inMessages, List<Integer> outMessages){
        this.inMessages = inMessages;
        this.outMessages = outMessages;
    }

    /**
     * Adds a Message ID to the incoming/readable list of Messages ID's
     * @param messageID The Message ID to be added
     * @return True iff the Message ID was added successfully
     */
    public boolean addMessageIDtoIn(int messageID){
        if (!this.searchForMessageID(messageID, this.inMessages)){ // Searches for the ID to already be here
            return this.inMessages.add(messageID); // Autoboxing to Integer here btw
        }
        return false;
    }

    /**
     * Adds a Message ID to the outgoing/sent list of Messages ID's
     * @param messageID The Message ID to be added
     * @return True iff the Message ID was added successfully
     */
    public boolean addMessageIDtoOut(int messageID){
        if (!this.searchForMessageID(messageID, this.inMessages)) {
            return this.outMessages.add(messageID);
        }
        return false;
    }

    /**
     * Removes a Message ID from the incoming/readable list of Messages ID's
     * @param messageID The Message ID to be removed
     * @return True iff the Message ID was removed successfully
     */
    public boolean removeMessageIDfromIn(int messageID){
        return this.inMessages.remove((Integer) messageID);
    }

    /**
     * Removes a Message ID from the outgoing/sent list of Messages ID's
     * @param messageID The Message ID to be removed
     * @return True iff the Message ID was removed successfully
     */
    public boolean removeMessageIDfromOut(int messageID){
        return this.outMessages.remove((Integer) messageID);
    }

    /**
     * Returns the list of Message ID's for outgoing/sent Messages
     * @return The outgoing/sent list of Messages ID's
     */
    public List<Integer> getOutMessages() {
        return outMessages;
    }

    /**
     * Returns the list of Message ID's for incoming/readable Messages
     * @return The incoming/readable list of Messages ID's
     */
    public List<Integer> getInMessages() {
        return inMessages;
    }

    /**
     * Verifies if an element is an list or not
     * @param messageID The Message ID to be searched
     * @param messageIDList The list of Message ID's to be searched from
     * @return True iff the item was found
     */
    private boolean searchForMessageID(int messageID, List<Integer> messageIDList){
        for (Integer integer : messageIDList) {
            if (messageID == integer) {
                return true;
            }
        }
        return false;
    }

}
