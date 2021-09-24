package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class UserData implements Serializable {

    private String username;
    private String displayname;
    private String password;
    private List<Integer> eventIDs;
    private List<String> friendsList;

    /**
     * Constructs a collection of a users data. This user defaults to ArrayList
     * implementation for the friends and event IDs lists.
     * @param username The username of the user
     * @param displayname The display name of the user
     * @param password The password used to login for this user
     */
    public UserData(String username, String displayname, String password){
        this.username = username;
        this.displayname = displayname;
        this.password = password;
        this.eventIDs = new ArrayList<Integer>();
        this.friendsList = new ArrayList<String>();
    }

    /**
     * Constructs a collection of a users data. Friends and event IDs lists for this
     * user are predetermined.
     * @param username The username of the user
     * @param displayname The display name of the user
     * @param eventIDs The IDs of all the event the user wishes to attend or has already attended
     * @param friendsList The list of messageable friends.
     */
    public UserData(String username, String displayname, String password, List<Integer> eventIDs,
                    List<String> friendsList) {
        this.username = username;
        this.displayname = displayname;
        this.password = password;
        this.eventIDs = eventIDs;
        this.friendsList = friendsList;
    }

    /**
     * Gets the username of the associated user
     * @return The username String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the display name of the associated user
     * @return The displayname string
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * Gets the list of event IDs that the user will attend of the associated user
     * @return The eventIDs list
     */
    public List<Integer> getEventIDs() {
        return eventIDs;
    }

    /**
     * Gets the list of messageable friends of the associated user
     * @return The friendsList list
     */
    public List<String> getFriendsList() {
        return friendsList;
    }

    /**
     * Sets a new display name of the associated user
     * @param displayname The new display name of the associated user
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    /**
     * Adds a new event ID to the list of event ID iff the event ID
     * is not already in the list of event ID of the associated user
     * @param eventId The unique ID of the new event
     * @return True iff the event ID was added successfully
     */
    // Soup's Comment: change to void & remove logic
    public boolean addEventID(int eventId) {
        return this.eventIDs.add(eventId);
    }

    /**
     * Adds a new friend to the list of messageable friends iff the friend
     * is not already in the list of friends of the associated user
     * @param newFriendName The username of the new friend
     */
    public void addFriend(String newFriendName) {
        this.friendsList.add(newFriendName);
    }

    /**
     * Removes a friend from the list friends of the associated user
     * @param username The username of the friend to be removed
     */
    public void removeFriend(String username){
        this.friendsList.remove(username);
    }

    /**
     * Gets the password used to login for this user
     * @return The password for logging in
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set a new password for this user
     * @param password The password used to login
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a String representation of the type of user this is
     * @return The type of user as a string
     */
    public String getUserType(){ return "User"; }

    /**
     * Removes an ID of an event the user was attending
     * @param eventID The username of the friend to be removed
     * @return True iff the event ID was in the list and it was removed
     */
    public boolean removeEventID(Integer eventID){ return this.eventIDs.remove(eventID); }
}
