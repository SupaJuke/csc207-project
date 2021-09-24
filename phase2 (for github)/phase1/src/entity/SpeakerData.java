package entity;

import java.util.List;

public class SpeakerData extends UserData{

    private List<Integer> speakingAt;

    /**
     * Constructs a collection of a users data. Specific this time to a Speaker.
     * This Speaker defaults to ArrayList implementation for the friends and
     * event IDs lists.
     * @param username The username of the user
     * @param displayname The display name of the user
     * @param password The password used to login by the user
     * @param speakingAt The list of event ID's the speaker is currently speaking at
     */
    public SpeakerData(String username, String displayname, String password, List<Integer> speakingAt) {
        super(username, displayname, password);
        this.speakingAt = speakingAt;
    }

    /**
     * Constructs a collection of a users data. Specific this time to a Speaker. Friends
     * and event IDs lists for this Speaker are predetermined.
     * @param username The username of the user
     * @param displayname The display name of the user
     * @param password The password used to login by the user
     * @param eventIDs The IDs of all the event the user wishes to attend or has already attended
     * @param friendsList The list of messageable friends.
     * @param speakingAt The list of event ID's the speaker is currently speaking at
     */
    public SpeakerData(String username, String displayname, String password, List<Integer> eventIDs,
                       List<String> friendsList, List<Integer> speakingAt) {
        super(username, displayname, password, eventIDs, friendsList);
        this.speakingAt = speakingAt;
    }

    /**
     * Get the list of event ID's the speaker is currently speaking at.
     * @return The speakingAt list
     */
    public List<Integer> getSpeakingAt() {
        return speakingAt;
    }

    @Override
    public String getUserType(){ return "Speaker"; }

    public boolean removeSpeakingAt(int eventID){
        return this.speakingAt.remove((Integer) eventID);
    };
}
