package usecase;

import entity.AttendeeData;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class AttendeeManager implements Serializable, Savable {

    private final List<AttendeeData> attendees;

    /**
     * Constructs an AttendeeManager with its Attendees predetermined.
     */
    public AttendeeManager() { this.attendees = new ArrayList<>(); }

    /**
     * Constructs an AttendeeManager with its attendees predetermined.
     * @param attendees The list of Attendees
     */
    public AttendeeManager(List<AttendeeData> attendees){
        this.attendees = attendees;
    }

    /**
     * Verify if the Attendee exists in AttendeeManager.
     * @param username The Attendee's username
     * @return True iff the Attendee exists in attendees
     */
    public boolean checkAttendeeExists(String username) {
        for (AttendeeData attendee : this.attendees) {
            if (attendee.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify if the Attendee exists in AttendeeManager.
     * @param attendee The Attendee
     * @return True iff the Attendee exists in attendees
     */
    public boolean checkAttendeeExists(AttendeeData attendee) { return this.attendees.contains(attendee); }

    /**
     * Returns an Attendee's data given the Attendee's username.
     * @param username The Attendee's username
     * @return Returns the Attendee's data iff the Attendee exists in attendees
     */
    public AttendeeData getAttendeeByID(String username) {
        for (AttendeeData attendee : this.attendees) {
            if (attendee.getUsername().equals(username)) {
                return attendee;
            }
        }
        return null;
    }

    /**
     * Add a new Attendee to attendees.
     * @param attendee The new Attendee
     * @return Returns true iff the new Attendee has been added to attendees
     */
    public boolean addAttendee(AttendeeData attendee) {
        // Check if the attendee is already in the list or not
        if (checkAttendeeExists(attendee)) {
            return false;
        }
        attendees.add(attendee);
        return true;
    }

    /**
     * Return a list of usernames of all existing Attendees.
     * @return The list of usernames of all existing Attendees
     */
    public List<String> getAttendeesUsernames() {
        List<String> attendeeNames = new ArrayList<>();

        for (AttendeeData attendee : attendees) {
            attendeeNames.add(attendee.getUsername());
        }

        return attendeeNames;
    }

    /**
     * Return a list of all existing Attendees.
     * @return The list of all existing Attendees
     */
    public List<AttendeeData> getAttendees() {
        return attendees;
    }
}
