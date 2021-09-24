package usecase;

import entity.UserData;
import java.io.Serializable;
import java.util.List;

public class FriendManager implements Serializable {

    private final UserData user;

    /**
     * Constructs a friend list with no friends
     * @param user the UserData of the User
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

    /**
     * Checks if a friend exists in a user's list of friends
     * @param username the username of the friend
     * @return returns True iff the friend
     */
    public boolean friendExists(String username) {
       return user.getFriendsList().contains(username);
    }

    /* Getters and Setters **/

    /**
     * A getter for the user
     * @return returns the UserData
     */
    public UserData getUser(){ return user; }

    /**
     * A getter for the list of friends
     * @return returns the list of friends
     */
    public List<String> getFriendsList() {return user.getFriendsList();}
}
