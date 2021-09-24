package presenter.messages;

import controller.ConferenceController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeakerMessagePresenter extends AbstractMessagePresenter {

    /**
     * Create a new instance of MessageController for a user.
     * @param conference The conference information.
     * @param username the username of the user.
     */
    public SpeakerMessagePresenter(ConferenceController conference, String username){
        super(conference, username);
    }


    //GUI stuff

    private List<Group> getEventGroups(){
        List<Group> myEvents = new ArrayList<>();

        List<Integer> speakingEventIDs = getConference().getSpeakerManager()
                .getSpeakerByID(getUsername()).getSpeakingAt();

        for (int eventID:speakingEventIDs) {
            String eventName = getConference().getEventManager().getEventInfo(eventID).get(0).toString(); //Event Name
            String key = "ID" + eventID + " : " + eventName;

            ArrayList<String> targetUsers = new ArrayList<>(super.getConference().getEventManager().getUsersFromEvent(eventID));

            myEvents.add(new MessageGroup( key, targetUsers ));
        }

        return myEvents;
    }

    /**
     * Get a Map linking each MessageGroup to its name
     * Each MessageGroup is a valid group the user can message
     * @return a Map linking each MessageGroup to its name
     */
    public Map<String, List<Group>> getValidTargetGroups(){
        Map<String, List<Group>> Groups = new HashMap<>();
        Groups.put("Friends", getFriendGroups());
        Groups.put("Events", getEventGroups());
        return Groups;
    }
}
