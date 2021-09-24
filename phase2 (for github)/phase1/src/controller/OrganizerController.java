package controller;

import entity.Event;
import entity.Room;
import entity.UserData;
import gateway.LocalFileGateway;
import usecase.EventManager;
import usecase.RoomManager;
import usecase.EventBrowser;
import usecase.UserManager;
import usecase.SpeakerManager;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class OrganizerController extends AccountController implements Accessible{

    private EventManager EM;
    private RoomManager RM;
    private EventBrowser EB;
    private UserManager UM;
    private SpeakerManager SM;
    private LocalFileGateway GW;
    List<String> OrganizerOptions = new ArrayList<>(Arrays.asList("1","2","3","4","5","6"));

    /**
     * Constructs an OrganizerController assigned to a specific conference.
     * @param conference the conference the user is attending.
     * @param userinfo the userinfo for the organizer.
     */
    public OrganizerController(ConferenceController conference, UserData userinfo){
        super(conference, userinfo);
        EM = conference.getEventManager();
        RM = conference.getRoomManager();
        EB = conference.getEventBrowser();
        UM = conference.getUserManager();
        SM = conference.getSpeakerManager();
        GW = conference.getLocalFileGateway();
        this.AccessibleOptions = OrganizerOptions;
    }
    /**
     * Generates an event with a speaker and adds it to the schedule.
     * @param name the name of the event
     * @param time the time of the event
     * @param speaker the username of the Speaker
     */
    public boolean generateEvent(String name, int time, String speaker){
        return EM.generateEvent(name, time, speaker);
    }

    /**
     * Generates an event and adds it to the schedule.
     * @param name the name of the event
     * @param time the time of the event
     */
    public boolean generateEvent(String name, int time){
        return EM.generateEvent(name, time);
    }

    /**
     * Removes an event from the schedule.
     * @param event the id of an event
     */
    public boolean removeEvent(int event){
        return EM.removeEvent(event);
    }
    /**
     * Removes an event from the schedule.
     * @param event the event the user is removing
     */
    public boolean removeEvent(Event event){
        if (EM.removeEvent(event)){
            if (RM.getRoomByEventIDandTime(event.getEventID(), event.getEventTime()) != null){
                RM.getRoomByEventIDandTime(event.getEventID(), event.getEventTime()).clearSlot(event.getEventTime());
            }
            return true;
        }
        return false;
    }

    /**
     * Adds a speaker to the event.
     * @param event the id of the event
     * @param speaker the username of the speaker
     * @return returns true if the speaker was added
     */
    public boolean addSpeaker(int event, String speaker) {
        // From the UI we know the speaker does not have another event at this time
        // Checks if the speaker exists and adds the speaker to the Event
        if (EM.addSpeaker(event, speaker)) {
            UM.getUserByID(speaker).addEventID(event);
            SM.addEventSpeakingIn(speaker, event);
            // Checks if the Event has a room booked and adds the Speaker to the Room's Speaker schedule
            try {
                RM.addSpeakerToRoom(RM.getRoomByEventIDandTime(event, EB.getEventFromID(event).getEventTime()),
                        EB.getEventFromID(event).getEventTime(),
                        Conference.getSpeakerManager().getSpeakerByID(speaker));
            }
            // Room does not exist
            catch(NullPointerException e){
                return true;
            }
            // Speaker was added
            return true;
        }
        return false;
    }
    public boolean removeSpeaker(int event, String speaker){
        if (EM.removeSpeaker(event, speaker)){
            UM.getUserByID(speaker).removeEventID(event);
            SM.cancelEventSpeakingAt(event, speaker);
            return true;
        }
        return false;
    }
//    /**
//     * Adds a speaker to the event.
//     * @param event the Event the speaker is being added to
//     * @param speaker the username of the speaker
//     * @return returns true if the speaker was added
//     */
//    public boolean addSpeaker(Event event, String speaker){
//        return EM.addSpeaker(event, speaker);
//    }

    /**
     * Generates a room with a certain capacity.
     * @param capacity the capacity of the room
     * @return True iff the room was generated, else returns False.
     */
    public boolean generateRoom(int capacity){
        return RM.generateRoom(capacity);
    }

    /**
     * A UI for the Organizer options.
     */
    public void subMenuUI() {
        boolean run = true;
        while (run) {
            System.out.println("=== Organizer Menu ===");
            System.out.println("[1] - Create an Event");
            System.out.println("[2] - Remove an Event");
            System.out.println("[3] - Add a Speaker to an Event");
            System.out.println("[4] - Remove a Speaker from an Event");
            System.out.println("[5] - Create a Room");
            System.out.println("[6] - Set a Room for an Event");
            System.out.println("[7] - View All Rooms");
            System.out.println("[8] - Exit");

            Scanner reader = new Scanner(System.in);
            String input = reader.nextLine();

            switch (input) {
                case "1":
                    createEventUI(reader);

                    GW.SaveEventManager(EM);
                    break;
                case "2":
                    removeEventUI(reader);

                    GW.SaveEventManager(EM);
                    break;
                case "3":
                    addSpeakerUI(reader);
                    break;
                case "4":
                    removeSpeakerUI(reader);
                    break;
                case "5":
                    createRoomUI(reader);

                    GW.SaveRoomManager(RM);
                    break;
                case "6":
                    setEventRoomUI(reader);
                    break;
                case "7":
                    viewRoomsUI(reader);
                case "8":
                    System.out.println("Returning to the main menu...");
                    run = false;
                    break;

            }

        }
    }

    /**
     * A UI to generate a event.
     * @param reader a Scanner to read the console.
     */
    private void createEventUI(Scanner reader){
        String speaker;
        String time;
        while (true) {
            System.out.println("Press enter at any point to return to the Organizer Menu.");
            System.out.println("Please enter the name of your Event.");
            String name = reader.nextLine();
            if (name.equals("")) {return;}

            do {
                System.out.println("Please enter the time of the Event (a number from 0-23)");
                System.out.println("For example, 12 am -> [0], 3 pm -> [15], 11pm -> [23]");
                time = reader.nextLine();

                if (time.equals("")) {
                    System.out.println("Returning to the menu...");
                    return;
                }
            } while (cannotParseToInt(time) || notValidTime(Integer.parseInt(time)));

            do {
                System.out.println("Please enter the name of the speaker for the Event. If there is no" +
                        "speaker, type [None].");
                speaker = reader.nextLine();
                if (speaker.toLowerCase().equals("none")){
                    break;
                }
                else if (speaker.equals("")){
                    System.out.println("Returning to the menu...");
                    return;
                }
            } while (notValidSpeaker(speaker) || !checkSpeakingTimes(speaker, Integer.parseInt(time)));

            boolean eventAdded;
            if (speaker.toLowerCase().equals("none")) {
                eventAdded = this.generateEvent(name, Integer.parseInt(time));
            } else {
                eventAdded = this.generateEvent(name, Integer.parseInt(time), speaker);
            }

            if (eventAdded) {
                String prompt;
                System.out.println("Successfully added the event. Press enter to exit to the menu.");
                System.out.printf("Your new event's id is %s. To add your event to a room, press [1]: ",
                        EB.browseEvents().get(EB.browseEvents().size() - 1).getEventID());

                do{
                    prompt = reader.nextLine();
                    if (prompt.equals("")) {
                        System.out.println("Returning to the menu...");
                        break;
                    }
                    else if (prompt.equals("1")) {
                        setEventRoomUI(reader);
                        break;
                    }
                }while(true);
                    return;
            } else {
                System.out.println("Event was not added. Please try again.");
            }
        }
    }

    /**
     * A UI to remove an event.
     * @param reader a Scanner to read the console.
     */
    private void removeEventUI(Scanner reader){
        SimpleEntry<String, String> eventmap;
        while (true) {
            System.out.println("Press enter at any point to return to the Organizer Menu.");
            eventmap = checkEventUI(reader);
            if (eventmap.getKey().equals("valid")){
                if (this.removeEvent(EB.getEventFromID(Integer.parseInt(eventmap.getValue())))) {
                    System.out.println("Event successfully removed. Returning to menu...");
                    break;
                } else {
                    System.out.println("The event was not removed. Please try again.");
                }
            }
            else if (eventmap.getKey().equals("exit")) {
                break;
            }
        }
    }

    /**
     * A UI to add a speaker to an event.
     * @param reader a Scanner to read the console.
     */
    private void addSpeakerUI(Scanner reader){
        SimpleEntry<String, String> eventmap;
        while (true) {
            String speaker3;
            System.out.println("Press enter at any point to return to the Organizer Menu.");
            do {
                System.out.println("Enter the name of the speaker.");
                speaker3 = reader.nextLine();
                if (speaker3.equals("")){
                    System.out.println("Returning to the menu...");
                    return;
                }
            } while (notValidSpeaker(speaker3));

            do {
                eventmap = checkEventUI(reader);
                if (eventmap.getKey().equals("exit")){
                    return;
                }
            } while (eventmap.getKey().equals("invalid") ||
                    !checkSpeakingTimes(speaker3,
                            EB.getEventFromID(Integer.parseInt(eventmap.getValue())).getEventTime()));

            if (this.addSpeaker(Integer.parseInt(eventmap.getValue()), speaker3)) {
                System.out.println("Speaker successfully added. Returning to the menu...");
                break;
            } else {
                System.out.println("Invalid event id or speaker name. Please try again.");
            }
        }
    }

    /**
     * A UI to add a speaker to an event.
     * @param reader a Scanner to read the console.
     */
    private void removeSpeakerUI(Scanner reader){
        SimpleEntry<String, String> eventmap;
        while (true) {
            String speaker3;
            System.out.println("Press enter at any point to return to the Organizer Menu.");
            do {
                System.out.println("Enter the name of the speaker.");
                speaker3 = reader.nextLine();
                if (speaker3.equals("")){
                    System.out.println("Returning to the menu...");
                    return;
                }
            } while (notValidSpeaker(speaker3));

            do {
                eventmap = checkEventUI(reader);
                if (eventmap.getKey().equals("exit")){
                    return;
                }
            } while (eventmap.getKey().equals("invalid"));

            if (this.removeSpeaker(Integer.parseInt(eventmap.getValue()), speaker3)) {
                System.out.println("Speaker successfully removed. Returning to the menu...");
                break;
            } else {
                System.out.println("The speaker is currently not speaking at this event. Please try another event ID.");
            }
        }
    }
    /**
     * A UI to create a room.
     * @param reader a Scanner to read the console.
     */
    private void createRoomUI(Scanner reader){
        while(true){
            System.out.println("Press enter at any point to exit to the Organizer menu.");
            System.out.println("Please enter the capacity for the room (must be greater than 0): ");
            String capacity = reader.nextLine();

            if (capacity.equals("")){
                System.out.println("Returning to the menu...");
                return;
            }
            else if (cannotParseToInt(capacity) || Integer.parseInt(capacity) <= 0){
                System.out.println("Invalid entry. Please enter a number greater than 0.");
            }
            else{
                if (generateRoom(Integer.parseInt(capacity))) {
                    System.out.println("Room added. Returning to the menu...");
                    return;
                }
                else{
                    System.out.println("Something went wrong. Please try again.");
                }
            }
        }
    }

    /**
     * A UI to set a room for an event.
     * @param reader a Scanner to read the console.
     */
    private void setEventRoomUI(Scanner reader){
        SimpleEntry<String, String> eventmap;
        String room;
        while(true) {
            System.out.println("Press enter at any point to exit to the Organizer menu.");
            do {
                eventmap = checkEventUI(reader);
                if (eventmap.getKey().equals("exit")){
                    return;
                }
            } while (eventmap.getKey().equals("invalid"));

            do {
                System.out.println("Please enter the room ID of the room you want to set the Event to: ");
                room = reader.nextLine();
                if (room.equals("")) {
                    System.out.println("Returning to the menu...");
                    return;
                }
            }while (!checkRoom(room, eventmap.getValue()));

            if (RM.addEventToRoom(Integer.parseInt(room),
                    EB.getEventFromID(Integer.parseInt(eventmap.getValue())).getEventTime(),
                    EB.getEventFromID(Integer.parseInt(eventmap.getValue())))){
                System.out.println("Successfully booked event room. Returning to menu...");
                return;
            }
            else{
                System.out.println("There was an error. Please try again.");
            }

        }

    }

    /**
     * A UI to view the list of available rooms.
     * @param reader a Scanner to read the console.
     */
    public void viewRoomsUI(Scanner reader){
        if (RM.getRooms().isEmpty()){
            System.out.println("Rooms: No Rooms available.");
        }
        else {
            System.out.println("Rooms:");
            System.out.println("Room <ID> | room capacity");
            System.out.println("-------------------------------");
            for (Room room : RM.getRooms()){
                System.out.printf("Room <%s>: %s", room.getRoomID(), room.getRoomCapacity());
            }
        }
        String input;
        System.out.println("\nPress enter to exit to the Organizer menu.");
        do{
            input = reader.nextLine();
        }while(!input.equals(""));
    }

    /**
     * A method to check if a string cannot be parsed into an integer.
     * @param input an inputted string
     * @return True iff the input can be parsed into an int, otherwise returns False.
     */
    private boolean cannotParseToInt(String input){
        try{
            Integer.parseInt(input);
            return false;
        }
        catch(NumberFormatException e){
            return true;
        }
    }

    /**
     * Checks if a number is not a number between 0-23 i.e. military time.
     * @param num a number
     * @return True iff num is not between 0-23, otherwise returns False.
     */
    private boolean notValidTime(int num){
        if (num > 23 || num < 0){
            System.out.println("Invalid input. Please state a number between 0-23.");
            return true;
        }
        return false;
    }

    /**
     * Checks if a speaker is not a valid user in the system.
     * @param name the name of the speaker
     * @return True iff the speaker is a valid user, otherwise returns False.
     */
    private boolean notValidSpeaker(String name){
        if (Conference.getSpeakerManager().checkSpeakerExists(name)) {
            return false;
        }
        System.out.println("Invalid speaker name. Please try again");
        return true;
    }

    /**
     * Checks if a speaker can attend an event at a certain time.
     * @param speaker the username of the speaker
     * @param time the time of the proposed event
     * @return True iff the speaker does not have an Event they're speaking at the same time as the proposed event,
     * otherwise returns False.
     */
    private boolean checkSpeakingTimes(String speaker, int time){
        for (int eventID: SM.getSpeakingEvents(speaker)){
            if (time == EB.getEventFromID(eventID).getEventTime()){
                System.out.println("You already signed up to an event at the same time. Please try another event.");
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a room is available for an event.
     * @param room the room ID for the room
     * @param eventID the event ID of the event
     * @return True iff the room is a valid number, exists and there is no other event using the room at the time,
     * otherwise returns False.
     */
    public boolean checkRoom(String room, String eventID){
        if (cannotParseToInt(room)){
            System.out.println("Invalid input. Please enter a number.");
            return false;
        }
        else if (!RM.checkRoomExists(Integer.parseInt(room))) {
            System.out.println("Room does not exist. Please enter a valid room ID.");
            return false;
        }
        else if (RM.getRoomByID(Integer.parseInt(room)).checkEventExistsAtTime(
                EB.getEventFromID(Integer.parseInt(eventID)).getEventTime())){
            System.out.println("Room is already booked. Please try another room ID.");
            return false;
        }
        return true;
    }

    /**
     * A UI to check if the input is a valid event ID
     * @param reader a Scanner to read the console.
     * @return returns a key value pair with the validity of the input, and the value
     */
    public SimpleEntry<String, String> checkEventUI(Scanner reader) {
        System.out.println("Please enter the event ID of the Event: ");
        String event = reader.nextLine();
        // If the user pressed enter
        if (event.equals("")) {
            System.out.println("Returning to the menu...");
            return new AbstractMap.SimpleEntry<>("exit", event);
        }
        // If the input is not an int
        else if (cannotParseToInt(event)) {
            System.out.println("Invalid input. Please try again. ");
            return new AbstractMap.SimpleEntry<>("invalid", event);
        }
        // If the event does not exist
        else if (EB.getEventFromID(Integer.parseInt(event)) == null ||
                !EB.checkEventExists(Integer.parseInt(event))){
            System.out.println("This event does not exist. Please try again.");
            return new AbstractMap.SimpleEntry<>("invalid", event);
        }
        // The input is a valid event ID
        return new AbstractMap.SimpleEntry<>("valid", event);
    }
}
