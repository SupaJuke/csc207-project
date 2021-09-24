package entity;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    private String message;
    private String senderUsername; // Username is stored (As apposed to userID) for security(?) reasons.
    private List<String> receiverUsername;
    private int messageID;
    private String recipientTitle;

    /**
     * Constructs a new Message
     * @param message The string or the the actual message that is sent
     * @param senderUsername The username of the message sender
     * @param receiverUsername The usernames of all the receivers of this message
     * @param messageID The unique ID of this message
     */
    public Message(String message, String senderUsername, List<String> receiverUsername, int messageID, String recipientTitle){
        this.message = message;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.messageID = messageID;
        this.recipientTitle = recipientTitle;
    }

    /**
     * Gets the message String
     * @return The message String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the username of the sender of the message
     * @return The senderUsername String
     */
    public String getSenderUsername() {
        return senderUsername;
    }

    /**
     * Gets the list of usernames of the receivers, of this message
     * @return The receiverUsername String
     */
    public List<String> getReceiverUsername() {
        return receiverUsername;
    }

    /**
     * Gets the ID of the message
     * @return The messageID integer
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * Gets the recipient title of the message
     * @return The recipientTitle string
     */
    public String getRecipientTitle() {
        return recipientTitle;
    }

    /**
     * Sets the title of the message
     * @param recipientTitle The new recipient title
     */
    public void setRecipientTitle(String recipientTitle) {
        this.recipientTitle = recipientTitle;
    }
}
