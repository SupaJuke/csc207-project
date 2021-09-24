package entity;

import java.io.Serializable;
import java.util.List;

public class VIPData extends UserData implements Serializable {

    private String username;
    private String displayname;
    private String password;
    private List<Integer> eventIDs;
    private List<String> friendsList;

    /**
     * Constructs a collection of a VIP data. This VIP defaults to ArrayList
     * implementation for the friends and event IDs lists.
     * @param username    The username of the VIP
     * @param displayname The display name of the VIP
     * @param password    The password used to login for this VIP
     */
    public VIPData(String username, String displayname, String password) {
        super(username, displayname, password);
    }

    /**
     * Constructs a collection of a VIP data. Friends and event IDs lists for this
     * Attendee are predetermined.
     * @param username    The username of the VIP
     * @param displayname The display name of the VIP
     * @param password    The password used to login by the VIP
     * @param eventIDs    The IDs of all the event the VIP wishes to attend or has already attended
     * @param friendsList The list of messageable friends.
     */
    public VIPData(String username, String displayname, String password,
                   List<Integer> eventIDs, List<String> friendsList) {
        super(username, displayname, password, eventIDs, friendsList);
    }

    /**
     * Return the usertype of this User.
     * @return The String "VIP"
     */
    @Override
    public String getUserType(){ return "VIP"; }
}