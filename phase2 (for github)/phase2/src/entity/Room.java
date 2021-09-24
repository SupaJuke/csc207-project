package entity;

import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {

    private final int roomID;
    private final int roomCapacity;
    private final Map<Integer, String> speakerSchedule;
    private final Map<Integer, List<String>> attendeeSchedule;
    private final Map<Integer, Map.Entry<Integer, Integer>> eventSchedule;
    private final Map<String, List<String>> features;

    /**
     * Constructs a new, empty room.
     * @param roomID The unique ID of the room
     * @param roomCapacity The capacity of Attendees in the room
     */
    public Room(int roomID, int roomCapacity) {
        this.roomID = roomID;
        this.roomCapacity = roomCapacity;
        this.speakerSchedule = new HashMap<>();
        this.attendeeSchedule = new HashMap<>();
        this.eventSchedule = new HashMap<>();
        this.features = new HashMap<>();

    }

    /**
     * Constructs a room with existing data.
     * @param roomID The unique ID of the room
     * @param roomCapacity The capacity of Attendees in the room
     * @param speakerSchedule The map between time and Speakers
     * @param attendeeSchedule The map between time and Attendees
     * @param eventSchedule The map Between time and event: duration
     */
    public Room(int roomID, int roomCapacity,
                Map<Integer, String> speakerSchedule,
                Map<Integer, List<String>> attendeeSchedule,
                Map<Integer, Map.Entry<Integer, Integer>> eventSchedule,
                Map<String, List<String>> features) {
        this.roomID = roomID;
        this.roomCapacity = roomCapacity;
        this.speakerSchedule = speakerSchedule;
        this.attendeeSchedule = attendeeSchedule;
        this.eventSchedule = eventSchedule;
        this.features = features;
    }

    /**
     * Get the unique ID of the Room.
     * @return The roomID int
     */
    public int getRoomID() { return roomID; }

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
     * Get the eventSchedule of the Room.
     * @return The eventSchedule of the Room
     */
    public Map<Integer, Map.Entry<Integer, Integer>> getEventSchedule() { return this.eventSchedule; }

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
    public boolean checkEventExistsAtTime(int time, int duration) {
        int end = time+duration;
        for (Map.Entry<Integer, Map.Entry<Integer, Integer>> entry : this.eventSchedule.entrySet()) {
            int tempStart = entry.getKey();
            int tempEnd = entry.getValue().getValue() + tempStart;
            if (((tempStart < time) && (time < tempEnd)) || ((tempStart < end) && (end < tempEnd))) {
                return true;
            }
        }
        return false;
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
     * Add a username of a Speaker to this room in the specific time.
     * @param time The time the Speaker is in the room
     * @param username The username of the Speaker added on a specific time
     */
    public void removeSpeaker(int time, String username) { this.speakerSchedule.remove(time, username); }

    /**
     * Add an eventID of an Event to this room in the specific time.
     * @param time The time the Speaker is in the room
     * @param eventID The eventID of the Event added on a specific time
     */
    public void addEvent(int time, int eventID, int duration) {
        this.eventSchedule.put(time, new AbstractMap.SimpleEntry<>(eventID, duration));
    }

    /**
     * Get a string containing a list of features separated by commas
     * @return String containing a list of features separated by commas
     */
    public String getFormattedFeatures() {
        StringBuilder featureList = new StringBuilder();
        for(String featureType: features.keySet()) {
            for (String feature : features.get(featureType)) {
                featureList.append(feature).append(", ");
            }
        }

        if(featureList.toString().equals(""))
            return featureList.toString();
        return featureList.toString().replace("None, ", "");
    }

    /**
     * Add a feature to this room
     * @param featureType The type of feature
     * @param feature The feature being added
     * @return True iff the feature was added, else return False
     */
    public boolean addElementToFeatures(String featureType, String feature) {
        if (checkFeatureTypeExists(featureType)) {
            if (checkRoomHasFeature(featureType, feature)) {
                return false;
            }
            this.features.get(featureType).add(feature);
            return true;
        }
        this.features.put(featureType, new ArrayList<>(Collections.singletonList(feature)));
        return true;
    }

    /**
     * Check if this room already has a feature
     * @param featureType The type of feature
     * @param feature The feature being added
     * @return True iff the room has the feature, else return False
     */
    public boolean checkRoomHasFeature(String featureType, String feature) {
        if(checkFeatureTypeExists(featureType)) {
            return this.features.get(featureType).contains(feature);
        }
        return false;
    }

    /**
     * Check if this room contains a feature type
     * @param featureType The type of feature
     * @return True iff the feature type exists, else return False
     */
    public boolean checkFeatureTypeExists(String featureType) {
        return this.features.containsKey(featureType);
    }

    /**
     * Check if a room meets the requirements for an event
     * @param event The event with requirements
     * @return True iff the event meets the requirements, else return False
     */
    public boolean checkRoomMeetsRequirements (Event event) {
        Map <String, List<String>> requirements = event.getRequirements();

        for (String requirementType: requirements.keySet()) {
            if (!this.checkFeatureTypeExists(requirementType)) { return false; }
            // Feature type is in the map features
            for (String requirement: requirements.get(requirementType)) {
                if (!this.checkRoomHasFeature(requirementType, requirement)) { return false; }
            }
        }
        return true;
    }
}
