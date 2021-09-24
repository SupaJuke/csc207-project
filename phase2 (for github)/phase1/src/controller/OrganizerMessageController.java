package controller;

import entity.OrganizerData;

import java.util.List;

public class OrganizerMessageController extends AbstractMessageController {

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information.
     * @param OD the OrganizerData of the user.
     */
    public OrganizerMessageController(ConferenceController conference, OrganizerData OD){
        super(conference, OD);
    }

    /**
     * Prints possible inputs, and prompts the user to input one.
     */
    @Override
    public boolean requestAction(){
        System.out.println("[1] - Send a message to one user.");
        System.out.println("[2] - Send a message to all attendees or speakers.");
        System.out.println("[3] - Reply to a message.");
        System.out.println("[4] - Exit");

        String input = super.ConsoleUI.getStringInput();

        if (input.equals("1")) {
            send();
        } else if (input.equals("2")) {
            sendAll();
        } else if (input.equals("3")) {
            reply();
        } else if (input.equals("4")) {
            return false;
        }

        return true;
    }

    /**
     * UI for the user to send a message to all speakers or attendees.
     */
    public void sendAll(){
        System.out.println("[1] - Send to all attendees.");
        System.out.println("[2] - Send to all speakers.");

        List<String> recipients;
        String input = super.ConsoleUI.getStringInput();

        String toTitle;
        if (input.equals("1")) {
            recipients = super.getConference().getAttendeeManager().getAttendeesUsernames();
            toTitle = "Attendees";
        } else if (input.equals("2")) {
            recipients = super.getConference().getSpeakerManager().getSpeakersUsernames();
            toTitle = "Speakers";
        } else {
            return;
        }

        typeMessage(recipients, toTitle);
    }
}
