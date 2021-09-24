package controller;

import gateway.LocalFileGateway;
import usecase.UserFactory;
import usecase.UserManager;

import java.util.Scanner;

public class LoginController implements Accessible {

    private final ConferenceController CC;
    private final UserManager UM;
    private final LocalFileGateway gateway;
    /**
     * The constructor for the loginController
     * @param conference the the conference that this login controller is made for
     */
    public LoginController(ConferenceController conference) {
        this.CC = conference;
        this.UM = conference.getUserManager();
        gateway = new LocalFileGateway();
    }

    /**
     * The text UI for the loginController which goes through the login/sign up process
     */
    @Override
    public void accessUI() {
        boolean quit = false;

        Scanner reader = new Scanner(System.in);
        while(!quit) {
            System.out.println("=== Login ===");
            System.out.println("[1] - Login ");
            System.out.println("[2] - Signup");
            System.out.println("[3] - Close");

            String in = reader.nextLine();

            switch(in) {
                case "1":
                    String username = loginUI();
                    if (username.equals("")) {
                        break;
                    }
                    System.out.println("Login successful.\n");
                    createUserUI(username);
                    break;
                case "2":
                    signUpUI();
                    break;
                case "3":
                    System.out.println("Exiting Program...");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
        gateway.SaveAll(CC);
        reader.close();
    }

    /**
     * A helper for accessUI that goes through the login process for the user.
     * If the user enters the empty string as its username it will will return the empty string immediately
     *
     */
    private String loginUI() {
        while (true) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Press enter to return to the Login Menu.");
            System.out.print("Type in your username: ");
            String username_inputted = reader.nextLine();

            if (username_inputted.equals("")) {
                return username_inputted;
            }

            System.out.print("Type in your password: ");
            String password_inputted = reader.nextLine();

            boolean confirmation = login(username_inputted, password_inputted);

            if (confirmation) {
                return username_inputted;
            }
            else {
                System.out.println("The username or password is incorrect. Please try again.");
            }
        }
    }

    /**
     * A helper for accessUI that creates the account controller and its message controller and accesses its UI
     * @param username the username of the user that logged in
     */
    private void createUserUI(String username) {
        AccountControllerFactory accountControllerFactory = new AccountControllerFactory();
        MessageControllerFactory messageControllerFactory = new MessageControllerFactory();

        // Create the account controller
        AccountController accountController = accountControllerFactory.createDefaultAccountController(
                UM.getUserByID(username).getUserType(), CC,
                UM.getUserByID(username), CC.getEventManager(), CC.getEventBrowser());

        // Create the message controller and assign it to the account controller
        accountController.setMessageController(messageControllerFactory.createDefaultAccountMessageController(
                UM.getUserByID(username).getUserType(), CC, UM.getUserByID(username)));
        accountController.accessUI();
    }

    /**
     * A helper for accessUI that goes through the sign up process for the user
     */
    private void signUpUI() {
        String new_username_inputted = signUpUIHelper();

        if (new_username_inputted.equals("")) {
            return;
        }

        System.out.print("Type in your new password: ");
        Scanner reader = new Scanner(System.in);
        String new_password_inputted = reader.nextLine();
        if (new_password_inputted.equals("")) {
            return;
        }

        System.out.print("Type in your new display name: ");
        String displayName_inputted = reader.nextLine();
        if (displayName_inputted.equals("")) {
            return;
        }

        System.out.println("\nSelect the Account Type: ");
        System.out.println("[1] - Regular Attendee");
        System.out.println("[2] - Organizer");
        System.out.println("[3] - Speaker");
        String type_inputted = signUpUITypeHelper();

        signUp(new_username_inputted, displayName_inputted, new_password_inputted, type_inputted);
        System.out.println("Sign Up successful. \n");
    }

    /**
     * A helper for the signUpUI method that returns a username if it does not yet exist in the lit of users
     * @return returns the new username
     */
    private String signUpUIHelper() {
        while (true) {
            System.out.println("Press enter to return to the Login Menu.");
            System.out.print("Type in your new username: ");

            Scanner reader = new Scanner(System.in);
            String new_username_inputted = reader.nextLine();

            if (!UM.checkUserExists(new_username_inputted) || new_username_inputted.equals("")) {
                return new_username_inputted;
            }
            else {
                System.out.println("This username already exists. Please try again.");
            }
        }
    }

    /**
     * A helper for the signUpUI method that returns the type of user that the user wants to make
     * @return returns the type that the user inputted
     */
    private String signUpUITypeHelper() {
        while(true) {
            Scanner reader = new Scanner(System.in);
            String type_inputted = reader.nextLine();

            if (type_inputted.equals("1") || type_inputted.equals("2") || type_inputted.equals("3")){
                return type_inputted;
            }
            else {
                System.out.println("Invalid input. Please try again. \n");
            }
        }
    }

    /**
     * Logs in a user into the conference
     * @param username the name of the user that wants to log in
     * @param password the password given by the user
     * @return returns true when the user successfully logs in, and returns false otherwise
     */
    public boolean login(String username, String password){
        if (UM.checkUserExists(username)){
            return (UM.getUserByID(username).getPassword().equals(password));
        }
        return false;
    }

    /**
     * Creates a new user
     * @param username the name of the user that wants to sign up
     * @param displayName the display name of the user that wants to sign up
     * @param password the password of the username
     * @param type the type of user. 1 means a regular user. 2 means an organizer. 3 means a speaker.
     */
    public void signUp(String username, String displayName, String password, String type){
        UserFactory userFactory = new UserFactory();
        switch (type) {
            case "1":
                CC.getAttendeeManager().addAttendee(userFactory.createDefaultAttendee(username, displayName, password));
                UM.addUser(CC.getAttendeeManager().getAttendeeByID(username));
                break;
            case "2":
                UM.addUser(userFactory.createDefaultOrganizer(username, displayName, password));
                break;
            case "3":
                CC.getSpeakerManager().addSpeaker(userFactory.createDefaultSpeaker(username, displayName, password));
                UM.addUser(CC.getSpeakerManager().getSpeakerByID(username));
                break;
        }
        gateway.SaveUsers(CC);
    }
}
