package controller;
import entity.*;

import java.util.*;

public class AccountController implements Accessible {
    /**
     * Each attendee will get their own AttendeeController
     */
    ConferenceController Conference;
    UserData userData;

    AbstractMessageController messageController;
    EventController eventController;
    UserController userController;
    FriendController friendController;

    List<String> AccessibleOptions;

    Map<String, String> AllOptions = new HashMap<String, String>(){{
        put("1", "Message");
        put("2", "Events");
        put("3", "Profile");
        put("4", "Friends");
        put("5", "SignOut");
        put("6", "Organizer Options");
        put("7", "Speaker Options");
    }};
    Map<String, Accessible> AllControllers;

    /**
     * Constructs a AttendeeController assigned to a specific conference
     * @param conference This is the conference the attendee is attending.
     * @param userinfo This is the userinfo for the specific attendee.
     *
     * @va messageController
     */
    public AccountController(ConferenceController conference, UserData userinfo) {
        Conference = conference;
        userData = userinfo;

        eventController = new EventController(Conference.getEventBrowser(), userData,
                Conference.getEventManager(), Conference.getRoomManager());
        userController = new UserController(Conference.getUserManager(), userData);
        friendController = new FriendController(conference, userData);

        AllControllers = new HashMap<String, Accessible>(){{
            put("1", messageController);
            put("2", eventController);
            put("3", userController);
            put("4", friendController);
        }};
    }

    @Override
    public void accessUI() {
        while (true){
            System.out.println("=== " + userData.getDisplayname() + "'s Main Menu ===");

            PrintOptions();

            Scanner reader = new Scanner(System.in);
            String input = reader.nextLine();
            //reader.close();

            if (AllControllers.containsKey(input)){
                AllControllers.get(input).accessUI();
            }
            else if (input.equals("5")){
                System.out.println("Signed Out!");
                break;
            }
            else if (input.equals("6")){
                this.subMenuUI();
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    protected void PrintOptions(){
        int number = 1;
        for(String key : AllOptions.keySet()){
            if (AccessibleOptions.contains(key)){
                System.out.println("[" + number + "] - " + AllOptions.get(key));
                number++;
            }
        }
    }

    void subMenuUI(){
        System.out.println("Invalid Input");
    }

    public void setMessageController(AbstractMessageController amc){
        this.messageController = amc;
        this.AllControllers.put("1", amc);
    }
}
