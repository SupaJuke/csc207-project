package presenter;

import controller.ConferenceController;
import gui.*;
import javafx.stage.Stage;
import presenter.messages.AbstractMessagePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountPresenter implements BasicPresenter {

    ConferenceController CC;

    String username;
    Stage PrimaryStage;
    AccountGUI view;
    Viewable loginGUI;

    AbstractMessagePresenter messagePresenter;
    MessageGUI messageGUI;

    EventPresenter eventPresenter;
    EventGUI eventGUI;

    UserPresenter userPresenter;
    Viewable userGUI;

    FriendPresenter friendPresenter;
    FriendGUI friendGUI;

    Map<String, Viewable> allControllers;

    /**
     * Constructs a AttendeeController assigned to a specific conference
     * @param conference This is the conference the attendee is attending.
     * @param username This is the userinfo for the specific attendee.
     * @param primaryStage This is the stage all GUI is drawn on.
     * @param LG this is the LoginGUI that the account sign out needs to go back to.
     * @param accGUI this is the Account's main menu GUI screen that all submenus go back to.
     */
    public AccountPresenter(ConferenceController conference, String username, Stage primaryStage, Viewable LG, 
                            AccountGUI accGUI) {
        CC = conference;
        this.username = username;
        PrimaryStage = primaryStage;
        loginGUI = LG;
        view = accGUI;
        view.setPresenter(this);

        eventPresenter = new EventPresenter(username, CC.getEventManager(), CC.getRoomManager(),
                CC.getLocalFileGateway(), CC.getUserManager());
        eventGUI = new EventGUI();
        eventGUI.setPresenters(eventPresenter, view);

        userPresenter = new UserPresenter(CC.getUserManager(), username, CC.getLocalFileGateway());
        userGUI = new UserGUI(userPresenter, this);

        friendPresenter = new FriendPresenter(conference, username);
        friendGUI = new FriendGUI();
        friendGUI.setPresenters(friendPresenter, this);

        allControllers = new HashMap<String, Viewable>(){{
            put("1", messageGUI);
            put("2", eventGUI);
            put("3", userGUI);
            put("4", friendGUI);
        }};
    }

    public Viewable getMessageGUI(){return messageGUI; }
    public Viewable getEventGUI(){ return eventGUI; }
    public Viewable getUserGUI(){return userGUI;}
    public Viewable getFriendGUI(){return friendGUI;}
    public Viewable getLoginGUI(){return loginGUI;}
    public ISignUP getLoginGUIForSignUP(){return (ISignUP) loginGUI;}
    public Viewable getAccountGUI(){return view;}
    public String getDisplayName() {return CC.getUserManager().getUserByID(username).getDisplayname();}
    public String update(String prompt, String input){return "";}
    public String checkInputs(String title, List<String> prompts, List<String> inputs ){return "";}
    public List<String> getRoomsForGUI() {return null;}
    public String getUsername() {return username;}
}
