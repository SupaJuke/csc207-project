package entity;

import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Message implements Serializable, Taggable {

    private final String message;
    private final String senderUsername;
    private final List<String> receiverUsername;
    private final int messageID;
    private final String recipientTitle;

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
     * Gets the tags of the message
     * @return The tags of the message
     */
    public SortedMap<String, String> getTags() {
        TreeMap<String, String> tagMap = new TreeMap<>();
        tagMap.put("MID", Integer.toString(getMessageID()));
        tagMap.put("From", getSenderUsername());
        tagMap.put("To", getRecipientTitle());
        tagMap.put("Body", getMessage());

        return tagMap;
    }
}
