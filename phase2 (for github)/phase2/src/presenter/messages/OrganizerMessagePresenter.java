package presenter.messages;

import controller.ConferenceController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizerMessagePresenter extends AbstractMessagePresenter {

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information.
     * @param username the username of the user.
     */
    public OrganizerMessagePresenter(ConferenceController conference, String username){
        super(conference, username);
    }



    //GUI stuff

    private List<Group> getUsertypeGroups(){
        List<Group> usertypes = new ArrayList<>();

        MessageGroup allAttendees = new MessageGroup("Attendees",
                super.getConference().getAttendeeManager().getAttendeesUsernames());
        usertypes.add(allAttendees);

        MessageGroup allSpeakers = new MessageGroup("Speakers",
                super.getConference().getSpeakerManager().getSpeakersUsernames());
        usertypes.add(allSpeakers);

        return usertypes;
    }

    /**
     * Get a Map linking each MessageGroup to its name
     * Each MessageGroup is a valid group the user can message
     * @return a Map linking each MessageGroup to its name
     */
    @Override
    public Map<String, List<Group>> getValidTargetGroups(){
        Map<String, List<Group>> Groups = new HashMap<>();
        Groups.put("Friends", getFriendGroups());
        Groups.put("Usertypes", getUsertypeGroups());
        return Groups;
    }
}
