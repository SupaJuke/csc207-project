package usecase;

import entity.*;
import gateway.LocalFileGateway;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomManager implements Serializable {

    private List<Room> rooms;
    private int currentRoomID;

    public RoomManager() {
        this.rooms = new ArrayList<Room>();
        this.currentRoomID = 0;
    }

    public RoomManager(List<Room> rooms) {
        this.rooms = rooms;
        this.currentRoomID = rooms.size();
    }

    /**
     * Gets the list of available rooms.
     * @return returns a list of Room entities.
     */
    public List<Room> getRooms(){return rooms; }

    /**
     * Check if the room is already in the list.
     * @param room The room being checked
     * @return True iff the room already exists
     */
    public boolean checkRoomExists (Room room) {
        return this.rooms.contains(room);
    }

    /**
     * Check if the room is already in the list by its ID.
     * @param roomID The ID of the room being checked
     * @return True iff the room already exists
     */
    public boolean checkRoomExists (int roomID) {
        Room room = getRoomByID(roomID);
        return checkRoomExists(room);
    }

    /**
     * Get a room by its ID.
     * @param roomID The ID of the room desired
     * @return The room iff it exists. Return Null otherwise.
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
     * Add the room to the list.
     * @param room The room to be added to the list
     * @return True iff the room is successfully added to the list
     */
    public boolean addRoom(Room room) {
        if (checkRoomExists(room)) {
            return false;
        }
        return this.rooms.add(room);
    }

    /**
     * Generates a new room to the list of available rooms.
     * @param roomCapacity The maximum number of users in this room
     * @return True iff a room is added. Return false otherwise
     */
    public boolean generateRoom(int roomCapacity) {
        // A room should have higher than 0 capacity
        if (roomCapacity <= 0) {
            return false;
        }
        int temp = currentRoomID;
        currentRoomID += 1;
        return addRoom(new Room(temp, roomCapacity));
    }

    /**
     * Add the Speaker to the Room at a specific time.
     * @param room The room to add the Speaker in the specific time
     * @param time The specific time in which the Speaker will be in the room
     * @param speaker The speaker to be added
     * @return True iff the Speaker is successfully added to the room at the time
     */
    public boolean addSpeakerToRoom(Room room, int time, SpeakerData speaker) {
        // The room does not exist
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
     * @param roomID The ID of the room to add the Speaker in the specific time
     * @param time The specific time in which the Speaker will be in the room
     * @param speaker The speaker to be added
     * @return True iff the Speaker is successfully added to the room at the time
     */
    public boolean addSpeakerToRoom(int roomID, int time, SpeakerData speaker) {
        Room room = getRoomByID(roomID);
        return addSpeakerToRoom(room, time, speaker);
    }

    /**
     * Add the Attendee to the Room at a specific time.
     * @param room The room to add the Attendee in the specific time
     * @param time The specific time in which the Attendee will be in the room
     * @param user The Attendee to be added
     * @return True iff the Attendee is successfully added to the room at the time
     */
    public boolean addUserToRoom(Room room, int time, UserData user) {
        // The room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }
        // The room is already full
        if (room.getNumAttendeeAtTime(time) >= room.getRoomCapacity()) {
            return false;
        }
        room.addAttendee(time, user.getUsername());
        return true;
    }

    /**
     * Add the Attendee to the Room at a specific time.
     * @param roomID The ID of the room to add the Attendee in the specific time
     * @param time The specific time in which the Attendee will be in the room
     * @param user The Attendee to be added
     * @return True iff the Attendee is successfully added to the room at the time
     */
    public boolean addUserToRoom(int roomID, int time, UserData user) {
        Room room = getRoomByID(roomID);
        return addUserToRoom(room, time, user);
    }

    /**
     * Add the Event to the Room at the specific time.
     * @param room The Room to add the Attendee in the specific time
     * @param time The specific time in which the Event will be in the room
     * @param event The Event to be added
     * @return True iff the Event is successfully added to the Room at the time
     */
    public boolean addEventToRoom(Room room, int time, Event event) {
        // The room does not exist
        if (!checkRoomExists(room)) {
            return false;
        }
        // There is already an Event at the time
        if (room.checkEventExistsAtTime(time)) {
            return false;
        }
        room.addEvent(time, event.getEventID());
        return true;
    }

    /**
     * Add the Event to the Room at the specific time.
     * @param roomID The ID of the Room to add the Event in the specific time
     * @param time The specific time in which the Event will be in the room
     * @param event The Event to be added
     * @return True iff the Event is successfully added to the Room at the time
     */
    public boolean addEventToRoom(int roomID, int time, Event event) {
        Room room = getRoomByID(roomID);
        return addEventToRoom(room, time, event);
    }

    /**
     * Get a room's capacity by its the event ID and the time event takes place
     * @param eventID the ID of the event
     * @param time The time of the event
     * @return The room's capacity with the event ID at that time
     */
    public Room getRoomByEventIDandTime(int eventID, int time){
        for (Room room : this.rooms){
            if (room.getEventSchedule().get(time) == eventID){
                return room;
            }
        }
        return null;
    }
}
