package presenter;

import controller.ConferenceController;
import entity.UserData;
import gui.AccountGUI;
import gui.MessageGUI;
import gui.Viewable;
import javafx.stage.Stage;
import presenter.messages.OrganizerMessagePresenter;
import usecase.EventManager;
import usecase.RoomManager;
import usecase.SpeakerManager;
import usecase.UserManager;
import usecase.restriction.RestrictionValidator;

import java.sql.SQLException;
import java.util.List;

public class OrganizerPresenter extends AccountPresenter implements BasicPresenter {
    private final ConferenceController CC;
    private final EventManager EM;
    private final UserManager UM;
    private final SpeakerManager SM;
    private final RoomManager RM;


    public OrganizerPresenter(ConferenceController CC, String username, Stage PM, Viewable LG, AccountGUI organizerGUI) {
        super(CC, username, PM, LG, organizerGUI);

        this.CC = CC;
        EM = CC.getEventManager();
        UM = CC.getUserManager();
        SM = CC.getSpeakerManager();
        RM = CC.getRoomManager();

        messagePresenter = new OrganizerMessagePresenter(CC, username);
        messageGUI = new MessageGUI(messagePresenter);
    }
    @Override
    public String update(String prompt, String input){
        if (input.equals("")){
            return "Enter a value";
        }
        else if (input.equals("123")){
            return "valid";
        }
        else if ((prompt.equals("Event ID") || prompt.equals("Room ID") || prompt.equals("Event Duration") ||
                prompt.equals("Room Capacity") || prompt.equals("Event Capacity") || prompt.equals("Time")) &&
                cannotParseToInt(input)){
            return "Enter an integer value";
        }
        else if (prompt.startsWith("Speaker Restrictions")) {
            // prompt Speaker RestrictionsEvent ID:#
            RestrictionValidator RV = new RestrictionValidator();
            RV.addElementToRestriction(EM.getEventFromID(Integer.parseInt(prompt.substring(29))),
                    "Speaker", input);
        }

        else if (prompt.startsWith("User Restrictions")) {
            // prompt User RestrictionsEvent ID:#
            RestrictionValidator RV = new RestrictionValidator();
            RV.addElementToRestriction(EM.getEventFromID(Integer.parseInt(prompt.substring(26))),
                    "Attendance", input);
        }
        else if (prompt.startsWith("Tech Requirements")) {
            // prompt Tech RequirementsEvent ID:#
            if (prompt.substring(17).startsWith("Event ID")) {
                EM.addElementToRequirements(EM.getEventFromID(Integer.parseInt(prompt.substring(26))),
                        "Technology", input);
                CC.getLocalFileGateway().saveSavable(EM, "EventManager"); //Saves EventManager
            }
            // prompt Tech RequirementsRoom ID:#
            else{
                RM.getRoomByID(
                        Integer.parseInt(prompt.substring(25))).addElementToFeatures("Technology", input);
                CC.getLocalFileGateway().saveSavable(RM, "RoomManager"); //Saves RoomManager
            }
        }
        else if (prompt.startsWith("Furniture")){
            // prompt FurnitureEvent ID:#
            if (prompt.substring(9).startsWith("Event ID")){
                EM.addElementToRequirements(EM.getEventFromID(Integer.parseInt(prompt.substring(18))),
                        "Furniture", input);
                CC.getLocalFileGateway().saveSavable(EM, "EventManager"); //Saves EventManager
            }
            // prompt FurnitureRoom ID:#
            else{
                RM.getRoomByID(
                        Integer.parseInt(prompt.substring(17))).addElementToFeatures("Furniture", input);
                CC.getLocalFileGateway().saveSavable(RM, "RoomManager"); //Saves RoomManager
            }
        }
        else{
            switch (prompt) {
                case "Event ID": {
                    return checkEventID(input);
                }
                case "Speaker's Username": {
                    return checkSpeakerName(input);
                }
                case "Time": {
                    return checkTime(input);
                }
                case "Room ID": {
                    return checkRoomID(input);
                }
                case "Event Duration":{
                    return checkEventDuration(input);
                }
                case ("Event Capacity"):
                case ("Room Capacity") : {
                    return checkCapacity(input);
                }

            }
        }
        return "valid";

    }
    @Override
    public String checkInputs(String title, List<String> prompts, List<String> inputs){
        for (String prompt: prompts){
            String check = update(prompt, inputs.get(prompts.indexOf(prompt)));
            if (check.equals("Enter a value")){
                return "Please fill all the required fields.";
            }
            else if (!check.equals("valid")){
                return check;
            }
        }
        switch(title){
            case "Create Event":
                // Event ID [0] / Time [1] / Duration [2] / Capacity [3]
                if (!EM.generateEvent(inputs.get(0), Integer.parseInt(inputs.get(1)), Integer.parseInt(inputs.get(2)))) {
                    return "There is another event at this time";
                }
                RestrictionValidator RV = new RestrictionValidator();
                RV.addElementToRestriction(EM.browseEvents().get(EM.browseEvents().size() - 1), "Capacity",
                        inputs.get(3));
                System.out.println("Event Created");
                CC.getLocalFileGateway().saveSavable(EM, "EventManager"); //Saves EventManager
                return String.format("Event ID:%s", EM.browseEvents().get(EM.browseEvents().size() - 1).getEventID());
            case "Remove Event":
                // Event ID [0]
                if (!EM.removeEvent(Integer.parseInt(inputs.get(0)))){
                    return "Invalid Event ID";
                }
                CC.getLocalFileGateway().saveSavable(EM, "EventManager"); //Saves EventManager
                break;
            case "Add Speaker to Event":
                RestrictionValidator rv = new RestrictionValidator();
                if (!rv.checkIfSpeakerIsAddable(EM.getEventFromID(Integer.parseInt(inputs.get(0))), inputs.get(1))){
                    return "Speaker maximum capacity reached. Cannot add more speakers.";
                 }
                // Event ID [0] / Username [1]
                else if (EM.checkAlreadyAttendingEvent(UM.getUserByID(inputs.get(1)), Integer.parseInt(inputs.get(0)))
                        || checkAlreadySpeakingEvent(UM.getUserByID(inputs.get(1)), Integer.parseInt(inputs.get(0)))
                        || !SM.addEventSpeakingIn(inputs.get(1), Integer.parseInt(inputs.get(0)))){
                    return String.format("%s is already in an event at this time", inputs.get(1));
                }
                // Event Has a Room
                if (EM.getEventFromID(Integer.parseInt(inputs.get(0))).getRoom() != -1){
                    RM.addSpeakerToRoom(
                            EM.getEventFromID(Integer.parseInt(inputs.get(0))).getRoom(),
                            EM.getEventFromID(Integer.parseInt(inputs.get(0))).getEventTime(),
                            SM.getSpeakerByID(inputs.get(1)));
                    CC.getLocalFileGateway().saveSavable(RM, "RoomManager"); //Saves RoomManager
                }
                EM.addSpeaker(Integer.parseInt(inputs.get(0)), inputs.get(1));
                try {
                    //Saves the SpeakingAt to SpeakerData in the DB
                    CC.getLocalFileGateway().addSpeakingAt(Integer.parseInt(inputs.get(0)), inputs.get(1));
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                CC.getLocalFileGateway().saveSavable(EM, "EventManager"); //Saves EventManager
                break;
            case "Remove Speaker from Event":
                // Event ID [0] / Username [1]
                if (!SM.cancelEventSpeakingAt(Integer.parseInt(inputs.get(0)), inputs.get(1))){
                    return String.format("%s is not speaking at this event", inputs.get(1));
                }
                EM.removeSpeaker(Integer.parseInt(inputs.get(0)), inputs.get(1));
                //EM.removeUserFromEvent(Integer.parseInt(inputs.get(0)), inputs.get(1));
                if (EM.getEventFromID(Integer.parseInt(inputs.get(0))).getRoom() != -1){
                    RM.removeSpeakerFromRoom(
                            EM.getEventFromID(Integer.parseInt(inputs.get(0))).getRoom(),
                            EM.getEventFromID(Integer.parseInt(inputs.get(0))).getEventTime(),
                            SM.getSpeakerByID(inputs.get(1)));
                    CC.getLocalFileGateway().saveSavable(RM, "RoomManager"); //Saves RoomManager
                }
                try {
                    //Saves the SpeakingAt to SpeakerData in the DB
                    CC.getLocalFileGateway().cancelSpeakingAtInDB(Integer.parseInt(inputs.get(0)), inputs.get(1));
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                CC.getLocalFileGateway().saveSavable(EM, "EventManager"); //Saves EventManager
                break;
            case "Create a Room":
                // Capacity [0]
                if (!RM.generateRoom(Integer.parseInt(inputs.get(0)))){
                    return "Invalid capacity";
                }
                CC.getLocalFileGateway().saveSavable(RM, "RoomManager"); //Saves RoomManager
                System.out.println("Room Created");

                return String.format("Room ID:%d" , RM.getMostRecentlyGeneratedRoom().getRoomID());
            case "Set Room for an Event":
                // Event ID [0] / Room ID [1]
                if (checkRoom(Integer.parseInt(inputs.get(1)), Integer.parseInt(inputs.get(0))).equals("1")) {
                    return "Room is already booked at this event's time";
                }
                else if (checkRoom(Integer.parseInt(inputs.get(1)), Integer.parseInt(inputs.get(0))).equals("2")){
                    return "Room capacity smaller than Event capacity.";
                }
                else if (checkRoom(Integer.parseInt(inputs.get(1)), Integer.parseInt(inputs.get(0))).equals("3")){
                    return "Room does not meet requirements for the event";
                }
                else {
                    if (RM.addEventToRoom(Integer.parseInt(inputs.get(1)),
                            EM.getEventFromID(Integer.parseInt(inputs.get(0))))) {
                        EM.getEventFromID(Integer.parseInt(inputs.get(0))).addRoom(Integer.parseInt(inputs.get(1)));
                        CC.getLocalFileGateway().saveSavable(EM, "EventManager"); //Saves EventManager
                        CC.getLocalFileGateway().saveSavable(RM, "RoomManager"); //Saves RoomManager
                        return "Room ID:" + inputs.get(0);
                    }
                }
                break;
            case "View Rooms":
                break;
        }
        return "valid";
    }

    public List<String> getRoomsForGUI() {
        return RM.getRoomsForGUI();
    }
    private String checkEventID(String input) {
        if (!EM.checkEventExists(Integer.parseInt(input))){
            return "Invalid Event ID";
        }
        return "valid";
    }

    private String checkSpeakerName(String input) {
        if (!SM.checkSpeakerExists(input)){
            return "Invalid username";
        }
        return "valid";
    }
    private String checkTime(String input) {
        if (0 > Integer.parseInt(input) || Integer.parseInt(input) > 23){
            return "Invalid time (needs to be 0-23)";
        }
        return "valid";
    }
    private String checkEventDuration(String input) {
        if (Integer.parseInt(input) <= 0){
            return "Enter a number greater than 0";
        }
        return "valid";
    }

    private String checkRoomID(String input) {
        if (!RM.checkRoomExists(Integer.parseInt(input))){
            return "Invalid Room ID";
        }
        return "valid";
    }

    private String checkCapacity(String input) {
        if (Integer.parseInt(input) < 0){
            return "Enter a number greater than 0";
        }
        return "valid";
    }

    private boolean cannotParseToInt(String input){
        try{
            Integer.parseInt(input);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private String checkRoom(int roomID, int eventID){
        // Room already booked
        if (RM.getRoomByID(roomID).checkEventExistsAtTime(
                EM.getEventFromID(eventID).getEventTime(),
                EM.getEventFromID(eventID).getEventDuration())){
            return "1";
        }
        // Event Capacity > Room Capacity
        else if ((Integer.parseInt(EM.getEventFromID(eventID).getRestrictions().get("Capacity").get(0)) >
                        RM.getRoomByID(roomID).getRoomCapacity())){
            return "2";
        }
        // E
        else if (!RM.checkRoomMeetsRequirements(RM.getRoomByID(roomID), EM.getEventFromID(eventID))){
            return "3";
        }
        return "valid";
    }
    /**
     * Checks if the user is already attending an event with overlapping time
     * @param user The user being added to an event
     * @param eventID The Event
     * @return True iff the user is already attending one; else return False
     */
    public boolean checkAlreadySpeakingEvent(UserData user, int eventID) {
        int eventStart = (int) EM.getEventFromID(eventID).getEventTime();
        int eventEnd = (int) EM.getEventFromID(eventID).getEventTime() + EM.getEventFromID(eventID).getEventDuration();
        for (int attending : SM.getSpeakingEvents(user.getUsername())){
            int attendingStart = (int) EM.getEventFromID(attending).getEventTime();
            int attendingEnd = (int) EM.getEventFromID(attending).getEventTime() +
                    EM.getEventFromID(attending).getEventDuration();
            // Check if the start or end is in others
            if (((attendingStart < eventStart) && (eventStart < attendingEnd)) ||
                    ((attendingStart < eventEnd) && (eventEnd < attendingEnd))) {
                return true;
            }
        }
        return false;
    }

    public List<String> getRecommendedRooms(int eventID){
        return RM.getRecommendedRooms(EM.getEventFromID(eventID));

    }
}

