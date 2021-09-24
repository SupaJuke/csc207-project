package controller;

import entity.UserData;
import gateway.LocalFileGateway;
import usecase.EventBrowser;
import usecase.EventManager;
import usecase.RoomManager;

import java.util.ArrayList;
import java.util.Scanner;

public class EventController implements Accessible{

    private EventBrowser EB;
    private UserData userData;
    private EventManager EM;
    private RoomManager RM;

    public EventController(EventBrowser eventBrowser, UserData userinfo, EventManager EM, RoomManager RM){
        EB = eventBrowser;
        userData = userinfo;
        this.EM = EM;
        this.RM = RM;
    }

    /**
     * A UI for the Event options.
     */
    @Override
    public void accessUI() {
        boolean run = true;
        while(run) {
            System.out.println("=== Event Menu ===");
            System.out.println("[1] - Browse all Events");
            System.out.println("[2] - View the Events you signed up to");
            System.out.println("[3] - Sign up for an Event");
            System.out.println("[4] - Cancel attending an Event");
            System.out.println("[5] - Exit");

            Scanner reader = new Scanner(System.in);
            String input = reader.nextLine();

            switch (input) {
                case "1":
                    this.browsingUI(reader);
                    break;
                case "2":
                    System.out.println("Signed up Events:");
                    System.out.println(userData.getEventIDs());
                    System.out.println("Press enter to return to the Event menu.");
                    String input2;
                    do {
                        input2 = reader.nextLine();
                    } while (!input2.equals(""));
                    break;
                case "3":
                    this.signupUI(reader);
                    break;
                case "4":
                    this.cancelUI(reader);
                    break;
                case "5":
                    run = false;
                    break;
            }

        }
        System.out.println("Returning to the main menu...");
    }

    /**
     * A UI for browsing events.
     * @param reader a Scanner to read the console.
     */
    private void browsingUI(Scanner reader){
        while(true) {
            System.out.println("=== Schedule Menu ===");
            System.out.println("Schedule:");
            System.out.println("[ID] | <Event Name>");
            System.out.println("-------------------------------");
            printListOfEvents();
            System.out.println("\n[1] - View an Event's details");
            System.out.println("[2] - Return");
            String input2 = reader.nextLine();
            if (input2.equals("1")) {
                viewEventInfoUI(reader);
                break;
            }
            else if (input2.equals("2")) {
                return;
            }
        }
    }

    /**
     * Prints the list of events currently in EventBrowser
     */
    public void printListOfEvents(){
        ArrayList<String> events = EB.getScheduleInfo();
        for (String event : events){
            System.out.println(event);
        }
        if (events.size() == 0){
            System.out.println("[There are currently no events scheduled]");
        }
    }

    /**
     * A UI to view a specific Event's info.
     * @param reader a Scanner to read the console.
     */
    public void viewEventInfoUI(Scanner reader){
        while(true){
            System.out.println("Press enter to return to the Schedule Menu.");
            System.out.println("Please enter the event ID of the Event: ");
            String event = reader.nextLine();

            if (event.equals("")){
                return;
            }
            else if (cannotParseToInt(event)) {
                System.out.println("Invalid input. Please try again.");
            }
            else {
                ArrayList<Object> eventinfo = EB.getEventInfo(Integer.parseInt(event));
                if (eventinfo.isEmpty()){
                    System.out.println("This event does not exist. Please enter a valid event ID.");
                }
                else {
                    if (eventinfo.get(2).equals("")){
                        System.out.printf("Title: %1$s, Time: %2$s, Speaker(s): TBA \n",
                                eventinfo.get(0), eventinfo.get(1));
                    }
                    else {
                        System.out.printf("Title: %1$s, Time: %2$s, Speaker(s): %3$s%n \n",
                                eventinfo.get(0), eventinfo.get(1), eventinfo.get(2));
                    }
                    System.out.println("Press enter to return to the Schedule Menu.");
                    String nextinput;
                    do {
                        nextinput = reader.nextLine();
                    }
                    while (!nextinput.equals(""));
                    return;
                }
            }
        }
    }

    /**
     * A UI for signing up to an event.
     * @param reader a Scanner for the console.
     */
    private void signupUI(Scanner reader){

        while(true){
            System.out.println("Press enter to return to the Event Menu. ");
            System.out.println("Please enter the event ID of the event you want to sign up to.");
            String event = reader.nextLine();
            if (event.equals("")){
                return;
            }
            else if (cannotParseToInt(event)){
                System.out.println("Invalid input. Please enter a number. ");
            }

            else if (EB.getEventFromID(Integer.parseInt(event)) == null){
                System.out.println("The Event does not exist.");
            }
            else{
                int eventID = Integer.parseInt(event);
                if (userData.getEventIDs().contains(eventID)){
                  System.out.println("You are already signed up to an event at this event's time.");
                }

                else if (checkRoom(eventID).equals("full")){
                    System.out.println("The Event is at full capacity.");
                }
                else if (EM.addEventID(userData, eventID)){
                    if (checkRoom(eventID).equals("exists")){
                    RM.addUserToRoom(RM.getRoomByEventIDandTime(eventID, EB.getEventFromID(eventID).getEventTime()),
                                            EB.getEventFromID(eventID).getEventTime(), userData);
                    }
                    System.out.println("Event successfully added.");
                    break;
                }
                else {
                    System.out.println("You have already signed up for this Event.");
                }
            }
        }
    }

    /**
     * A UI for cancelling an event attendance.
     * @param reader a Scanner for the console.
     */
    private void cancelUI(Scanner reader){
        while(true){
            System.out.println("Press enter to return to the Event menu. ");
            System.out.println("Please enter the event ID of the event you want to cancel attending.");
            String event = reader.nextLine();
            if (event.toLowerCase().equals("")){
                break;
            }
            else if (cannotParseToInt(event)){
                System.out.println("Invalid input. Please enter a number. ");
            }
            else{
                if (userData.removeEventID(Integer.parseInt(event))){
                    System.out.println("Event attendance cancelled.");
                    break;
                }
                else {
                    System.out.println("Event was not removed. Please check if you have entered a valid event ID or" +
                            "if you were signed up for the event.");
                }
            }
        }
    }

    public String checkRoom(int eventID){
        try {
            // Room is at max capacity
             if (RM.getRoomByEventIDandTime(eventID, EB.getEventFromID(eventID).getEventTime()).getRoomCapacity()
                     <= RM.getRoomByEventIDandTime(eventID,
                     EB.getEventFromID(eventID).getEventTime()).getNumAttendeeAtTime(
                     EB.getEventFromID(eventID).getEventTime())){
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
    /**
     * A method to check if a string cannot be parsed into an integer.
     * @param input an inputted string
     * @return returns true if the input can be parsed into an int
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
}
