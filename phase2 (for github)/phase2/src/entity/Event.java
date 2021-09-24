package entity;

import java.io.Serializable;
import java.util.*;

public class Event implements Serializable {

    private final int eventID;
    private final int eventTime;
    private final int eventDuration;
    private final String eventTitle;
    private List<String> speakerUsernames;
    private List<String> usersInAttendance;
    private Map<String, List<String>> restrictions;
    private Map<String, List<String>> requirements;
    private int roomID = -1;

    /**
     * Constructs an empty Event with the default parameters.
     */
    public Event() {
        this.eventID = -1;
        this.eventTime = -1;
        this.eventDuration = 0;
        this.eventTitle = "";
        this.speakerUsernames = new ArrayList<>();
        this.usersInAttendance = new ArrayList<>();
        this.restrictions = new HashMap<>();
        this.requirements = new HashMap<>();
    }

    /**
     * Constructs an event. The list of users in attendance is predetermined.
     * @param eventID The unique event ID
     * @param eventTime The time an event takes place
     * @param eventDuration The duration of time an event takes place
     * @param eventTitle The title of the event
     * @param speakerUsernames The usernames of the speakers of this event
     * @param usersInAttendance The users that are attending the event
     * @param restrictions The restrictions for the event
     * @param requirements The requirements for the event
     */
    public Event(int eventID, int eventTime, int eventDuration, String eventTitle,
                 List<String> speakerUsernames, List<String> usersInAttendance,
                 Map<String, List<String>> restrictions,
                 Map<String, List<String>> requirements){
        this.eventID = eventID;
        this.eventTime = eventTime;
        this.eventDuration = eventDuration;
        this.eventTitle = eventTitle;
        this.speakerUsernames = speakerUsernames;
        this.usersInAttendance = usersInAttendance;
        this.restrictions = restrictions;
        this.requirements = requirements;
    }

    /**
     * Constructs an event without a speaker. No speaker is represented as the empty string.
     * The users in attendance is defaulted to an ArrayList.
     * @param eventID The unique event ID
     * @param eventTime The time an event takes place
     * @param eventDuration The duration of time an event takes place
     * @param eventTitle The title of the event
     */
    public Event(int eventID, int eventTime, int eventDuration, String eventTitle){
        this.eventID = eventID;
        this.eventTime = eventTime;
        this.eventDuration = eventDuration;
        this.eventTitle = eventTitle;
        this.speakerUsernames = new ArrayList<>();
        this.usersInAttendance = new ArrayList<>();
        this.restrictions = new HashMap<>();
        this.requirements = new HashMap<>();
    }

    /**
     * Get the unique event ID.
     * @return The eventID int
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Get the time the event takes place.
     * @return The eventTime int
     */
    public int getEventTime() {
        return eventTime;
    }

    /**
     * Get the duration of time an event takes place.
     * @return The eventDuration int
     */
    public int getEventDuration() {
        return eventDuration;
    }

    /**
     * Get the title of this event.
     * @return The eventTitle string
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Get the usernames of the Speakers of this event.
     * @return The speakerUsername string
     */
    public List<String> getSpeakerUsernames() {
        return speakerUsernames;
    }

    /**
     * Sets a list of usernames for the Speakers of this event.
     * @param usernames A list of usernames
     */
    public void setSpeakerUsernames(List<String> usernames){this.speakerUsernames = usernames;}
    /**
     * Gets the list of users in attendance of the event.
     * @return The list of users
     */
    public List<String> getUsersInAttendance() {
        return usersInAttendance;
    }

    /**
     * Gets the list of the roomIDs the event is held in.
     * @return The list of roomIDs
     */
    public int getRoom() { return this.roomID; }

    /**
     * Sets the users in attendance.
     * @param usernames a list of usernames
     */
    public void setUsersInAttendance(List<String> usernames) {this.usersInAttendance = usernames;}

    /**
     * Sets the roomID of the Room this event is being held at.
     * @param roomID the IDs of the Room
     */
    public void addRoom(int roomID) { this.roomID = roomID; }

    /**
     * Returns the HashMap of restrictions.
     * @return Restrictions a list of restrictions
     */
    public Map<String, List<String>> getRestrictions(){
        return this.restrictions;
    }

    /**
     * Sets the restriction of the Event.
     * @param restrictions the restrictions
     */
    public void setRestrictions(Map<String, List<String>> restrictions){this.restrictions = restrictions;}

    /**
     * Returns the requirements of the Event.
     * @return Requirements of the Event
     */
    public Map<String, List<String>> getRequirements() { return requirements; }

    /**
     * Sets the requirements of the Event.
     * @param requirements of the Event
     */
    public void setRequirements(Map<String, List<String>> requirements) {this.requirements = requirements;}
}
