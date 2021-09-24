package presenter.messages;

import controller.ConferenceController;
import entity.Taggable;
import usecase.MessageBuilder;
import usecase.MessageManager;
import usecase.MessageNotifier;
import usecase.UserManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class AbstractMessagePresenter {

    private final ConferenceController conference;
    private final MessageManager MM;
    private final UserManager UM;

    private final String username;

    private final MessageBuilder msgBuilder = new MessageBuilder();

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information
     * @param username the username of the user
     */
    public AbstractMessagePresenter(ConferenceController conference, String username){
        this.conference = conference;
        MM = conference.getMessageManager();
        UM = conference.getUserManager();

        this.username = username;
    }



    //Non-text stuff below


    /**
     * Create a list of MessageGroups from a list of usernames
     * One MessageGroup per username
     * @param usernames the usernames to make MessageGroups out of
     * @return list of usernames as MessageGroups
     */
    public List<Group> createGroupList(List<String> usernames){
        List<Group> usernameGroups = new ArrayList<>();

        for(String f: usernames){
            MessageGroup MG = new MessageGroup(f);
            usernameGroups.add(MG);
        }
        return usernameGroups;
    }

    /**
     * Get my messages, removing any duplicate messages
     * @return List of messages as Taggables
     */
    public List<Taggable> getMyMessages(){
        List<Taggable> myMessages = new ArrayList<>(MM.getMessages(username));
        removeDupes(myMessages);
        return myMessages;
    }

    /**
     * Get a list of MessageGroups of the user's friends
     * @return the list of MessageGroups of friends
     */
    public List<Group> getFriendGroups(){
        List<String> myFriends = new ArrayList<>(conference.getUserManager()
                .getUserByID(getUsername()).getFriendsList());
        removeDupes(myFriends);

        return createGroupList(myFriends);
    }


    /**
     * Get a Map linking each MessageGroup to its name
     * Each MessageGroup is a valid group the user can message
     * @return a Map linking each MessageGroup to its name
     */
    abstract public Map<String, List<Group>> getValidTargetGroups();


    /**
     * Sends a message to a list of recipients; all other deliverMessage(...) call this one
     * @param recipients the list of users that is receiving the message
     * @param messageBody the message
     * @param toTitle the user that is sending the message
     * @return returns True iff the message was sent successfully.
     */
    public boolean deliverMessage(List<String> recipients, String messageBody, String toTitle){
        int messageID = MM.getNextMessageID();
        boolean sendSuccess = MM.sendMessage(msgBuilder.buildMessage(messageBody, username, recipients,
                messageID, toTitle));

        //saving
        if (sendSuccess){
            //conference.getLocalFileGateway().saveSavable(MM, "MessageManager");
            try {
                conference.getLocalFileGateway().addMessageToDB(messageID, username, recipients, messageBody, toTitle);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }

        return sendSuccess;
    }


    /**
     * Removes any duplicate elements in a list
     * @param myList the list that the dupes are being removed from
     * @param <E> the type of elements in this list
     */
    public <E> void removeDupes(List<E> myList){
        ArrayList<E> tempList = new ArrayList<>();
        for (E item: myList){
            if (!tempList.contains(item)){
                tempList.add(item);
            }
        }
        myList.clear();
        myList.addAll(tempList);
    }



    /**
     * get the MM's MessageNotifier observable
     * @return the MM's MessageNotifier observable
     */
    public MessageNotifier getMessageNotifier(){
        return MM.getMessageEvent();
    }

    /**
     * A getter for the conference
     * @return returns the conference
     */
    public ConferenceController getConference(){
        return conference;
    }

    /**
     * A getter for the username of the user logged into the AMC
     * @return returns the username
     */
    public String getUsername() {
        return username;
    }


    public String getUsertype(){
        return UM.getUserByID(username).getUserType();
    }



}
