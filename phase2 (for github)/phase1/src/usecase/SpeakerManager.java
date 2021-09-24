package usecase;

import entity.SpeakerData;

import java.util.ArrayList;
import java.util.List;

public class SpeakerManager {

    public List<SpeakerData> speakers;

    /**
     * Constructs a SpeakerManager with a default ArrayList implementation
     * of the list.
     */
    public SpeakerManager() { this.speakers = new ArrayList<SpeakerData>(); }

    /**
     * Constructs a Speaker Manager. The list of speakers is predetermined.
     * @param speakers The list of allexisting speakers
     */
    public SpeakerManager(List<SpeakerData> speakers) { this.speakers = speakers; }

    /**
     * Add a new event ID that the speaker will speak at if they are not already speaking there
     * @param username The speaker that will speak at the event
     * @param eventID The new event ID to be spoken at by this speaker
     * @return True iff the speaker exists, the event is not already being spoken at,
     *         and the event was added successfully
     */
    public boolean addEventSpeakingIn(String username, int eventID) {
        // First check if the speaker exists
        if (this.checkSpeakerExists(username)){

            // Retrieve the speakers data
            SpeakerData speakerToEdit = this.getSpeakerByID(username);

            // Check if the event is already being spoken at by speakerToEdit, if not, add it
            if (this.isSpeakingAt(eventID, speakerToEdit.getSpeakingAt())) {
                return false;
            } else {
                return speakerToEdit.getSpeakingAt().add(eventID);
            }
        }
        return false;
    }

    /**
     * Adds a speaker (and all of their data) to the list of speakers iff they are
     * not already in the list of speakers.
     * @param speakerData The speaker that is to be added
     * @return True iff the speaker does not already exist and they were added successfully
     */
    public boolean addSpeaker(SpeakerData speakerData){
        if (this.checkSpeakerExists(speakerData.getUsername())){
            return false;
        } else {
            return this.speakers.add(speakerData);
        }
    }

    /**
     * Verify that the speaker already exists in SpeakerManager
     * @param username The speakers username
     * @return True iff the speaker exists in the list of speakers
     */
    public boolean checkSpeakerExists(String username){
        for (SpeakerData speaker : this.speakers){
            if (speaker.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if this speaker is speaking at the specified event
     * @param SpeakingAt The speakers list of events they are speaking at
     * @param eventID The event ID that the speaker may be speaking at
     * @return True iff the speaker is speaking at the event
     */
    public boolean isSpeakingAt(int eventID, List<Integer> SpeakingAt){
        for (Integer eventIDSpeakingAt : SpeakingAt){
            if (eventIDSpeakingAt == eventID){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a speaker's data given the speaker's username
     * @param username The username of the speaker
     * @return Returns the speaker iff they exist
     */
    public SpeakerData getSpeakerByID(String username){
        for (SpeakerData speaker : this.speakers) {
            if (speaker.getUsername().equals(username)) {
                return speaker;
            }
        }
        return null;
    }

    /**
     * Returns a speaker's list of event IDs they are speaking at given the speaker's username
     * @param username The username of the speaker
     * @return The list of event ID's iff they exist
     */
    public List<Integer> getSpeakingEvents(String username){
        if (this.checkSpeakerExists(username)){
            return this.getSpeakerByID(username).getSpeakingAt();
        }
        return null;
    }

    /**
     * Returns a list of all the speakers usernames
     * @return The list of usernames of the speakers
     */
    public List<String> getSpeakersUsernames() {
        List<String> speakerNames = new ArrayList<>();

        for (SpeakerData speakerData : this.speakers){
            speakerNames.add(speakerData.getUsername());
        }

        return speakerNames;
    }

    /**
     * Cancels speaking at the event with the ID eventID
     * @param eventID The event ID of the event
     * @return True iff the event was removed
     */
    public boolean cancelEventSpeakingAt(int eventID, String speakerUsername){
        for (SpeakerData speaker : this.speakers){
            if (speaker.getUsername().equals(speakerUsername)){
                return speaker.removeSpeakingAt(eventID);
            }
        }
        return false;
    }

    public List<SpeakerData> getSpeakers() {
        return speakers;
    }
}
