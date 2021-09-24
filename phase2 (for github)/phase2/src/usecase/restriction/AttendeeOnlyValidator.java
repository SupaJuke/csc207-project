package usecase.restriction;

import entity.Event;
import entity.UserData;

public class AttendeeOnlyValidator implements AttendanceValidator {
    @Override
    public boolean attendeeIsAddable(UserData userData) {
        return userData.getUserType().equals("Attendee");
    }

    @Override
    public boolean attendeeIsRemovable(UserData userData, Event event) {
        return event.getUsersInAttendance().contains(userData.getUsername());
    }
}
