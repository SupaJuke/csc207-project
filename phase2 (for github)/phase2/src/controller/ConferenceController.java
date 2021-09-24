package controller;
import gateway.LocalFileGateway;
import gateway.dbConnection;
import usecase.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConferenceController{

    private final LocalFileGateway gateway;
    public Map<String, Savable> savedFiles = new HashMap<String, Savable>();

    Connection connection;

    /**
     * Creates a new instance of conferenceController
     */
    public ConferenceController() throws SQLException {

        try{
            this.connection = dbConnection.getConnection();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        gateway = new LocalFileGateway(connection);
        gateway.loadAllFiles("This is going to use the new database");
        this.savedFiles = this.gateway.savedFiles;

    }
    public boolean isDatabaseConnected(){
        return this.connection != null;
    }
    /**
     * A getter for message manager
     * @return returns the messageManager
     */
    public MessageManager getMessageManager() {
        return (MessageManager) this.gateway.savedFiles.get("MessageManager");
    }

    /**
     * A getter for user manager
     * @return returns the usereManager
     */
    public UserManager getUserManager() {
        return (UserManager) this.gateway.savedFiles.get("UserManager");
    }

    /**
     * A getter for speaker manager
     * @return returns the speakerManager
     */
    public SpeakerManager getSpeakerManager() {
        return (SpeakerManager) this.gateway.savedFiles.get("SpeakerManager");
    }

    /**
     * A getter for event manager
     * @return returns the eventManager
     */
    public EventManager getEventManager() {
        return (EventManager) this.gateway.savedFiles.get("EventManager");
    }

    /**
     * A getter for attendee manager
     * @return returns the AttendeeManager
     */
    public AttendeeManager getAttendeeManager() {
        return (AttendeeManager) this.gateway.savedFiles.get("AttendeeManager");
    }

    /**
     * A getter for room manager
     * @return returns the RoomManager
     */
    public RoomManager getRoomManager() {
        return (RoomManager) this.gateway.savedFiles.get("RoomManager");
    }

    /**
     * A getter for VIP manager
     * @return returns the VIP manager
     */
    public VIPManager getVIPManager(){return (VIPManager) this.gateway.savedFiles.get("VIPManager");}

    /**
     * A getter for LocalFileGateway
     * @return returns the LocalFileGateway
     */
    public LocalFileGateway getLocalFileGateway() {
        return gateway;
    }
}