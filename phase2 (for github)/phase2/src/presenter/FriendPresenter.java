package presenter;

import controller.ConferenceController;
import usecase.FriendManager;

import java.sql.SQLException;

/**
 * Acts as the middle man between FriendGUI and the Model
 */
public class FriendPresenter {

    private final FriendManager FM;
    private final ConferenceController CC;

    /**
     * Constructs a FriendPresenter and initializes FM and CC;
     * @param CC Conference's controller
     * @param username username of the User
     */
    public FriendPresenter(ConferenceController CC, String username) {
        FM = new FriendManager(CC.getUserManager().getUserByID(username));
        this.CC = CC;
    }

    /**
     * Helper method handles adding/removing a friend
     * @param isAdding true if adding a friend. false if removing a friend
     * @param username username of user
     * @return true if the friend is added / removed. Else, return false
     * @throws SQLException throws a Stack Trace if problems occur interacting with SQL
     */
    public boolean friendHelper(boolean isAdding, String username) throws SQLException{
        try {
            if (isAdding) {
                if (CC.getUserManager().checkUserExists(username) && FM.addFriend(username)){
                    //adds friend to local Database
                    CC.getLocalFileGateway().addFriendToDB(FM.getUser().getUsername(), username);
                    System.out.println("Friend has been added.");
                    return true;
                }
                else
                    System.out.println("Friend could not be added.");
                    return false;
            }
            else {
                if (CC.getUserManager().checkUserExists(username) && FM.removeFriend(username)) {
                    //removes friend from local Database
                    CC.getLocalFileGateway().removeFriendFromDB(FM.getUser().getUsername(), username);
                    System.out.println("Friend has been removed.");
                }
                else
                    System.out.println("Friend could not be removed.");
                return true;
            }
        }
        catch (SQLException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * Getter for FM
     * @return returns FM
     */
    public FriendManager getFriendManager() { return FM; }


}
