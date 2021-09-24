package usecase.restriction;

import entity.Event;

public class OneSpeakerValidator implements SpeakerValidator{

    @Override
    public boolean speakerIsAddable(String speakerUsername, Event event) {
        int numOfSpeakers = event.getSpeakerUsernames().size();
        if (numOfSpeakers < 1) {
            return true;
        } else if (event.getSpeakerUsernames().contains(speakerUsername)){
            return false;
        }
        return false;
    }
}
