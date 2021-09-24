package entity;

import java.util.List;

public class AttendeeData extends UserData {

    /**
     * Constructs a collection of a users data. This Attendee defaults to ArrayList
     * implementation for the friends and event IDs lists.
     * @param username The username of the user
     * @param displayname The display name of the user
     * @param password The password used to login by the user
     */
    public AttendeeData(String username, String displayname, String password) {
        super(username, displayname, password);
    }

    /**
     * Constructs a collection of a users data. Friends and event IDs lists for this
     * Attendee are predetermined.
     * @param username    The username of the user
     * @param displayname The display name of the user
     * @param password The password used to login by the user
     * @param eventIDs    The IDs of all the event the user wishes to attend or has already attended
     * @param friendsList The list of messageable friends.
     */
    public AttendeeData(String username, String displayname, String password, List<Integer> eventIDs, List<String> friendsList) {
        super(username, displayname, password, eventIDs, friendsList);
    }

    @Override
    public String getUserType(){ return "Attendee"; }


}
