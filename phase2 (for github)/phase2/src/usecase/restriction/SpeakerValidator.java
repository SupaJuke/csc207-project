package usecase.restriction;

import entity.Event;

public interface SpeakerValidator {

    /**
     * Check to see if the speaker is addable based on it's restrictions
     * @param speakerUsername The speaker attempting to be added
     * @param event The event the speaker will be added to
     * @return True iff the speaker is addable
     */
    boolean speakerIsAddable(String speakerUsername, Event event);

}
