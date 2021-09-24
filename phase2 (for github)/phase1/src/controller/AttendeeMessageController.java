package controller;

import entity.AttendeeData;

import java.util.ArrayList;
import java.util.List;

public class AttendeeMessageController extends AbstractMessageController {

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information.
     * @param AD the AttendeeData of the user.
     */
    public AttendeeMessageController(ConferenceController conference, AttendeeData AD){
        super(conference, AD);
    }

    /**
     * Prints possible inputs, and prompts the user to input one.
     */
    @Override
    public boolean requestAction(){
        System.out.println("[1] - Send a message.");
        System.out.println("[2] - Reply to a message.");
        System.out.println("[3] - Exit");
        String input = super.ConsoleUI.getStringInput();

        if (input.equals("1")) {
            send();
        } else if (input.equals("2")) {
            reply();
        } else if (input.equals("3")) {
            return false;
        }

        return true;
    }

    /**
     * Get the list of valid target usernames for the user to send a single message to.
     * @return The list of valid target usernames.
     */
    public List<String> getValidTargets(){
        List<String> friendsAndSpeakers = new ArrayList<>();
        friendsAndSpeakers.addAll(super.getUD().getFriendsList());
        friendsAndSpeakers.addAll(super.getConference().getSpeakerManager().getSpeakersUsernames());
        return friendsAndSpeakers;
    }
}
