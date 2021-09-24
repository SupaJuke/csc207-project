package controller;

import usecase.UserManager;
import java.util.Scanner;
import entity.UserData;

public class UserController implements Accessible {

    private UserManager UM;
    private UserData UD;

    /**
     * Constructs a UserController containing the UserManager.
     * @param userManager The UserManager
     */
    public UserController(UserManager userManager, UserData userData) {
        this.UM = userManager;
        this.UD = userData;
    }

    /**
     * Accesses UserController UI
     */
    @Override
    public void accessUI() {

        boolean run = true;
        while (run) {

            System.out.println("=== Profile Menu ===");
            System.out.println("[1] - Change display name.");
            System.out.println("[2] - Change password.");
            System.out.println("[3] - Exit.");

            Scanner reader = new Scanner(System.in);
            String input = reader.nextLine();

            switch (input) {
                case "1":
                    // change display name
                    userHelper(1);
                    break;
                case "2":
                    // change password
                    userHelper(2);
                    break;
                case "3":
                    System.out.println("Returning to main menu...");
                    run = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * A helper for AccessUI
     */
    private void userHelper(int type) {
        Scanner reader = new Scanner(System.in);
        String input;

        // type = 1 > change display name
        if (type == 1) {
            while (true) {
                System.out.print("Please enter the new display name: ");
                input = reader.nextLine();
                if (this.changeDisplayName(input)) {
                    break;
                }
                else {
                    System.out.println("Please try again");
                }
            }
        }

        // type = 2 > change password
        if (type == 2) {
            while (true) {
                System.out.print("Please enter new password: ");
                input = reader.nextLine();
                if (this.changePassword(input)) {
                    break;
                } else {
                    System.out.println("Please try again");
                }
            }
        }
    }

    /**
     * Change the UserData displayname.
     * @param name the new displayname
     * @return True iff the displayname has been changed
     */
    private boolean changeDisplayName(String name) {
        if (UM.changeDisplayName(this.UD, name)) {
            System.out.println("Display name has been changed to " + name + ".");
            return true;
        }
        else {
            System.out.println("Display name could not be changed.");
            return false;
        }
    }

    /**
     * Change the UserData password.
     * @param password the new password
     * @return True iff the password has been changed
     */
    private boolean changePassword(String password) {
        if (UM.changePassword(this.UD, password)) {
            System.out.println("Password has been changed to " + password + ".");
            return true;
        }
        else {
            System.out.println("Password could not be changed.");
            return false;
        }
    }
}
