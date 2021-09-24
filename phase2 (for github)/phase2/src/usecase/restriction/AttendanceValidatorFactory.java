package usecase.restriction;

public class AttendanceValidatorFactory {

    /**
     * Create an attendance validator given a selected restriction on it
     * @param selection The selection of the type of attendance restriction
     * @return The attendance validator
     */
    public AttendanceValidator createAttendanceValidator(String selection) {
        switch (selection) {
            case "Attendee Only":
                return new AttendeeOnlyValidator();
            case "Organizer Only":
                return new OrganizerOnlyValidator();
            case "Speaker Only":
                return new SpeakerOnlyValidator();
            case "VIP Only":
                return new VIPOnlyValidator();
            case "No Restriction":
                return new NoRestrictionOnAttendance();
            default:
                return null;
        }
    }
}
