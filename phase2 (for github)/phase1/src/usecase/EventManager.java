package usecase;

import entity.Event;
import entity.UserData;
import gateway.LocalFileGateway;

import java.io.Serializable;
import java.util.ArrayList;


public class EventManager implements Serializable {

    private EventBrowser eventBrowser;
    private int currentEventID = 0;


    /**
     * Constructs an EventManager.
     * @param eventBrowser The EventBrowser for this conference
     */
    public EventManager (EventBrowser eventBrowser) {
        this.eventBrowser = eventBrowser;

    }

    /**
     * Adds a new event ID to the list of event ID iff the event ID
     * is not already in the list of event ID of the associated user
     * @param eventID The unique ID of the new event
     * @return True iff the event ID was added successfully
     */
    public boolean addEventID(UserData user, int eventID){
        // If user is already attending the event, return false.
        if (!user.getEventIDs().contains(eventID)){

            return eventBrowser.getEventFromID(eventID).addUserInAttendance(user.getUsername())
                    && user.addEventID(eventID);
        }
        return false;
    }

    /**
     * Generates an event with a speaker name and an empty list of attendees and adds it to the schedule
     * @param name The name of the event
     * @param time The time of the event
     * @param speaker The username of the speaker
     */
    public boolean generateEvent(String name, int time, String speaker){
        int temp = currentEventID;
        ArrayList<String> speakers = new ArrayList<>();
        speakers.add(speaker);
        Event event = new Event(temp, time, name, speakers, new ArrayList<>());
        if(eventBrowser.signUp(event)) {
            currentEventID += 1;
            return(addSpeaker(event.getEventID(), speaker));
        }
        return false;
    }

    /**
     * Generates an event and adds it to the schedule without a speaker or list of attendees
     * @param name The name of the event
     * @param time The time of the event
     */
    public boolean generateEvent(String name, int time) {
        int temp = currentEventID;
        currentEventID += 1;
        return eventBrowser.signUp(new Event(temp, time, name));
    }

    /**
     * Removes an event from the schedule given an event ID
     * @param eventID The EventID of the event
     * @return True iff the event was removed, else return False
     */
    public boolean removeEvent(int eventID) {
        if (eventBrowser.getEventFromID(eventID) == null){
            return false;
        }
        return eventBrowser.cancel(eventBrowser.getEventFromID(eventID));
    }
    /**
     * Removes an event from the schedule given an event ID
     * @param event The event entity
     * @return True iff the event was removed, else return False
     */
    public boolean removeEvent(Event event) {
        return eventBrowser.cancel(event);
    }

    /**
     * Adds a speaker to an event given an EventID
     * @param eventID The EventID of the event
     * @param speakerID The username of the speaker
     * @return True iff the speaker was added, else return false
     */
    public boolean addSpeaker(int eventID, String speakerID) {
        if (eventBrowser.getEventFromID(eventID) == null){
            return false;
        }
        return this.addSpeaker(eventBrowser.getEventFromID(eventID), speakerID);
    }

    /**
     * Adds a speaker to an event given an Event entity
     * @param event The event entity
     * @param speakerID The username of the speaker
     * @return True iff the speaker was added, else return false
     */
    public boolean addSpeaker(Event event, String speakerID) {
        return event.addSpeakerUsername(speakerID);
    }

    /**
     * Removes a speaker from an event given an event ID
     * @param eventID the eventID of the event
     * @param speakerID the username of the speaker
     * @return True iff the speaker was removed, else return false
     */
    public boolean removeSpeaker(int eventID, String speakerID){
        Event event = eventBrowser.getEventFromID(eventID);
        if (event.getSpeakerUsernames().contains(speakerID)) {
            return event.removeSpeakerUsername(speakerID);
        }
        return false;
    }
    /**
     * Check if the event exists
     * @param eventID The event ID to be checked for existence
     * @return True iff the event exists.
     */
    public boolean checkEventExists(int eventID){
        return this.eventBrowser.checkEventExists(eventID);
    }

    public EventBrowser getEventBrowser() {
        return eventBrowser;
    }
}
