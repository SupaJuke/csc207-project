package usecase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import entity.UserData;
import gateway.LocalFileGateway;

public class FriendManager implements Serializable {

    private final UserData user;

    /**
     * Constructs a friend list with no friends
     */
    public FriendManager(UserData user){
        this.user = user;
    }

    /**
     * Checks if friend exists, adds friend, and updates friendsList
     * @param username The username of the friend that gets added
     * @return True iff the friend gets added. Else returns False
     */
    public boolean addFriend(String username) {
        if(!friendExists(username)) {
            user.addFriend(username);
            return true;
        }
        return false;
    }

    /**
     * Checks if friend exists, removes friend, and updates friendsList
     * @param username The username of the friend that gets removed
     * @return True iff the friend gets removed. Else return False
     */
    public boolean removeFriend(String username) {
        if(friendExists(username)) {
            user.removeFriend(username);
            return true;
        }
        return false;
    }

    public boolean friendExists(String username) {
       return user.getFriendsList().contains(username);
    }

    /** Getters and Setters **/

    public UserData getUser(){ return user; }

    public List<String> getFriendsList() { return user.getFriendsList(); }
}
