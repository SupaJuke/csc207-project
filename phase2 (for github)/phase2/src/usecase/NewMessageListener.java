package usecase;

import entity.Taggable;

public interface NewMessageListener {

    /**
     * Notify the listener of the new message
     * @param messageTags the message as a Taggable
     */
    void notifyNewMessage(Taggable messageTags);
}
