package usecase.restriction;

import entity.Event;
import entity.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestrictionValidator {

    /**
     * Check to see if the speaker is addable based on the restriction
     * on the event
     * @param event The event with the restriction
     * @param speakerUsername The speaker being added
     * @return True iff the speaker was added
     */
    public boolean checkIfSpeakerIsAddable(Event event, String speakerUsername) {

        String restrictionType = "Speaker";

        if (!checkRestrictionExists(event, restrictionType)){
            return true; // There is no restriction so return true
        }

        String speakersAmount = event.getRestrictions().get(restrictionType).get(0);

        // Use a factory to make a speaker validator
        SpeakerValidatorFactory speakerValidatorFactory = new SpeakerValidatorFactory();
        SpeakerValidator speakerValidator = speakerValidatorFactory.createSpeakerValidator(speakersAmount);

        // Check if the speaker is addable in terms of the events restrictions
        if (!speakerValidator.speakerIsAddable(speakerUsername, event)) {
            System.out.println("Speaker maximum capacity reached. Cannot add more speakers.");
            return false;
        }
        return true;
    }

    /**
     * Check to see if the user is addable based on the restriction on the event
     * @param event The event with the restriction
     * @param userData The user that is being added to the event
     * @return True iff the user is addable
     */
    public boolean checkIfUserIsAddable(Event event, UserData userData) {

        String restrictionType = "Attendance";

        if (!checkRestrictionExists(event, restrictionType)){
            return true; // There is no restriction so return true
        }

        List<String> userRestrictions = event.getRestrictions().get(restrictionType);

        for (String userRes : userRestrictions) {
            // Use a factory to make a attendance validator
            AttendanceValidatorFactory attendanceValidatorFactory = new AttendanceValidatorFactory();
            AttendanceValidator attendanceValidator = attendanceValidatorFactory.createAttendanceValidator(userRes);

            // Check if the speaker is addable in terms of the events restrictions
            if (!attendanceValidator.attendeeIsAddable(userData)) {
                System.out.println("You are not authorized to go to this event.");
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * Check to see if the event has space based on the capacity of the event
     * @param event The event to be checked for it's capacity
     * @return True iff the event is not at max capacity
     */
    public boolean checkIfEventHasSpace(Event event){

        String restrictionType = "Capacity";

        if (!checkRestrictionExists(event, restrictionType)){
            return true; // There is no restriction so return true
        }

        String eventCap = event.getRestrictions().get(restrictionType).get(0);

        if (!(Integer.parseInt(eventCap.replaceAll("\\s+","")) > event.getUsersInAttendance().size())){
            System.out.println("The event is at full capacity.");
            return false;
        }
        return true;

    }

    /**
     * Add a restriction to the event
     * @param event The event that will have a restriction
     * @param restrictionType The type of the restriction
     * @param restriction the specific restriction itself
     */
    public void addElementToRestriction(Event event, String restrictionType, String restriction){
        Map<String, List<String>> rst = event.getRestrictions();
        if (checkRestrictionExists(event, restrictionType)){
            rst.get(restrictionType).add(restriction);
        } else {
            List<String> restrictions = new ArrayList<>();
            restrictions.add(restriction);
            rst.put(restrictionType, restrictions);
        }

        event.setRestrictions(rst);
    }

    /**
     * Check to see if an event already has a restriction
     * @param event The event that will be checked
     * @param restrictionType The restriction type to be checked for its existence
     * @return True iff the restriction type exists
     */
    public boolean checkRestrictionExists(Event event, String restrictionType){
        return event.getRestrictions().containsKey(restrictionType);
    }
}
