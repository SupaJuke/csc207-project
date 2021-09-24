package presenter.messages;

import java.util.List;

public interface Group {

    /**
     * get the name of the Group
     * @return the group name
     */
    String getGroupName();

    /**
     * get the group targets
     * @return the list of usernames of ppl in the group
     */
    List<String> getGroupTargets();
}
