package usecase;

import entity.UserData;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class UserManager implements Serializable {

    private List<UserData> users;

    /**
     * Constructs a UserManager with its users predetermined.
     */
    public UserManager() { this.users = new ArrayList<UserData>(); }

    /**
     * Constructs a UserManager with its users predetermined.
     * @param users The list of all existing users
     */
    public UserManager(List<UserData> users){
        this.users = users;
    }

    /**
     * Change the user's displayname into a new name iff the user exists
     * and the new name is not the same as the previous one.
     * @param username The username of the user that is changing his or her displayname
     * @param name The new displayname the user is changing into
     * @return True iff the user's displayname is changed. Else returns False
     */
    public boolean changeDisplayName(String username, String name) {
        UserData user = getUserByID(username);
        // check if the user exists or not
        if (!checkUserExists(username)) {
            return false;
        }

        // check if the displayname is the same as the previous one or not
        else if (user.getDisplayname().equals(name)) {
            return false;
        }
        // change the user's displayname and return true
        else {
            user.setDisplayname(name);
            return true;
        }
    }

    /**
     * Change the user's displayname into a new name iff the user exists
     * and the new name is not the same as the previous one.
     * @param user The username of the user that is changing his or her displayname
     * @param name The new displayname the user is changing into
     * @return True iff the user's displayname is changed. Else returns False
     */
    public boolean changeDisplayName(UserData user, String name) {
        // check if the user exists or not
        if (!checkUserExists(user)) {
            return false;
        }

        // check if the displayname is the same as the previous one or not
        else if (user.getDisplayname().equals(name) || user.getDisplayname().equals("")) {
            return false;
        }
        // change the user's displayname and return true
        else {
            user.setDisplayname(name);
            return true;
        }
    }

    /**
     * Change the user's password into a new password iff the user exists
     * and the new password is not the same as the previous one.
     * @param username The username of the user that is changing his or her password
     * @param pw The new password the user is changing into
     * @return True iff the user's password is changed; else returns false
     */
    public boolean changePassword(String username, String pw) {
        UserData user = getUserByID(username);
        // check if the user exists or not
        if (!checkUserExists(username)) {
            return false;
        }

        // check if the password is the same as the previous one or not
        else if (user.getPassword().equals(pw)) {
            return false;
        }
        // change the user's password and return true
        else {
            user.setPassword(pw);
            return true;
        }
    }

    /**
     * Change the user's password into a new password iff the user exists
     * and the new password is not the same as the previous one.
     * @param user The username of the user that is changing his or her password
     * @param pw The new password the user is changing into
     * @return True iff the user's password is changed; else returns false
     */
    public boolean changePassword(UserData user, String pw) {
        // check if the user exists or not
        if (!checkUserExists(user)) {
            return false;
        }

        // check if the password is the same as the previous one or not
        else if (user.getPassword().equals(pw)) {
            return false;
        }
        // change the user's password and return true
        else {
            user.setPassword(pw);
            return true;
        }
    }

    /**
     * Verify if the user exists in UserManager.
     * @param username The user's username
     * @return True iff the user exists in the list of users
     */
    public boolean checkUserExists(String username) {
        UserData user = getUserByID(username);
        return checkUserExists(user);
    }

    /**
     * Verify if the user exists in UserManager.
     * @param user The user
     * @return True iff the user exists in the list of users
     */
    public boolean checkUserExists(UserData user) {
        return this.users.contains(user);
    }

    /**
     * Returns a user's data given the user's username.
     * @param username The user's username
     * @return Returns the user's data iff it exists. Return Null otherwise.
     */
    public UserData getUserByID(String username) {
        for (UserData user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /* TODO: This needs modification for phase 2 */
    /**
     * Add a new user to users.
     * @param user The new user
     * @return Returns true iff the new user has been added to users
     */
    public boolean addUser(UserData user) {
        // Check if the user is already in the list or not
        if (checkUserExists(user)) {
            return false;
        }
        users.add(user);
        return true;
    }

    /**
     * Return a list of usernames of all existing users.
     * @return The list of usernames of all existing users.
     */
    public List<String> getUsersUsernames() {
        List<String> userNames = new ArrayList<>();

        for (UserData user : users) {
            userNames.add(user.getUsername());
        }

        return userNames;
    }

    public List<UserData> getUsers() {
        return users;
    }
}
