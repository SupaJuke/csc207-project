package usecase;
import entity.Message;
import entity.MessagingHistory;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MessageManager implements Serializable {

    private final HashMap<Integer, Message> messages;
    private final HashMap<String, MessagingHistory> histories;

    private int nextMID = 0;

    /**
     * Constructs an instance of MessageManager with no messages or history.
     */
    public MessageManager(){
        messages = new HashMap<>();
        histories = new HashMap<>();
    }



    /**
     * Find Messages by ID.
     * @param mIDList The IDs of the Messages you are looking for.
     * @return the Message found.
     */
    public List<Message> getMessageFromID(List<Integer> mIDList){
        ArrayList<Message> gotMessages = new ArrayList<>();

        for (Integer mID: mIDList){
            gotMessages.add(messages.get(mID));
        }
        return gotMessages;
    }

    /**
     * Find a Message by ID.
     * @param messageID The ID of the Message you are looking for.
     * @return the Message found.
     */
    public Message getMessageFromID(Integer messageID){
        return messages.get(messageID);
    }



    /**
     * Get the messages that have been sent to or by the user, sorting them in ascending order by ID.
     * @param username The username of the user whose outbound messages will be returned.
     * @return the ArrayList of Messages found.
     */
    public List<Message> getMessages(String username){
        MessagingHistory myHistory = histories.get(username);
        if (myHistory == null){
            return new ArrayList<>();
        }

        List<Integer> mIDList = new ArrayList<>();

        mIDList.addAll(myHistory.getInMessages());
        mIDList.addAll(myHistory.getOutMessages());

        Collections.sort(mIDList);

        return getMessageFromID(mIDList);
    }

    /**
     * Get the messages that have been sent by the user.
     * @param username The username of the user whose outbound messages will be returned.
     * @return the ArrayList of Messages found.
     */
    public List<Message> getOutMessages(String username){
        MessagingHistory myHistory = histories.get(username);
        if (myHistory == null){
            return new ArrayList<>();
        }
        List<Integer> mIDList = myHistory.getOutMessages();

        return getMessageFromID(mIDList);
    }

    /**
     * Get the messages that have been sent to the user.
     * @param username The username of the user whose incoming messages will be returned.
     * @return the ArrayList of Messages found.
     */
    public List<Message> getInMessages(String username){
        MessagingHistory myHistory = histories.get(username);
        if (myHistory == null){
            return new ArrayList<>();
        }
        List<Integer> mIDList = myHistory.getInMessages();

        return getMessageFromID(mIDList);
    }



    private MessagingHistory getHistory(String username){

        boolean isSenderExists = ( histories.get(username) != null );
        if (!isSenderExists){
            MessagingHistory newMH = new MessagingHistory(new ArrayList<>(), new ArrayList<>() );
            histories.put(username, newMH);
        }
        return histories.get(username);
    }

    /**
     * Send a Message.
     * @param message The Message to be sent.
     * @return True iff the message was sent successfully.
     */
    public boolean sendMessage(Message message){

        if (message.getMessageID() >= nextMID){
            nextMID = message.getMessageID() + 1;
        }else{
            return false;
        }
        messages.put(message.getMessageID(), message);

        String senderName = message.getSenderUsername();
        MessagingHistory senderMH = getHistory(senderName);
        senderMH.addMessageIDtoOut( message.getMessageID() );

        for (String receiverName: message.getReceiverUsername()){
            MessagingHistory receiverMH = getHistory(receiverName);
            receiverMH.addMessageIDtoIn( message.getMessageID() );
        }

        return true;
    }

    public int getNextMessageID(){
        return nextMID;
    }
}
