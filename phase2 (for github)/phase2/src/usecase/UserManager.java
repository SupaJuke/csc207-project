package usecase;

import entity.UserData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserManager implements Serializable, Savable {

    private List<UserData> users;

    /**
     * Constructs an empty UserManager.
     */
    public UserManager() {
        this.users = new ArrayList<UserData>();
    }

    /**
     * Constructs a UserManager with its Users predetermined.
     * @param users The list of Users
     */

    public UserManager(List<UserData> users){
        this.users = users;
    }

    /**
     * Change the User's display name into a new name iff the User exists
     * and the new name is not the same as the previous one.
     * @param username The username of the User that is changing his or her display name
     * @param name The new display name the User is changing into
     * @return True iff the User's display name is changed; else returns false
     */
    public boolean changeDisplayName(String username, String name) {
        UserData user = getUserByID(username);
        // check if the user exists or not
        if (!checkUserExists(username)) {
            return false;
        }

        // check if the display name is the same as the previous one or not
        else if (user.getDisplayname().equals(name)) {
            return false;
        }
        // change the user's display name and return true
        else {
            user.setDisplayname(name);
            return true;
        }
    }

    /**
     * Change the User's display name into a new name iff the User exists
     * and the new name is not the same as the previous one.
     * @param user The User that is changing his or her display name
     * @param name The new display name the User is changing into
     * @return True iff the user's display name is changed; else returns false
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
     * Change the User's password into a new password iff the User exists
     * and the new password is not the same as the previous one.
     * @param username The username of the User that is changing his or her password
     * @param pw The new password the User is changing into
     * @return True iff the User's password is changed; else returns false
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
     * Change the User's password into a new password iff the User exists
     * and the new password is not the same as the previous one.
     * @param user The User that is changing his or her password
     * @param pw The new password the User is changing into
     * @return True iff the User's password is changed; else returns false
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
     * Verify if the User exists in UserManager.
     * @param username The User's username
     * @return True iff the User exists in users
     */
    public boolean checkUserExists(String username) {
        UserData user = getUserByID(username);
        return checkUserExists(user);
    }

    /**
     * Verify if the User exists in UserManager.
     * @param user The User
     * @return True iff the User exists in users
     */
    public boolean checkUserExists(UserData user) {
        return this.users.contains(user);
    }

    /**
     * Returns a User's data given the User's username.
     * @param username The User's username
     * @return Returns the User's data iff it exists; else returns null
     */
    public UserData getUserByID(String username) {
        for (UserData user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Add a new User to users.
     * @param user The new User
     * @return Returns true iff the new User has been added to users
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
     * Return a list of usernames of all existing Users.
     * @return The list of usernames of all existing Users.
     */
    public List<String> getUsersUsernames() {
        List<String> userNames = new ArrayList<>();

        for (UserData user : users) {
            userNames.add(user.getUsername());
        }

        return userNames;
    }

    /**
     * Return a list of all existing Users
     * @return a list of all exiting Users
     */
    public List<UserData> getUsers() { return users; }
}
