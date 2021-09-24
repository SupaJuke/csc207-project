package controller;

import entity.UserData;
import usecase.EventBrowser;
import usecase.EventManager;
import usecase.RoomManager;

public class AccountControllerFactory {
    /**
     * Takes a selection from user input and selects the kind of AccountController to construct.
     * @param selection The input from the user
     * @param CC the controller for the conference
     * @param UD the user that the controller is being made for
     * @param EM the manager of the events for the controller
     * @return returns a new AccountController
     */
    public AccountController createDefaultAccountController(String selection, ConferenceController CC, UserData UD,
                                                            EventManager EM, EventBrowser EB) {
        switch (selection) {
            case "Attendee":
                return new AttendeeController(CC, UD);
            case "Organizer":
                return new OrganizerController(CC, UD);
            case "Speaker":
                return new SpeakerController(CC, UD, EM, EB);
            default:
                return null;
        }
    }
}
