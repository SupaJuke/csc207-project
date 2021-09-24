package controller;

import entity.SpeakerData;

import java.util.ArrayList;
import java.util.List;

public class SpeakerMessageController extends AbstractMessageController {

    private SpeakerData SD;

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information.
     * @param SD the SpeakerData of the user.
     */
    public SpeakerMessageController(ConferenceController conference, SpeakerData SD){
        super(conference, SD);
        this.SD = SD;
    }

    /**
     * Prints possible inputs, and prompts the user to input one.
     */
    @Override
    public boolean requestAction(){
        System.out.println("=== Speaker Message Menu ===");
        System.out.println("[1] - Send a message to users at an event");
        System.out.println("[2] - Reply to a message.");
        System.out.println("[3] - Exit");

        String input = super.ConsoleUI.getStringInput();

        if (input.equals("1")) {
            sendToEvents();
        } else if (input.equals("2")) {
            reply();
        } else if (input.equals("3")) {
            return false;
        }
        
        return true;
    }

    /**
     * UI for the user to send a message to all attendees in one or more events.
     */
    public void sendToEvents() {

        ArrayList<String> targetUsers = new ArrayList<>();
        StringBuilder toTitle = new StringBuilder();

        while (true){
            List<Integer> speakingEventIDs = SD.getSpeakingAt();
            System.out.println("You speak at the following events:");
            for (int eventID:speakingEventIDs) {
                System.out.println(eventID);
            }

            System.out.println("Enter the ID of an event you would like to send the message to, or enter \"Stop\" to type the message:");

            String lastInput = super.ConsoleUI.getStringInput();

            if (lastInput.equals("Stop"))
                break;

            try{
                int targetEventID = Integer.parseInt(lastInput);

                if (!speakingEventIDs.contains(targetEventID)) {
                    System.out.println("You have entered an invalid event ID.");
                    continue;
                }
                targetUsers.addAll(super.getConference().getEventBrowser().getUsersFromEvent(targetEventID));
                if (!toTitle.toString().equals("")){
                    toTitle.append(", ");
                }
                toTitle.append(targetEventID);
            }catch (NumberFormatException e){
                System.out.println("Invalid input detected. Please try again.");
            }
        }

        typeMessage(targetUsers, "Events "+toTitle);
    }
}
