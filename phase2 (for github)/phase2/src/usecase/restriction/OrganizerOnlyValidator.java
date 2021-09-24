package usecase.restriction;

import entity.Event;
import entity.UserData;

public class OrganizerOnlyValidator implements AttendanceValidator{

    @Override
    public boolean attendeeIsAddable(UserData userData) {
        return userData.getUserType().equals("Organizer");
    }

    @Override
    public boolean attendeeIsRemovable(UserData userData, Event event) {
        return event.getUsersInAttendance().contains(userData.getUsername());
    }

}
