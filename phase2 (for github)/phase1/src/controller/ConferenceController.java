package controller;
import gateway.ListsOfUsers;
import gateway.LocalFileGateway;
import usecase.*;

public class ConferenceController{
    private final MessageManager messageManager;
    private final UserManager userManager;
    private final EventBrowser eventBrowser;
    private final SpeakerManager speakerManager;
    private final AttendeeManager attendeeManager;
    private final EventManager eventManager;
    private final RoomManager roomManager;

    private final LocalFileGateway gateway;
    /**
     * Creates a new instance of conferenceController
     */
    public ConferenceController() {
         gateway = new LocalFileGateway();

        if (gateway.MessageManagerFileExists()){
            messageManager = gateway.LoadMessageManager();
        }
        else {
            messageManager = new MessageManager();
        }

        if(gateway.UserFileExists()){
            ListsOfUsers UserFile = gateway.LoadUsers();
            userManager = new UserManager(UserFile.getUsers());
            speakerManager = new SpeakerManager(UserFile.getSpeakers());
            attendeeManager = new AttendeeManager(UserFile.getAttendees());
        }
        else{
            userManager = new UserManager();
            speakerManager = new SpeakerManager();
            attendeeManager = new AttendeeManager();
        }

        if (gateway.EventManagerFileExists()){
            eventManager = gateway.LoadEventManager();
            eventBrowser = eventManager.getEventBrowser();
        }
        else{
            eventBrowser = new EventBrowser();
            eventManager = new EventManager(eventBrowser);
        }

        if (gateway.RoomManagerFileExists()){
            roomManager = gateway.LoadRoomManager();
        }
        else{
            roomManager = new RoomManager();
        }

    }

    /**
     * A getter for message manager
     * @return returns the messageManager
     */
    public MessageManager getMessageManager() {
        return messageManager;
    }

    /**
     * A getter for user manager
     * @return returns the usereManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * A getter for event manager
     * @return returns the eventeManager
     */
    public EventBrowser getEventBrowser() {
        return eventBrowser;
    }

    /**
     * A getter for speaker manager
     * @return returns the speakerManager
     */
    public SpeakerManager getSpeakerManager() {
        return speakerManager;
    }

    /**
     * A getter for event manager
     * @return returns the eventManager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * A getter for attendee manager
     * @return returns the AttendeeManager
     */
    public AttendeeManager getAttendeeManager() {
        return attendeeManager;
    }

    /**
     * A getter for room manager
     * @return returns the RoomManager
     */
    public RoomManager getRoomManager(){ return roomManager; }

    /**
     * A getter for LocalFileGateway
     * @return returns the LocalFileGateway
     */
    public LocalFileGateway getLocalFileGateway() {
        return gateway;
    }
}