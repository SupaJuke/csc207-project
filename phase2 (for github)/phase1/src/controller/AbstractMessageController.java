package controller;

import entity.Message;
import entity.UserData;
import usecase.MessageManager;
import usecase.UserManager;

import java.lang.reflect.Array;
import java.util.*;

abstract public class AbstractMessageController implements Accessible {

    final ConsoleInputUI ConsoleUI = new ConsoleInputUI();

    private  final ConferenceController conference;
    private final MessageManager MM;
    private final UserManager UM;

    private final UserData UD;

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information
     * @param UD the UserData of the user
     */
    public AbstractMessageController(ConferenceController conference, UserData UD){
        this.conference = conference;
        MM = conference.getMessageManager();
        UM = conference.getUserManager();

        this.UD = UD;
    }


    /**
     * Begins the text UI routine of this controller.
     */
    @Override
    public void accessUI() {
        System.out.println("=== Messenger Menu ===");

        boolean cont = true;
        while (cont) {
            displayMyMessages();

            cont = requestAction();
        }
    }

    /**
     * Display the user's messages.
     */
    public void displayMyMessages(){
        displayMessage(MM.getMessages(UD.getUsername()));
        System.out.println();
    }

    /**
     * Prints possible inputs, and prompts the user to input one.
     */
    abstract public boolean requestAction();


    /**
     * Displays the messages that this user received
     * @param messages the list of messages
     */
    public void displayMessage(List<Message> messages){
        System.out.println("Here are your messages:");

        ArrayList<Message> messagesClone = new ArrayList<>(messages);
        removeDupes(messagesClone);

        for (Message m: messagesClone){
            displayMessage(m);
        }
    }

    /**
     * Displays a message
     * @param message the message that is being displayed
     */
    public void displayMessage(Message message){
        int id = message.getMessageID();
        String sender = message.getSenderUsername();
        String body = message.getMessage();
        String toTitle = message.getRecipientTitle();
        System.out.format( "[MsgID: %d] [From: %s] [To: %s]: %n%s %n", id, sender, toTitle, body);
    }


    /**
     * Sends a message to a recipient
     * @param recipient the user that is receiving the message
     * @param messageBody the message
     * @param toTitle the user that is sending the message
     * @return returns True iff the message was sent successfully.
     */
    public boolean deliverMessage(String recipient, String messageBody, String toTitle){
        ArrayList<String> newList = new ArrayList<>();
        newList.add(recipient);
        return deliverMessage(newList, messageBody, toTitle);
    }

    /**
     * Sends a message to a list of recipients
     * @param recipients the list of users that is receiving the message
     * @param messageBody the message
     * @param toTitle the user that is sending the message
     * @return returns True iff the message was sent successfully.
     */
    public boolean deliverMessage(List<String> recipients, String messageBody, String toTitle){
        Message newMessage = new Message(messageBody, UD.getUsername(), recipients, MM.getNextMessageID(), toTitle);

        boolean sendSuccess = MM.sendMessage(newMessage);

        //saving
        if (sendSuccess)
            conference.getLocalFileGateway().SaveMessageManager(MM);

        return sendSuccess;
    }

    /**
     * @param recipients the usernames of the recipients.
     * Prompts the user to type a message, then sends said message to the given list of people.
     */
    public boolean typeMessage(List<String> recipients, String toTitle){

        System.out.println("Enter the message body:");
        String messageBody = ConsoleUI.getStringInput();

        return deliverMessage(recipients, messageBody, toTitle);
    }

    /**
     * @param recipient the username of the recipient.
     * Prompts the user to type a message, then sends said message to the given person.
     */
    public boolean typeMessage(String recipient){
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(recipient);

        return typeMessage(recipients, recipient);
    }


    /**
     * Removes any duplicate elements in a list
     * @param myList the list that the dupes are being removed from
     * @param <E> the type of elements in this list
     */
    private <E> void removeDupes(List<E> myList){
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
     * Gives a list of valid users that can receive messages
     * @return the list of users that can receive messages
     */
    public List<String> getValidTargets(){
        List<String> attendeesAndSpeakers = new ArrayList<>();

        attendeesAndSpeakers.addAll(conference.getAttendeeManager().getAttendeesUsernames());
        attendeesAndSpeakers.addAll(conference.getSpeakerManager().getSpeakersUsernames());
        return attendeesAndSpeakers;
    }

    /**
     * Lists each user that can receive messages
     */
    public void listValidSendTargets(){
        System.out.println("Choose from a valid target: ");
        for (String username: getValidTargets()){
            System.out.println(username);
        }
    }


    /**
     * Prompts the user to send a message to a person by username.
     * Uses getValidTargets() to get the list of valid targets for the message.
     */
    public void send(){
        listValidSendTargets();

        String targetUsername = ConsoleUI.getStringInput();

        if (!getValidTargets().contains(targetUsername)){
            System.out.println("Invalid target entered.");
            return;
        }

        typeMessage(targetUsername);
    }

    /**
     * Prompts the user to reply to a message.
     */
    public void reply(){
        System.out.print("Please enter the message ID of the message you want to reply to: ");

        Integer targetMsgID = ConsoleUI.getIntInput();
        Message replyTarget = MM.getMessageFromID(targetMsgID);

        if (replyTarget != null) {
            //typeMessage(replyTarget.getSenderUsername());

            System.out.println("Enter the message body:");
            String messageBody = ConsoleUI.getStringInput();
            String toTitle = "Msg "+targetMsgID.toString();

            deliverMessage(replyTarget.getSenderUsername(), messageBody, toTitle);
        }else{
            System.out.println("Invalid message ID entered.");
        }
    }

    /**
     * A getter for the conference
     * @return returns the conference
     */
    public ConferenceController getConference(){
        return conference;
    }


    public UserData getUD(){
        return UD;
    }

}
