package usecase.restriction;

public class SpeakerValidatorFactory {

    public SpeakerValidator createSpeakerValidator(String selection) {
        switch (selection) {
            case "One Speaker":
                return new OneSpeakerValidator();
            case "Multiple Speaker":
                return new MultiSpeakerValidator();
            case "No Speakers":
                return new NoSpeakerValidator();
            default:
                return null;
        }
    }
}
