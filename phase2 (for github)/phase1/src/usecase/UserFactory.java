package usecase;

import entity.AttendeeData;
import entity.OrganizerData;
import entity.SpeakerData;
import entity.UserData;

import java.util.ArrayList;

public class UserFactory {

    /**
     * Creates a new Attendee
     * @param username the username of the attendee
     * @param displayName the display name of the attendee
     * @param password the password of the attendee
     * @return returns a new AttendeeData class with username displayName and password
     */
    public AttendeeData createDefaultAttendee(String username, String displayName, String password) {
       return new AttendeeData(username, displayName, password);
    }

    /**
     * Creates a new Organizer
     * @param username the username of the organizer
     * @param displayName the display name of the organizer
     * @param password the password of the organizer
     * @return returns a new OrganizerData class with username displayName and password
     */
    public OrganizerData createDefaultOrganizer(String username, String displayName, String password) {
        return new OrganizerData(username, displayName, password);
    }

    /**
     * Creates a new Speaker
     * @param username the username of the speaker
     * @param displayName the display name of the spzeaker
     * @param password the password of the speaker
     * @return returns a new SpeakerData class with username displayName and password
     */
    public SpeakerData createDefaultSpeaker(String username, String displayName, String password) {
        return new SpeakerData(username, displayName, password, new ArrayList<>());
    }

}
