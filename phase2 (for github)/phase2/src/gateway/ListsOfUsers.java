package gateway;

import entity.AttendeeData;
import entity.SpeakerData;
import entity.UserData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListsOfUsers implements Serializable {

    private List<UserData> UD;
    private List<AttendeeData> AD;
    private List<SpeakerData> SD;

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

    public ListsOfUsers() {
        UD = new ArrayList<>();
        AD = new ArrayList<>();
        SD = new ArrayList<>();
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

    public void addUD(UserData UD) {
        this.UD.add(UD);
    }

    public void addAD(AttendeeData AD) {
        this.AD.add(AD);
    }

    public void addSD(SpeakerData SD) {
        this.SD.add(SD);
    }
}
