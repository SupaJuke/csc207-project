package usecase;

import entity.Event;
import entity.UserData;

import java.io.Serializable;
import java.util.*;


public class EventManager implements Serializable, Savable {

    private final List<Event> schedule;
    private int currentEventID = 0; 


    /**
     * Constructs an EventManager. Defaulted to an ArrayList.
     */
    public EventManager() {
        schedule = new ArrayList<>();
    }

    /**
     * Constructs an EventManager with a predetermined list.
     */
    public EventManager(List<Event> schedule) {
        this.schedule = schedule;
    }

    /**
     * Checks if the user is already attending an event with overlapping time
     * @param user The user being added to an event
     * @param event The Event
     * @return True iff the user is already attending one; else return False
     */
    public boolean checkAlreadyAttendingEvent(UserData user, Event event) {
        int eventStart = (int) getEventInfo(event).get(1);
        int eventEnd = (int) getEventInfo(event).get(2);
        for (int attending : user.getEventIDs()){
            int attendingStart = (int) getEventInfo(attending).get(1);
            int attendingEnd = (int) getEventInfo(attending).get(2);
            // Check if the start or end is in others
            if (((attendingStart <= eventStart) && (eventStart <= attendingEnd)) ||
                    ((attendingStart <= eventEnd) && (eventEnd <= attendingEnd))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the user is already attending an event with overlapping time
     * @param user The user being added to an event
     * @param eventID The eventID of the Event
     * @return True iff the user is already attending one; else return False
     */
    public boolean checkAlreadyAttendingEvent(UserData user, int eventID) {
        return checkAlreadyAttendingEvent(user, getEventFromID(eventID));
    }

    /**
     * Adds a new event ID to the list of event ID iff the event ID
     * is not already in the list of event ID of the associated user
     * @param eventID The unique ID of the new event
     * @return True iff the event ID was added successfully
     */
    public boolean addEventID(UserData user, int eventID) {
        // User already attending other events (overlapping)
        if (checkAlreadyAttendingEvent(user, eventID)) {
            return false;
        }
        List<String> userlist = getEventFromID(eventID).getUsersInAttendance();
        // Add username to Event
        userlist.add(user.getUsername());
        getEventFromID(eventID).setUsersInAttendance(userlist);
        return user.addEventID(eventID);
    }

    /**
     * Generates an event with a speaker name and an empty list of attendees and adds it to the schedule
     * @param name The name of the event
     * @param time The time of the event
     * @param duration The duration of the event
     * @param speaker The username of the speaker
     */
    public boolean generateEvent(String name, int time, int duration, String speaker){
        int temp = currentEventID;
        ArrayList<String> speakers = new ArrayList<>();
        speakers.add(speaker);
        Event event = new Event(temp, time, duration, name, speakers, new ArrayList<>(),
                new HashMap<>(), new HashMap<>());
        if(signUp(event)) {
            currentEventID += 1;
            return(addSpeaker(event.getEventID(), speaker));
        }
        return false;
    }

    /**
     * Generates an event and adds it to the schedule without a speaker or list of attendees
     * @param name The name of the event
     * @param time The time of the event
     * @param duration The duration of the event
     */
    public boolean generateEvent(String name, int time, int duration) {
        int temp = currentEventID;
        currentEventID += 1;
        return signUp(new Event(temp, time, duration, name));
    }

    /**
     * Removes an event from the schedule given an event ID
     * @param eventID The EventID of the event
     * @return True iff the event was removed, else return False
     */
    public boolean removeEvent(int eventID) {
        Event event = getEventFromID(eventID);
        if (event.getEventID() == -1) {
            return false;
        }
        return schedule.remove(event);
    }
    /**
     * Removes an event from the schedule given an event ID
     * @param event The event entity
     * @return True iff the event was removed, else return False
     */
    public boolean removeEvent(Event event) {
        return schedule.remove(event);
    }

    /**
     * Adds a speaker to an event given an EventID
     * @param event The Event
     * @param speakerID The username of the speaker
     * @return True iff the speaker was added, else return false
     */
    public boolean addSpeaker(Event event, String speakerID) {
        if (!(schedule.contains(event))) {
            return false;
        }
        List<String> speakerNames = event.getSpeakerUsernames();
        speakerNames.add(speakerID);
        event.setSpeakerUsernames(speakerNames);
        //addUserToEvent(event, speakerID);
        return true; }

    /**
     * Adds a speaker to an event given an Event entity
     * @param eventID The eventID of the Event
     * @param speakerID The username of the speaker
     * @return True iff the speaker was added, else return false
     */
    public boolean addSpeaker(int eventID, String speakerID) {
        return addSpeaker(getEventFromID(eventID), speakerID);
    }


    /**
     * Removes a speaker from an event given an event ID
     * @param eventID the eventID of the event
     * @param speakerID the username of the speaker
     * @return True iff the speaker was removed, else return false
     */
    public boolean removeSpeaker(int eventID, String speakerID){
        Event event = getEventFromID(eventID);
        List<String> speakerNames = event.getSpeakerUsernames();
        if (speakerNames.remove(speakerID)) {
            event.setSpeakerUsernames(speakerNames);
            return true;
        }
        return false;
    }

    /**
     * Removes a speaker from an event given an event ID
     * @param event the Event
     * @param speakerID the username of the speaker
     * @return True iff the speaker was removed, else return false
     */
    public boolean removeSpeaker(Event event, String speakerID) {
        return removeSpeaker(event.getEventID(), speakerID);
    }

    /**
     * Returns a list of events with their event ID and name.
     * @return returns a list of events
     */
    public List<String> getScheduleInfo() {
        ArrayList<String> events = new ArrayList<>();
        for (Event event : schedule){
            String eventInfo = String.format("[%d] - %s", event.getEventID(), event.getEventTitle());
            events.add(eventInfo);
        }
        return events;
    }
    /**
     * Sign up an event to the schedule
     * @param event The event that gets added
     * @return True iff the event gets added, else return False
     */
    public boolean signUp(Event event) {
        if (!schedule.contains(event)) {
            schedule.add(event);
            return true;
        }
        return false;
    }

    /**
     * Gives a list of event info for an event
     * @param eventID The eventID of the event
     * @return A list containing the event's title, time, and speaker name if the event exists. Returns an empty list
     * otherwise.
     */
    public List<Object> getEventInfo(int eventID) {
        for (Event event: schedule) {
            if (event.getEventID() == eventID)
                return new ArrayList<>(Arrays.asList(
                        event.getEventTitle(),
                        event.getEventTime(),
                        event.getEventDuration() + event.getEventTime(),
                        event.getSpeakerUsernames()));
        }
        return new ArrayList<>();
    }

    /**
     * Gives a list of event info for an event
     * @param event The Event
     * @return A list containing the event's title, time, and speaker name if the event exists. Returns an empty list
     * otherwise.
     */
    public List<Object> getEventInfo(Event event) {
        int eventID = event.getEventID();
        for (Event ignored : schedule) {
            if (event.getEventID() == eventID)
                return new ArrayList<>(Arrays.asList(
                        event.getEventTitle(),
                        event.getEventTime(),
                        event.getEventDuration() + event.getEventTime(),
                        event.getSpeakerUsernames()));
        }
        return new ArrayList<>();
    }

    /**
     * Returns an event given an eventID from the schedule
     * @param eventID The ID of the event
     * @return An event if it is found, else return null
     */
    public Event getEventFromID(int eventID) {
        for (Event event: schedule) {
            if (event.getEventID() == eventID) {
                return event;
            }
        }
        return new Event();
    }

    /**
     * Returns the list of users that are attending an event given an EventID
     * @param eventID The EventID of the event
     * @return An arraylist containing the usernames of all the users in attendance
     */
    public List<String> getUsersFromEvent(int eventID) {
        return getUsersFromEvent(getEventFromID(eventID));
    }

    /**
     * Returns the list of users that are attending an event given an event entity
     * @param event The event entity
     * @return An arraylist containing the usernames of all the users in attendance
     */
    public List<String> getUsersFromEvent(Event event) {
        return event.getUsersInAttendance();
    }

    /**
     * Adds a user to an event.
     * @param event the Event the user is being added to
     * @param username the username of the user
     * @return True iff the user was added to the event.
     */
    public boolean addUserToEvent(Event event, String username){
        List<String> userlist = event.getUsersInAttendance();
        if (!userlist.contains(username)){
            userlist.add(username);
            event.setUsersInAttendance(userlist);
            return true;
        }
        return false;
    }
    public boolean addUserToEvent(int eventID, String username){
        Event event = getEventFromID(eventID);
        return addUserToEvent(event, username);
    }

    /**
     * Removes a user from an event
     * @param eventID The ID of the event
     * @param username The username of the user
     */
    public void removeUserFromEvent(int eventID, String username){
        Event event = getEventFromID(eventID);
        List<String> userList = event.getUsersInAttendance();
        if (userList.remove(username)){
            event.setUsersInAttendance(userList);
        }
    }

     /**
     * Checks if an event is in the schedule.
     * @param eventID The ID of the event.
     * @return True iff the event is in the schedule, else return False
     */
    public boolean checkEventExists(int eventID) {
        return schedule.contains(getEventFromID(eventID));
    }

    /**
     * A getter for the event schedule
     * @return returns schedule
     */
    public List<Event> browseEvents() { return schedule; }

    /**
     * Adds a requirement to the list of requirements of an event
     * @param event the event
     * @param requirementType the type of requirement
     * @param requirement the requirement
     */
    public void addElementToRequirements(Event event, String requirementType, String requirement) {
        Map<String, List<String>> newReqs = event.getRequirements();
        newReqs.putIfAbsent(requirementType, new ArrayList<>());
        newReqs.get(requirementType).add(requirement);
        event.setRequirements(newReqs);
    }

    /**
     * Adds a list of requirements to the list of requirements of an event
     * @param event the event
     * @param requirementType the type of requirement
     * @param requirements the list of requiements
     */
    public void addElementToRequirements(Event event, String requirementType, ArrayList<String> requirements) {
        Map<String, List<String>> newReqs = event.getRequirements();
        newReqs.putIfAbsent(requirementType, requirements);
        for (String requirement: requirements) {
            if(!requirements.contains(requirement)) {
                newReqs.get(requirementType).add(requirement);
            }
        }
        event.setRequirements(newReqs);
    }

    /**
     * A getter for the list of events that a user is attending
     * @param UD the user
     * @return returns the list of eventIDs of the events that the user is attending
     */
    public List<String> getListOfUserEvents(UserData UD) {
        List<String> events = new ArrayList<>();
        for (int eventID : UD.getEventIDs()) {
            String eventInfo = String.format("[%d] - %s", eventID, getEventFromID(eventID).getEventTitle());
            events.add(eventInfo);
        }
        return events;
    }
}
