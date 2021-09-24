package usecase;

import entity.AttendeeData;

import java.util.List;
import java.util.ArrayList;

public class AttendeeManager {

    private List<AttendeeData> attendees;

    /**
     * Constructs an AttendeeManager with its attendees predetermined.
     */
    public AttendeeManager() { this.attendees = new ArrayList<AttendeeData>(); }

    /**
     * Constructs an AttendeeManager with its attendees predetermined.
     * @param attendees The list of all existing attendees
     */
    public AttendeeManager(List<AttendeeData> attendees){
        this.attendees = attendees;
    }

    /**
     * Verify if the attendee exists in AttendeeManager.
     * @param username The attendee's attendeename
     * @return True iff the attendee exists in the list of attendees
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
     * Verify if the attendee exists in AttendeeManager.
     * @param attendee The attendee
     * @return True iff the attendee exists in the list of attendees
     */
    public boolean checkAttendeeExists(AttendeeData attendee) {
        if (this.attendees.contains(attendee)) {
            return true;
        }
        return false;
    }

    /**
     * Returns an attendee's data given the attendee's username.
     * @param username The attendee's username
     * @return Returns the attendee's data iff the attendee exists in the list of attendees
     */
    public AttendeeData getAttendeeByID(String username) {
        for (AttendeeData attendee : this.attendees) {
            if (attendee.getUsername().equals(username)) {
                return attendee;
            }
        }
        return null;
    }

    /* TODO: This needs modification for phase 2 */
    /**
     * Add a new attendee to attendees.
     * @param attendee The new attendee
     * @return Returns true iff the new attendee has been added to attendees
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
     * Return a list of usernames of all existing attendees.
     * @return The list of usernames of all existing attendees.
     */
    public List<String> getAttendeesUsernames() {
        List<String> attendeeNames = new ArrayList<>();

        for (AttendeeData attendee : attendees) {
            attendeeNames.add(attendee.getUsername());
        }

        return attendeeNames;
    }

    public List<AttendeeData> getAttendees() {
        return attendees;
    }
}
