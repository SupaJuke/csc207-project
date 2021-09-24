package presenter.messages;

import java.util.ArrayList;
import java.util.List;

public class MessageGroup implements Group {
    private final String groupName;
    private final List<String> groupTargets;

    /**
     * create an empty MessageGroup
     * @param groupName the groupName of the MessageGroup
     */
    public MessageGroup(String groupName) {
        this.groupName = groupName;
        this.groupTargets = new ArrayList<>();
        this.groupTargets.add(groupName);
    }

    /**
     * create a MessageGroup with a list of targets
     * @param groupName the groupName of the MessageGroup
     * @param groupTargets the usernames of the ppl in the MessageGroup
     */
    public MessageGroup(String groupName, List<String> groupTargets) {
        this.groupName = groupName;
        this.groupTargets = new ArrayList<>(groupTargets);
    }

    /**
     * get the name of the MessageGroup
     * @return the MessageGroup name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * get the group targets
     * @return the list of usernames of ppl in the group
     */
    public List<String> getGroupTargets() {
        return groupTargets;
    }
}
