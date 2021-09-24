package usecase.restriction;

import entity.Event;

public class NoSpeakerValidator implements SpeakerValidator{

    @Override
    public boolean speakerIsAddable(String speakerUsername, Event event) {
        return false;
    }

}
