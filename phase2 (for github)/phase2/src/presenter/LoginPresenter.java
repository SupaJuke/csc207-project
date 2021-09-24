package presenter;

import controller.AccountPresenterFactory;
import controller.ConferenceController;
import gui.Viewable;
import javafx.stage.Stage;
import usecase.UserFactory;
import usecase.UserManager;

import java.sql.SQLException;

public class LoginPresenter{

    private final ConferenceController conferenceController;
    private final UserManager userManager;

    /**
     * A constructor for LoginPresenter
     * @param conference the conference controller in charge of the conference
     */
    public LoginPresenter(ConferenceController conference){
        conferenceController = conference;
        userManager = conference.getUserManager();
    }

    /**
     * Checks if the username is an existing user and matches it with the entered password
     * @param username the username that the user entered
     * @param password the password that the user entered
     * @return returns true iff username is an existing user and password matches the user's password
     */
    public boolean login(String username, String password) {
        if (userManager.checkUserExists(username)){
            return (userManager.getUserByID(username).getPassword().equals(password));
        }
        return false;
    }

    /**
     * A method that creates the user's presenter
     * @param username the username of the user
     * @param primaryStage the Stage that the GUI uses
     * @param LG the Viewable GUI
     * @return returns the AccountPresenter
     */
    public AccountPresenter createAccountPresenter(String username, Stage primaryStage, Viewable LG){
        AccountPresenterFactory accountPresenterFactory = new AccountPresenterFactory();
        return accountPresenterFactory.createAccountPresenter(
                userManager.getUserByID(username).getUserType(), conferenceController, username, primaryStage, LG);
    }
    /**
     * Creates a new user with username, password, and display name
     * @param username the name of the user
     * @param password the password used to login to the conference
     * @param displayName the display name of the user
     * @param type the type of user the user wants to be
     * @return returns true iff the user is successfully created
     */
    public boolean signUp(String username, String password, String displayName, String type) {
        if (userManager.checkUserExists(username) || username.equals("")
                || password.equals("") || displayName.equals("") || type.equals("")) {
            return false;
        }
        else {
            try {
                signUpNewUser(username, displayName, password, type);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            return true;
        }
    }

    private void signUpNewUser(String username, String displayName, String password, String type) throws SQLException {
        UserFactory userFactory = new UserFactory();
        switch (type) {
            case "Attendee":
                conferenceController.getAttendeeManager().addAttendee(
                        userFactory.createDefaultAttendee(username, displayName, password));
                userManager.addUser(conferenceController.getAttendeeManager().getAttendeeByID(username));
                break;
            case "Organizer":
                userManager.addUser(userFactory.createDefaultOrganizer(username, displayName, password));
                break;
            case "Speaker":
                conferenceController.getSpeakerManager().addSpeaker(
                        userFactory.createDefaultSpeaker(username, displayName, password));
                userManager.addUser(conferenceController.getSpeakerManager().getSpeakerByID(username));
                break;
            case "VIP":
                conferenceController.getVIPManager().addVIP(userFactory.createDefaultVIP(
                        username, displayName, password));
                userManager.addUser(conferenceController.getVIPManager().getVIPByID(username));
                break;
            default:

        }
        AddUserToDatabase(username, displayName, password, type);
    }
    private void AddUserToDatabase(String user, String disName, String pass, String accType) throws SQLException {
        conferenceController.getLocalFileGateway().insertNewUser(user, disName, pass, accType);
    }

    public void shutdownSequence() {
        conferenceController.getLocalFileGateway().saveAllFiles();
    }
}
