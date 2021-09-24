package presenter.messages;

import controller.ConferenceController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendeeMessagePresenter extends AbstractMessagePresenter {

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information.
     * @param username the username of the user.
     */
    public AttendeeMessagePresenter(ConferenceController conference, String username){
        super(conference, username);
    }


    //GUI stuff

    private List<Group> getSpeakerGroups(){
        List<String> speakers = new ArrayList<>(super.getConference().getSpeakerManager().getSpeakersUsernames());
        removeDupes(speakers);

        return createGroupList(speakers);
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
        Groups.put("Speakers", getSpeakerGroups());
        return Groups;
    }
}
