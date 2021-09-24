package usecase;
import entity.Message;
import entity.MessagingHistory;
import entity.Taggable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MessageManager implements Savable {

    private final HashMap<Integer, Message> messages;
    private final HashMap<String, MessagingHistory> histories;

    private int nextMID = 0;

    private final MessageNotifier messageEvent = new MessageNotifier();

    /**
     * Constructs an instance of MessageManager with no messages or history.
     */
    public MessageManager(){
        messages = new HashMap<>();
        histories = new HashMap<>();
    }

    /**
     * Constructs an instance of MessageManager with a list of messages, reconstructing the histories.
     */
    public MessageManager(List<Message> inputMessages){
        messages = new HashMap<>();
        histories = new HashMap<>();

        int highestID = 0;

        for (Message m: inputMessages){
            messages.put(m.getMessageID(), m);

            int mID = m.getMessageID();
            if (mID > highestID){
                highestID = mID;
            }

            String senderUN = m.getSenderUsername();

            MessagingHistory senderHistory = histories.get(senderUN);
            boolean isSenderExists = ( histories.get(senderUN) != null );
            if (!isSenderExists){
                senderHistory = new MessagingHistory();
                histories.put(senderUN, senderHistory);
            }
            senderHistory.addMessageIDtoOut(m.getMessageID());

            for (String receiverUN: m.getReceiverUsername()) {

                MessagingHistory receiverHistory = histories.get(receiverUN);
                boolean isReceiverExists = ( histories.get(receiverUN) != null );
                if (!isReceiverExists){
                    receiverHistory = new MessagingHistory();
                    histories.put(receiverUN, receiverHistory);
                }
                receiverHistory.addMessageIDtoIn(m.getMessageID());

            }
        }

        nextMID = highestID + 1;
    }

    /**
     * get the observable MessageNotifier
     * @return the observable MessageNotifier
     */
    public MessageNotifier getMessageEvent(){
        return messageEvent;
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
    public List<Taggable> getMessages(String username){
        MessagingHistory myHistory = histories.get(username);
        if (myHistory == null){
            return new ArrayList<>();
        }

        List<Integer> mIDList = new ArrayList<>();

        mIDList.addAll(myHistory.getInMessages());
        mIDList.addAll(myHistory.getOutMessages());

        Collections.sort(mIDList);

        return new ArrayList<>(getMessageFromID(mIDList));
    }

    /**
     * A getter for the messaging history
     * @param username the name of the user
     * @return returns the MessagingHistory of the user
     */
    private MessagingHistory getHistory(String username){

        boolean isSenderExists = ( histories.get(username) != null );
        if (!isSenderExists){
            MessagingHistory newMH = new MessagingHistory(new ArrayList<>(), new ArrayList<>() );
            histories.put(username, newMH);
        }
        return histories.get(username);
    }

    /**
     * Gets the message sender's username by a message ID
     * @param msgID The message ID
     * @return the username of the message sender if there is one. Otherwise, null.
     */
    public String getMessageSenderFromID(int msgID){
        Message msg = getMessageFromID(msgID);
        if (msg != null)
            return msg.getSenderUsername();
        else
            return null;
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

        messageEvent.notifyNewMessage(message);

        return true;
    }

    /**
     * get the ID of the next message
     * @return ID for the next message
     */
    public int getNextMessageID(){
        return nextMID;
    }
}
