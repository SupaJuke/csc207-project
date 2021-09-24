package presenter;

import entity.Room;
import entity.Event;
import gateway.LocalFileGateway;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import usecase.EventManager;
import usecase.RoomManager;
import usecase.UserManager;
import usecase.restriction.RestrictionValidator;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EventPresenter {

    private final String username;
    private final EventManager EM;
    private final RoomManager RM;
    private final LocalFileGateway GW;
    private final UserManager UM;

    /**
     * Constructs an EventPresenter
     * @param username UserData using the EventGUI
     * @param EM EventManager
     * @param RM RoomManager
     * @param GW Gateway class to save the changes
     */
    public EventPresenter(String username, EventManager EM, RoomManager RM, LocalFileGateway GW, UserManager UM) {

        this.username = username;
        this.EM = EM;
        this.RM = RM;
        this.GW = GW;
        this.UM = UM;
    }

    /**
     * Prints the list of events currently in EventBrowser
     */
    public List<String> getListOfEvents(){
        return EM.getScheduleInfo();
    }

    public String checkRoom(int eventID){
        try {
            // Room is at max capacity
            int dur = EM.getEventFromID(eventID).getEventDuration();
            if (RM.getRoomByEventIDAndTime(eventID, EM.getEventFromID(eventID).getEventTime(), dur).getRoomCapacity()
                    <= RM.getRoomByEventIDAndTime(eventID,
                    EM.getEventFromID(eventID).getEventTime(), dur).getNumAttendeeAtTime(
                    EM.getEventFromID(eventID).getEventTime())){

                return "full";
            }
        }
        catch(NullPointerException e){
            // Room doesn't exist
            return "DNE";
        }
        // Room exists
        return "exists";
    }

    public EventManager getEM() {
        return EM;
    }

    public String checkRestrictions(Map<String, List<String>> restrictions) {
        if (restrictions.isEmpty()) {
            return "None";
        }
        else {
            StringBuilder currString = new StringBuilder();
            for (String key : restrictions.keySet())
                currString.append("\n").append(key).append(" restriction: ").append(restrictions.get(key)).append(" ");
            return currString.toString();
        }
    }

    public String checkSpeakers(List<String> speakers) {
        if (speakers.isEmpty()) {
            return "None";
        } else {
            String prefix = "";
            StringBuilder currString = new StringBuilder();
            for (String speaker : speakers) {
                currString.append(prefix);
                prefix = ",";
                currString.append(speaker);
            }
            return currString.toString();
        }
    }

    public void signUpToEvent(int eventID, Label resultLabel, int roomID) {

        Event event = EM.getEventFromID(eventID);

        RestrictionValidator RV = new RestrictionValidator();
        // Checking for qualifications
        if (!RV.checkIfUserIsAddable(event, UM.getUserByID(username))){
            resultLabel.setText("You are not authorized to go to this event.");
        }
        // Checking for the event capacity
        else if (!RV.checkIfEventHasSpace(event)){
            resultLabel.setText("The event is at full capacity.");
        }
        // Checking for overlap
        else if (EM.checkAlreadyAttendingEvent(UM.getUserByID(username), eventID)) {
            resultLabel.setText("Events are overlapping!");
        }
        else if (EM.addUserToEvent(eventID, UM.getUserByID(username).getUsername())){
            UM.getUserByID(username).addEventID(eventID);
            EM.addUserToEvent(eventID, UM.getUserByID(username).getUsername());
            if (!(event.getEventID() == -1)) {
                Room room = RM.getRoomByID(roomID);

                for (int time = event.getEventTime();
                     time < event.getEventTime() + event.getEventDuration(); time++) {
                    RM.addUserToRoom(room, time, UM.getUserByID(username));
                }
            }
            GW.saveSavable(EM, "EventManager");
            GW.saveSavable(RM, "RoomManager");

            try {
                GW.signUpToEventInDB(UM.getUserByID(username).getUsername(), eventID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            resultLabel.setText("You are now attending this event");
        }
        else {
            resultLabel.setText("You are already attending this event");
        }
    }

    public boolean cancelForEvent(Label label, TextField textField) {
        try {
            int eventID = Integer.parseInt(textField.getText());
            Event event = EM.getEventFromID(eventID);

            if (getEM().checkEventExists(eventID) && UM.getUserByID(username).getEventIDs().contains(eventID)) {
                UM.getUserByID(username).removeEventID(eventID);
                EM.removeUserFromEvent(eventID, UM.getUserByID(username).getUsername());
                if (!(event.getRoom() == -1)) {
                    Room room = RM.getRoomByEventIDAndTime(eventID, event.getEventTime(), event.getEventDuration());
                    for (int time = event.getEventTime();
                         time < event.getEventTime() + event.getEventDuration(); time++) {
                        RM.removeUserInRoom(room, time, UM.getUserByID(username));
                    }
                }
                try {
                    GW.cancelForEventInDB(UM.getUserByID(username).getUsername(), eventID);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                return true;
            }
            else {
                label.setText("Invalid ID. Please try again.");
                return false;
            }
        }
        catch (NumberFormatException e) {
            label.setText("Invalid ID. Please try again.");
            return false;
        }
    }

    public String checkRoomStatus(int eventID) {
        int roomID = EM.getEventFromID(eventID).getRoom();
        if (roomID == -1) {
            return "The room has not been assigned yet.";
        }
        return String.valueOf(roomID);
    }

    public String getEventTechnology(int eventID) {
        if (EM.getEventFromID(eventID).getRequirements().containsKey("Technology")) {
            return EM.getEventFromID(eventID).getRequirements().get("Technology").toString();
        }
        else {
            return "None";
        }
    }

    public List<String> viewMyEvents() {
        return EM.getListOfUserEvents(UM.getUserByID(username));
    }


}