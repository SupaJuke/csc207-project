package usecase.restriction;

import entity.Event;
import entity.UserData;

public class SpeakerOnlyValidator implements AttendanceValidator{
    @Override
    public boolean attendeeIsAddable(UserData userData) {
        return userData.getUserType().equals("Speaker");
    }

    @Override
    public boolean attendeeIsRemovable(UserData userData, Event event) {
        return event.getUsersInAttendance().contains(userData.getUsername());
    }
}
