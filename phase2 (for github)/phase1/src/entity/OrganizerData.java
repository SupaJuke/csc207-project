package entity;

import java.util.List;

public class OrganizerData extends UserData {

    /**
     * Constructs a collection of a users data. Specific to an Organizer.
     * This Organiser defaults to ArrayList implementation for the friends and eventIDs lists.
     * @param username    The username of the user
     * @param displayname The display name of the user
     * @param password The password used to login by the user
     */
    public OrganizerData(String username, String displayname, String password) {
        super(username, displayname, password);
    }

    /**
     * Constructs a collection of a users data. Specific to an Organizer. Friends
     * and event IDs lists for this Speaker are predetermined.
     * @param username    The username of the user
     * @param displayname The display name of the user
     * @param password The password used to login by the user
     * @param eventIDs    The IDs of all the event the user wishes to attend or has already attended
     * @param friendsList The list of messageable friends.
     */
    public OrganizerData(String username, String displayname, String password, List<Integer> eventIDs,
                         List<String> friendsList) {
        super(username, displayname, password, eventIDs, friendsList);
    }

    @Override
    public String getUserType(){ return "Organizer"; }
}
