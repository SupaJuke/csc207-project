package usecase;

import entity.Taggable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MessageNotifier implements Serializable {
    private Set<NewMessageListener> listeners = new HashSet<>();

    /**
     * Adds a NewMessageListener to the list of listeners to notify when a new message is sent
     * @param toAdd the NewMessageListener you want to add
     */
    public void addListener(NewMessageListener toAdd) {
        listeners.add(toAdd);
    }


    /**
     * Removes a NewMessageListener from the list of listeners
     * @param toRemove the NewMessageListener you want to remove
     * @return true if an object was removed
     */
    public boolean removeListener(NewMessageListener toRemove){
        return listeners.remove(toRemove);
    }

    /**
     * notify the listeners of a new message
     * @param messageTags the new message as a Taggable
     */
    public void notifyNewMessage(Taggable messageTags) {
        for (NewMessageListener ml : listeners)
            ml.notifyNewMessage(messageTags);
    }
}
