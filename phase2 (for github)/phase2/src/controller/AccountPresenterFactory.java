package controller;

import gui.*;
import javafx.stage.Stage;
import presenter.*;

public class AccountPresenterFactory {
    /**
     * Takes a selection from user input and selects the kind of AccountController to construct.
     * @param selection The input from the user
     * @param CC the controller for the conference
     * @param username the user that the controller is being made for
     * @return returns a new AccountController
     */
    public AccountPresenter createAccountPresenter(String selection, ConferenceController CC, String username,
                                                   Stage primaryStage, Viewable LG) {
        switch (selection) {
            case "Attendee":
                AttendeeGUI attGUI = new AttendeeGUI();
                AttendeePresenter AP = new AttendeePresenter(CC, username, primaryStage, LG, attGUI);
                attGUI.setPresenter(AP);
                return AP;
            case "Organizer":
                OrganizerGUI orgGUI = new OrganizerGUI();
                OrganizerPresenter OP = new OrganizerPresenter(CC, username, primaryStage, LG, orgGUI);
                orgGUI.setPresenter(OP);
                return OP;
            case "Speaker":
                SpeakerGUI speGUI = new SpeakerGUI();
                SpeakerPresenter SP = new SpeakerPresenter(CC, username, primaryStage, LG, speGUI);
                speGUI.setPresenter(SP);
                return SP;
            case "VIP":
                VIPGUI vipgui = new VIPGUI();
                VIPPresenter VP = new VIPPresenter(CC, username, primaryStage, LG, vipgui);
                vipgui.setPresenter(VP);
                return VP;
            default:
                return null;
        }
    }
}
