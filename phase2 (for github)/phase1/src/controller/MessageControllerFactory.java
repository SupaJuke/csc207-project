package controller;

import entity.AttendeeData;
import entity.OrganizerData;
import entity.SpeakerData;
import entity.UserData;

public class MessageControllerFactory {
    /**
     * Takes a selection from user input and selects the kind of AbstractMessageController to construct.
     * @param selection The input from the user
     * @param CC the controller for the conference
     * @param UD the user that the controller is being made for
     * @return returns a new AbstractMessageController
     */
    public AbstractMessageController createDefaultAccountMessageController(String selection, ConferenceController CC, UserData UD) {
        switch (selection) {
            case "Attendee":
                return new AttendeeMessageController(CC, (AttendeeData) UD);
            case "Organizer":
                return new OrganizerMessageController(CC, (OrganizerData) UD);
            case "Speaker":
                return new SpeakerMessageController(CC, (SpeakerData) UD);
            default:
                return null;
        }
    }
}
