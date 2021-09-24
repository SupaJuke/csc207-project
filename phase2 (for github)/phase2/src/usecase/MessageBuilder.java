package usecase;

import entity.Message;

import java.util.List;

public class MessageBuilder {
    /**
     * @param message Message body
     * @param fromUN Username of sender
     * @param toUNs List of recipients' username
     * @param msgID ID of the message
     * @param toTitle a name for all the recipients
     * @return the Message constructed from the parameters
     */
    public Message buildMessage(String message, String fromUN, List<String> toUNs, int msgID, String toTitle){
        return new Message(message, fromUN, toUNs, msgID, toTitle);
    }
}
