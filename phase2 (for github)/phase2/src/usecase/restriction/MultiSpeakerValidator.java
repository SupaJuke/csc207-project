package usecase.restriction;

import entity.Event;

public class MultiSpeakerValidator implements SpeakerValidator {

    @Override
    public boolean speakerIsAddable(String speakerUsername, Event event) {
        return !event.getSpeakerUsernames().contains(speakerUsername);
    }

}
