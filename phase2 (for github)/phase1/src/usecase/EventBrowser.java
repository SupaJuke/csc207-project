package usecase;

import entity.Event;
import entity.UserData;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;

public class EventBrowser implements Serializable {

    private ArrayList<Event> schedule;

    public EventBrowser(){
        schedule = new ArrayList<>();
    }
    /**
     * Return a list of events in the schedule.
     * @return schedule
     */
    public ArrayList<Event> browseEvents() {
        return schedule;
    }

    /**
     * Returns a list of events with their event ID and name.
     * @return returns a list of events
     */
    public ArrayList<String> getScheduleInfo(){
        ArrayList<String> events = new ArrayList<>();
        for (Event event : schedule){
            String eventinfo = String.format("[%d] - %s", event.getEventID(), event.getEventTitle());
            events.add(eventinfo);
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
     * Cancel an event from the schedule
     * @param event The event that gets removed
     * @return True iff the event was removed, else return False
     */
    public boolean cancel(Event event) {
        return(schedule.remove(event));
    }

    /**
     * Gives a list of event info for an event
     * @param eventID The eventID of the event
     * @return A list containing the event's title, time, and speaker name if the event exists. Returns an empty list
     * otherwise.
     */
    public ArrayList<Object> getEventInfo(int eventID) {
        for (Event event: schedule) {
            if (event.getEventID() == eventID)
                return new ArrayList<>(Arrays.asList(event.getEventTitle(),
                        event.getEventTime(), event.getSpeakerUsernames()));
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
        return null;
    }

    /**
     * Returns the list of users that are attending an event given an EventID
     * @param eventID The EventID of the event
     * @return An arraylist containing the usernames of all the users in attendance
     */
    public ArrayList<String> getUsersFromEvent(int eventID) {
        Event event = getEventFromID(eventID);
        return (ArrayList<String>) event.getUsersInAttendance();
    }

    /**
     * Returns the list of users that are attending an event given an event entity
     * @param event The event entity
     * @return An arraylist containing the usernames of all the users in attendance
     */
    public ArrayList<String> getUsersFromEvent(Event event) {
        return (ArrayList<String>) event.getUsersInAttendance();
    }

    /**

    /**
     * Checks if an event is in the schedule.
     * @param eventID The ID of the event.
     * @return True iff the event is in the schedule, else return False
     */
    public boolean checkEventExists(int eventID) {
        return schedule.contains(getEventFromID(eventID));
    }

}
