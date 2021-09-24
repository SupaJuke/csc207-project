package controller;

import entity.UserData;
import gateway.LocalFileGateway;
import usecase.FriendManager;
import usecase.UserManager;

import java.util.Scanner;

public class FriendController implements Accessible{

    private final FriendManager FM;
    private final ConferenceController CC;
    private final LocalFileGateway GW;
    /**
     * Constructs a FriendController containing the FriendManager.
     * @param CC Conference's controller
     * @param user UserData object of user
     */
    public FriendController(ConferenceController CC, UserData user) {
        FM = new FriendManager(user);
        this.CC = CC;
        GW = CC.getLocalFileGateway();
    }

    /**
     * Accesses FriendController UI?
     */
    public void accessUI(){
        boolean run = true;
        while(run) {
            System.out.println("=== Friend Menu ===");///////////////////Press 3 to view friends list
            System.out.println("[1] - Add a Friend");
            System.out.println("[2] - Remove a Friend");
            System.out.println("[3] - View Friend List");
            System.out.println("[4] - Exit");

            Scanner reader = new Scanner(System.in);
            String in = reader.nextLine();

            switch(in) {
                case "1":
                    friendHelper(true);

                    GW.SaveUsers(CC);
                    break;
                case "2":
                    friendHelper(false);

                    GW.SaveUsers(CC);
                    break;
                case "3":
                    System.out.println("--------------");
                    System.out.println("Friends List:");
                    for(String friend : FM.getFriendsList())
                        System.out.println(" - " + friend);
                    System.out.println("--------------");
                    break;
                case "4":
                    run = false;
                    break;

            }
        }
    }

    private void friendHelper(boolean isAdding) {
        Scanner reader = new Scanner(System.in);
        String in;
        if(isAdding) {
            while(true){
                System.out.print("Enter friend username: ");
                System.out.println("Press enter to return to Friend options");
                in = reader.nextLine();
                if (in.equals("")) {
                    break;
                }
                if(CC.getUserManager().checkUserExists(in) && FM.addFriend(in)){
                    System.out.println("Successfully added friend.");
                    break;
                }
                else
                    System.out.println("Friend already exists or is not a User. Please try again");
            }
        }
        else{
            while(true){
                System.out.println("Enter friend username: ");
                in = reader.nextLine();
                if (in.equals("")) {
                    break;
                }
                if(CC.getUserManager().checkUserExists(in) && FM.removeFriend(in)){
                    System.out.println("Successfully removed friend.");
                    break;
                }
                else
                    System.out.println("Friend does not exist or is not a User. Please try again");
            }
        }
    }


    /**
     * Adds friend in FriendManager
     * @param username Username of the friend to be added to the friendlist
     */
    private void addFriend(String username){
        if(FM.addFriend(username))
            System.out.println("Friend has been added.");
        else
            System.out.println("Friend could not be added.");
    }

    /**
     * Removes friend in FriendManager
     * @param username Username of the friend to be removed to the friendlist
     */
    private void removeFriend(String username){
        if(FM.removeFriend(username))
            System.out.println("Friend has been removed.");
        else
            System.out.println("Friend could not be removed.");
    }

}
