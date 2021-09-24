package usecase.restriction;

import entity.Event;
import entity.UserData;

public class NoRestrictionOnAttendance implements AttendanceValidator{


    @Override
    public boolean attendeeIsAddable(UserData userData) {
        return true;
    }

    @Override
    public boolean attendeeIsRemovable(UserData userData, Event event) {
        return true;
    }
}
