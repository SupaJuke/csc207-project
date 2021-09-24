package gateway;

import entity.AttendeeData;
import entity.SpeakerData;
import entity.UserData;

import java.io.Serializable;
import java.util.List;

public class ListsOfUsers implements Serializable {

    private final List<UserData> UD;
    private final List<AttendeeData> AD;
    private final List<SpeakerData> SD;

    /**
     * Constructor for this class which is used by the LocalFileGateway to store lists of different users.
     * @param users This is a list of UserData
     * @param attendees This is a list of AttendeeData
     * @param speakers T his is a list of SpeakerData
     */
    public ListsOfUsers(List<UserData> users, List<AttendeeData> attendees, List<SpeakerData> speakers) {
        UD = users;
        AD = attendees;
        SD = speakers;
    }

    /**
     * Getter for UserData List
     * @return List<UserData>
     */
    public List<UserData> getUsers() {
        return UD;
    }

    /**
     * Getter for AttendeeData List
     * @return List<AttendeeData>
     */
    public List<AttendeeData> getAttendees() {
        return AD;
    }

    /**
     * Getter for SpeakerData List
     * @return List<SpeakerData>
     */
    public List<SpeakerData> getSpeakers() {
        return SD;
    }
}
