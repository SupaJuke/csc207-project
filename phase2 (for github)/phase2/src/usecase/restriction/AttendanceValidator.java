package usecase.restriction;

import entity.Event;
import entity.UserData;

public interface AttendanceValidator {

    /**
     * Check to see if an attendance is addable based on the type of
     * restriction
     * @param userData The user attempting to be added to the event
     * @return True iff the attendee is addable
     */
    boolean attendeeIsAddable(UserData userData);

    /**
     * Check to see if an attendee is removable
     * @param userData The user that is trying to removed
     * @param event The event we are removing from
     * @return True iff the attendee is removable
     */
    boolean attendeeIsRemovable(UserData userData, Event event);

}
