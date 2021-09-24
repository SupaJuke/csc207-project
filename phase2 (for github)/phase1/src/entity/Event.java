package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {

    private int eventID;
    private int eventTime;
    private String eventTitle;
    private List<String> speakerUsernames;
    private List<String> usersInAttendance;

    /**
     * Constructs an event. The list of users in attendance is predetermined.
     * @param eventID The unique event ID
     * @param eventTime The time an event takes place
     * @param eventTitle The title of the event
     * @param speakerUsernames The usernames of the speakers of this event
     * @param usersInAttendance The users that are attending the event
     */
    public Event(int eventID, int eventTime, String eventTitle,
                 List<String> speakerUsernames, List<String> usersInAttendance){
        this.eventID = eventID;
        this.eventTime = eventTime;
        this.eventTitle = eventTitle;
        this.speakerUsernames = speakerUsernames;
        this.usersInAttendance = usersInAttendance;
    }

    /**
     * Constructs an event without a speaker. No speaker is represented as the empty string.
     * The users in attendance is defaulted to an ArrayList.
     * @param eventID The unique event ID
     * @param eventTime The time an event takes place
     * @param eventTitle The title of the event
     */
    public Event(int eventID, int eventTime, String eventTitle){
        this.eventID = eventID;
        this.eventTime = eventTime;
        this.eventTitle = eventTitle;
        this.speakerUsernames = new ArrayList<>();
        this.usersInAttendance = new ArrayList<>();
    }

    /**
     * Get the unique event ID
     * @return The eventID int
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Get the time the event takes place
     * @return The eventTime int
     */
    public int getEventTime() {
        return eventTime;
    }

    /**
     * Set the time the event takes place
     * @param eventTime The new time the event will take place
     */
    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Get the title of this event
     * @return The eventTitle string
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Set the title of this event
     * @param eventTitle The eventTitle string
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Get the usernames of the Speakers of this event
     * @return The speakerUsername string
     */
    public List<String> getSpeakerUsernames() {
        return speakerUsernames;
    }

    /**
     * Add a Speaker of this event by username
     * @param speakerUsername The username of the new Speaker
     */
    public boolean addSpeakerUsername(String speakerUsername) { return speakerUsernames.add(speakerUsername); }

    /**
     * Remove a Speaker of this event by username
     * @param speakerUsername The username of the Speaker
     */
    public boolean removeSpeakerUsername(String speakerUsername){ return speakerUsernames.remove(speakerUsername);}

    /**
     * Gets the list of users in attendance of the event
     * @return The list of users
     */
    public List<String> getUsersInAttendance() {
        return usersInAttendance;
    }

    /**
     * Add a user into the attendance list of this event
     * @param username The username of the user that now attends the event
     */
    public boolean addUserInAttendance(String username) {
        return this.usersInAttendance.add(username);
    }
}
