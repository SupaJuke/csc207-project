package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements Serializable {

    private int roomID;
    private int roomCapacity;
    private Map<Integer, String> speakerSchedule;
    private Map<Integer, List<String>> attendeeSchedule;
    private Map<Integer, Integer> eventSchedule;

    /**
     * Constructs a new, empty room.
     * @param roomID The unique ID of the room
     * @param roomCapacity The capacity of Attendees in the room
     */
    public Room(int roomID, int roomCapacity){
        this.roomID = roomID;
        this.roomCapacity = roomCapacity;
        this.speakerSchedule = new HashMap<Integer, String>();
        this.attendeeSchedule = new HashMap<Integer, List<String>>();
        this.eventSchedule = new HashMap<Integer, Integer>();
    }

    /**
     * Constructs a room with existing data.
     * @param roomID The unique ID of the room
     * @param roomCapacity The capacity of Attendees in the room
     * @param speakerSchedule The map between time and Speakers
     * @param attendeeSchedule The map between time and Attendees
     * @param eventSchedule The map Between time and Events
     */
    public Room(int roomID, int roomCapacity,
                Map<Integer, String> speakerSchedule,
                Map<Integer, List<String>> attendeeSchedule,
                Map<Integer, Integer> eventSchedule) {
        this.roomID = roomID;
        this.roomCapacity = roomCapacity;
        this.speakerSchedule = speakerSchedule;
        this.attendeeSchedule = attendeeSchedule;
        this.eventSchedule = eventSchedule;
    }

    /**
     * Get the unique ID of the Room.
     * @return The roomID int
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * Get the attendance capacity of the Room.
     * @return The roomCapacity int
     */
    public int getRoomCapacity() {
        return roomCapacity;
    }

    /**
     * Get the attendeeSchedule of the Room.
     * @return The attendeeSchedule of the Room
     */
    public Map<Integer, List<String>> getAttendeeSchedule() { return this.attendeeSchedule; }

    /**
     * Get the speakerSchedule of the Room.
     * @return The speakerSchedule of the Room
     */
    public Map<Integer, String> getSpeakerSchedule() { return this.speakerSchedule; }

    /**
     * Get the eventSchedule of the Room.
     * @return The eventSchedule of the Room
     */
    public Map<Integer, Integer> getEventSchedule() { return this.eventSchedule; }

    /**
     * Get the number of Attendees of the room at the time
     * @param time The specific time
     * @return Number of Attendees in the room at time
     */
    public int getNumAttendeeAtTime(int time) {
        if (!checkAttendeeExistsAtTime(time)) {
            return 0;
        }
        else {
            return attendeeSchedule.get(time).size();
        }
    }

    /**
     * Check if there is an Attendee at the time.
     * @param time The specific time to look for Attendees
     * @return True iff there is an Attendee at the time
     */
    public boolean checkAttendeeExistsAtTime(int time) {
        return this.attendeeSchedule.containsKey(time);
    }

    // Not really necessary but may be used for other classes
    /**
     * Check if there is a Speaker at the time.
     * @param time The specific time to look for a Speaker
     * @return True iff there is a Speaker at the time
     */
    public boolean checkSpeakerExistsAtTime(int time) {
        return this.speakerSchedule.containsKey(time);
    }

    /**
     * Check if there is an Event at the time.
     * @param time The specific time to look for an Event
     * @return True iff there is an Event at the time
     */
    public boolean checkEventExistsAtTime(int time) {
        return this.eventSchedule.containsKey(time);
    }

    /**
     * Add a username of an Attendee to the room in the specific time.
     * @param time The time the Attendee is in the room
     * @param username The username of the Attendee added on a specific time
     */
    public void addAttendee(int time, String username) {
        // Someone else is here at the time
        if (checkAttendeeExistsAtTime(time)) {
            this.attendeeSchedule.get(time).add(username);
        }

        // None is here at the time
        else {
            List<String> temp = new ArrayList<>();
            temp.add(username);
            this.attendeeSchedule.put(time, temp);
        }
    }

    /**
     * Add a username of a Speaker to this room in the specific time.
     * @param time The time the Speaker is in the room
     * @param username The username of the Speaker added on a specific time
     */
    public void addSpeaker(int time, String username) { this.speakerSchedule.put(time, username); }

    /**
     * Add an eventID of an Event to this room in the specific time.
     * @param time The time the Speaker is in the room
     * @param eventID The eventID of the Event added on a specific time
     */
    public void addEvent(int time, int eventID) { this.eventSchedule.put(time, eventID); }

    /**
     * Clear a timeslot of the Room.
     * @param time The desired timeslot
     */
    public void clearSlot(int time) {
        if (eventSchedule.containsKey(time)) {
            this.speakerSchedule.remove(time);
            this.attendeeSchedule.remove(time);
            this.eventSchedule.remove(time);
        }
    }
}
