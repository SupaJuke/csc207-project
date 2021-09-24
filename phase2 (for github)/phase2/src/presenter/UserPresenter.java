package presenter;

import gateway.LocalFileGateway;
import usecase.UserManager;

import java.sql.SQLException;

public class UserPresenter {

    private final UserManager UM;
    private final String username;
    private final LocalFileGateway GW;

    /**
     * Constructs a UserPresenter containing the UserManager and the Gateway.
     * @param UM The UserManager
     * @param username The username of the User
     */
    public UserPresenter(UserManager UM, String username, LocalFileGateway GW) {
        this.UM = UM;
        this.username = username;
        this.GW = GW;
    }

    /**
     * Change the UserData display name.
     * @param name the new display name
     * @return 0 iff the display name has been changed;
     *         else returns an integer according to the unsatisfied condition(s)
     */
    public String changeDisplayName(String name) throws SQLException {
        if (name.equals("")) {
            return "No display name received! " +
                    "Please input a new display name";
        }
        if (UM.getUserByID(username).getDisplayname().equals(name)) {
            return "Same display name received! " +
                    "Please input a new display name";
        }
        UM.changeDisplayName(username, name);
        GW.changeDisplaynameInDB(username, name);
        return "Successfully changed display name to " + UM.getUserByID(username).getDisplayname();
    }

    /**
     * Verify and change the UserData password.
     * @param oldPW the input old password
     * @param newPW the input new password
     * @param confirmPW the input confirm password
     * @return 0 iff the password has been changed;
     *         else returns an integer according to the unsatisfied condition(s)
     */
    public String changePassword(String oldPW, String newPW, String confirmPW) throws SQLException {
        // old password input unmatched
        if (!UM.getUserByID(username).getPassword().equals(oldPW)) {
            return "The old password does not match! " +
                    "Please try again";
        }
        // new password input empty
        if (newPW.equals("")) {
            return "No password received! " +
                    "Please input a new password";
        }
        // new password input is the same
        if (UM.getUserByID(username).getPassword().equals(newPW)) {
            return "Same password received! " +
                    "Please input a new password";
        }
        // confirm password does not match
        if (!newPW.equals(confirmPW)) {
            return "Confirm password does not match! " +
                    "Please try again";
        }
        // Conditions are satisfied
        UM.changePassword(username, newPW);
        GW.changePasswordInDB(username, newPW);
        return "Successfully changed the password";
    }

    /**
     * Returns the User's display name.
     * @return User's display name
     */
    public String getDisplayName() { return UM.getUserByID(username).getDisplayname(); }
}
