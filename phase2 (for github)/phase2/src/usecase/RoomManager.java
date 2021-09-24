package usecase;

import entity.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomManager implements Serializable, Savable {

    private List<Room> rooms;
    private int currentRoomID;

    public RoomManager() {
        this.rooms = new ArrayList<>();
        this.currentRoomID = 0;
    }

    public RoomManager(List<Room> rooms) {
        this.rooms = rooms;
        this.currentRoomID = rooms.size();
    }

    /**
     * Returns the list of available Rooms.
     * @return returns a list of Room entities.
     */
    public List<Room> getRooms(){ return rooms; }

    /**
     * Returns a list of rooms to be used in the GUI.
     * @return returns a list of Room IDs, and their features
     */
    public List<String> getRoomsForGUI() {
        ArrayList<String> roomsList = new ArrayList<>();
        for (Room room: this.rooms) {
            String featureString;
            if (room.getFormattedFeatures().equals("")){
                featureString = "";
            }
            else{
                featureString = room.getFormattedFeatures().substring(0, room.getFormattedFeatures().length() - 2);
            }
            roomsList.add(
                    "ID: " + room.getRoomID() +
                    "\nCapacity: " + room.getRoomCapacity() +
                    "\nFeatures: " + featureString
            );
        }
        return roomsList;
    }

    /**
     * Check if the Room is already in the list.
     * @param room The Room being checked
     * @return True iff the Room already exists
     */
    public boolean checkRoomExists (Room room) {
        return this.rooms.contains(room);
    }

    /**
     * Check if the Room is already in the list by its ID.
     * @param roomID The ID of the Room being checked
     * @return True iff the Room already exists
     */
    public boolean checkRoomExists (int roomID) {
        Room room = getRoomByID(roomID);
        return checkRoomExists(room);
    }

    /**
     * Get a Room by its ID.
     * @param roomID The ID of the Room desired
     * @return The Room iff it exists; else returns null
     */
    public Room getRoomByID(int roomID) {
        for (Room room : this.rooms) {
            if (room.getRoomID() == roomID) {
                return room;
            }
        }
        return null;
    }

    /**
     * Add the Room to the list.
     * @param room The Room to be added to the list
     * @return True iff the Room is successfully added to the list
     */
    public boolean addRoom(Room room) {
        if (checkRoomExists(room)) {
            return false;
        }
        return this.rooms.add(room);
    }

    /**
     * Returns the most recently generated Room.
     * @return The last Room of rooms; null iff rooms is empty
     */
    public Room getMostRecentlyGeneratedRoom() {
        if (this.rooms.isEmpty()) {
            return null;
        }
        else{
            return this.rooms.get(this.rooms.size()-1);
        }
    }

    /**
     * Generates a new Room to the list of available Rooms.
     * @param roomCapacity The maximum number of Users in this Room
     * @return True iff a Room is added. Return false otherwise
     */
    public boolean generateRoom(int roomCapacity) {
        // A Room should have higher than 0 capacity
        if (roomCapacity <= 0) {
            return false;
        }
        int temp = currentRoomID;
        currentRoomID += 1;
        return addRoom(new Room(temp, roomCapacity));
    }

    /**
     * Add the Speaker to the Room at a specific time.
     * @param room The Room to add the Speaker in the specific time
     * @param time The specific time in which the Speaker will be in the Room
     * @param speaker The speaker to be added
     * @return True iff the Speaker is successfully added to the Room at the time
     */
    public boolean addSpeakerToRoom(Room room, int time, SpeakerData speaker) {
        // The Room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }
        // There is already a Speaker at the time
        if (room.checkSpeakerExistsAtTime(time)) {
            return false;
        }
        room.addSpeaker(time, speaker.getUsername());
        return true;
    }

    /**
     * Add the Speaker to the Room by ID at a specific time.
     * @param roomID The ID of the Room to add the Speaker in the specific time
     * @param time The specific time in which the Speaker will be in the Room
     * @param speaker The speaker to be added
     * @return True iff the Speaker is successfully added to the Room at the time
     */
    public boolean addSpeakerToRoom(int roomID, int time, SpeakerData speaker) {
        Room room = getRoomByID(roomID);
        return addSpeakerToRoom(room, time, speaker);
    }

    /**
     * Remove the Speaker from the Room at a specific time.
     * @param room The Room to add the Speaker in the specific time
     * @param time The specific time in which the Speaker will be in the Room
     * @param speaker The speaker to be added
     * @return True iff the Speaker is successfully added to the Room at the time
     */
    public boolean removeSpeakerFromRoom(Room room, int time, SpeakerData speaker) {
        // The Room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }
        // There is no Speaker at the time
        if (!room.checkSpeakerExistsAtTime(time)) {
            return false;
        }
        room.removeSpeaker(time, speaker.getUsername());
        return true;
    }

    /**
     * Remove the Speaker from the Room at a specific time.
     * @param roomID The room ID of the room to add the Speaker in the specific time
     * @param time The specific time in which the Speaker will be in the Room
     * @param speaker The speaker to be added
     * @return True iff the Speaker is successfully added to the Room at the time
     */
    public boolean removeSpeakerFromRoom(int roomID, int time, SpeakerData speaker) {
        Room room = getRoomByID(roomID);
        return removeSpeakerFromRoom(room, time, speaker);
    }

    /**
     * Add the Attendee to the Room at a specific time.
     * @param room The Room to add the Attendee in the specific time
     * @param time The specific time in which the Attendee will be in the Room
     * @param user The Attendee to be added
     * @return True iff the Attendee is successfully added to the Room at the time
     */
    public boolean addUserToRoom(Room room, int time, UserData user) {
        // The Room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }
        // The Room is already full
        if (room.getNumAttendeeAtTime(time) >= room.getRoomCapacity()) {
            return false;
        }
        room.addAttendee(time, user.getUsername());
        return true;
    }

    /**
     * Add the Attendee to the Room at a specific time.
     * @param roomID The ID of the Room to add the Attendee in the specific time
     * @param time The specific time in which the Attendee will be in the Room
     * @param user The Attendee to be added
     * @return True iff the Attendee is successfully added to the Room at the time
     */
    public boolean addUserToRoom(int roomID, int time, UserData user) {
        Room room = getRoomByID(roomID);
        return addUserToRoom(room, time, user);
    }

    public boolean removeUserInRoom(Room room, int time, UserData user) {
        // The Room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }

        // The user is not in the room at the time
        if (!room.getAttendeeSchedule().get(time).contains(user.getUsername())) {
            return false;
        }

        else {
            room.getAttendeeSchedule().get(time).remove(user.getUsername());
            return true;
        }
    }

    /**
     * Add the Event to the Room at the specific time.
     * @param room The Room to add the Attendee in the specific time
     * @param event The Event to be added
     * @return True iff the Event is successfully added to the Room at the time
     */
    public boolean addEventToRoom(Room room, Event event) {
        // The Room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }
        // There is already an Event at the time
        if (room.checkEventExistsAtTime(event.getEventTime(), event.getEventDuration())) {
            return false;
        }

        // The Room does not meet the event requirements
        if (!room.checkRoomMeetsRequirements(event)) {
            return false;
        }

        room.addEvent(event.getEventTime(), event.getEventID(), event.getEventDuration());
        return true;
    }

    /**
     * Add the Event to the Room at the specific time.
     * @param roomID The ID of the Room to add the Event in the specific time
     * @return True iff the Event is successfully added to the Room at the time
     */
    public boolean addEventToRoom(int roomID, Event event) {
        Room room = getRoomByID(roomID);
        // The Room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }
        // There is already an Event at the time
        if (room.checkEventExistsAtTime(event.getEventTime(), event.getEventDuration())) {
            return false;
        }

        // The Room does not meet the event requirements
        if (!room.checkRoomMeetsRequirements(event)) {
            return false;
        }

        room.addEvent(event.getEventTime(), event.getEventID(), event.getEventDuration());
        return true;
    }

    /**
     * Get a Room's capacity by its the event ID and the time event takes place
     * @param eventID the ID of the event
     * @param time The start time of the event
     * @return The Room's capacity with the event ID at that time
     */
    public Room getRoomByEventIDAndTime(int eventID, int time, int dur){
        for (Room room : this.rooms){
            if(room.getEventSchedule().get(time) != null){
                if (room.getEventSchedule().get(time).getKey() == eventID){ //(time, (eventID, Duration))
                    return room;
                }
            }

        }
        return null;
    }

    /**
     * Check if a Room meets the requirements for an event
     * @param room The Room being checked
     * @param event The event with the requirements being checked
     * @return True iff the Room meets the requirements; else returns false
     */
    public boolean checkRoomMeetsRequirements (Room room, Event event) {
        return room.checkRoomMeetsRequirements(event);
    }

    /**
     * Returns a list of Rooms with Room Info which meet the requirements for an event
     * @param event The event with requirements
     * @return An ArrayList of rooms and their information
     */
    public List<String> getRecommendedRooms (Event event) {
        List<String> recommendedRooms = new ArrayList<>();
        for (Room room: rooms) {
            if (checkRoomMeetsRequirements(room, event)) {
                String featureString;
                if (room.getFormattedFeatures().equals("")){
                    featureString = "";
                }
                else{
                    featureString = room.getFormattedFeatures().substring(0, room.getFormattedFeatures().length() - 2);
                }
                recommendedRooms.add(
                        "ID: " + room.getRoomID() +
                                "\nCapacity: " + room.getRoomCapacity() +
                                "\nFeatures: " + featureString
                );
            }
        }
        return recommendedRooms;
    }
}
