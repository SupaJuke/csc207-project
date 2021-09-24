package presenter.messages;

import controller.ConferenceController;
import entity.UserData;

public class MessagePresenterFactory {
    /**
     * Takes a selection from user input and selects the kind of AbstractMessageController to construct.
     * @param selection The input from the user (name of the usertype)
     * @param CC the controller for the conference
     * @param UD the user that the controller is being made for
     * @return returns a new AbstractMessageController
     */
    public AbstractMessagePresenter createDefaultAccountMessageController(String selection, ConferenceController CC,
                                                                           UserData UD) {
        switch (selection) {

            case "Attendee":
                return new AttendeeMessagePresenter(CC, UD.getUsername());
            case "Organizer":
                return new OrganizerMessagePresenter(CC, UD.getUsername());
            case "Speaker":
                return new SpeakerMessagePresenter(CC, UD.getUsername());
            default:
                return null;
        }
    }
}
