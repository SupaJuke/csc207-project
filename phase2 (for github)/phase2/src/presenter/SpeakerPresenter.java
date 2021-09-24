package presenter;

import controller.ConferenceController;

import gui.AccountGUI;
import gui.MessageGUI;
import gui.Viewable;
import javafx.stage.Stage;
import presenter.messages.SpeakerMessagePresenter;
import usecase.EventManager;
import usecase.SpeakerManager;
import usecase.UserManager;
import usecase.restriction.RestrictionValidator;

import java.sql.SQLException;
import java.util.List;

/**
 * Acts as the middle man between SpeakerGUI and the Model
 */
public class SpeakerPresenter extends AccountPresenter {

    private final SpeakerManager SM;
    private final EventManager EM;
    private final UserManager UM;

    /**
     * Constructor for SpeakerPresenter initializes SM, EM, AMC
     * @param conference passes ConferenceController object to initialize SM, EM, and AMC
     * @param username current user
     * @param PM stage used in program
     * @param LG passes Viewable object for super constructor
     * @param speakerGUI passes SpeakerGUI object for super constructor
     */
    public SpeakerPresenter(ConferenceController conference, String username, Stage PM, Viewable LG,
                            AccountGUI speakerGUI) {
        super(conference, username, PM, LG, speakerGUI);
        SM = conference.getSpeakerManager();
        EM = conference.getEventManager();
        UM = conference.getUserManager();

        messagePresenter = new SpeakerMessagePresenter(conference, username);
        messageGUI = new MessageGUI(messagePresenter);

    }

    public String speakerEventHelper(String input) {
        try {
            RestrictionValidator RV = new RestrictionValidator();
            int n = Integer.parseInt(input);

            if (!EM.checkEventExists(n))
                return "That event does not exist.";
            else if(!RV.checkIfSpeakerIsAddable(EM.getEventFromID(n), username)){
                return "Speaker is restricted or is already added to the event.";
            }
            else if (addEventSpeaking(username, n)) {
                //adds Speaker to Event and adds Speaking Event to Speaker

                return "Successfully signed up for the event";
            } else {
                return "You are already signed up for the event.";
            }
        }
        //catches errors when parsing input
        catch(NumberFormatException e) { return "That event is not valid. Please try again"; }
    }

    /**
     * Adds speaking event for Speaker
     * @param username username of Speaker
     * @param eventID id of the event
     */
    public boolean addEventSpeaking(String username, int eventID) {
        SM.addEventSpeakingIn(username, eventID);
        //updates Event to have speaker
        try {
            CC.getLocalFileGateway().addSpeakingAt(eventID, username);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return EM.addSpeaker(eventID, username);
    }

    /**
     * @param username username of Speaker
     * @return Returns a list of event ids the Speaker is speaking at
     */
    public List<Integer> getSpeakingEvents(String username) {
        return SM.getSpeakingEvents(username);
    }

    //Getters and Setters

    /**
     * A getter for the event manager
     * @return returns the EventManager
     */
    public EventManager getEM() { return EM; }

    /**
     * A getter for the username
     * @return returns the username
     */
    public String getUsername() { return username; }
}
