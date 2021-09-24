package controller;

import entity.SpeakerData;
import entity.UserData;
import usecase.EventBrowser;
import usecase.EventManager;
import usecase.SpeakerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SpeakerController extends AccountController {

    private final SpeakerManager SM;
    private final EventManager EM;
    private final EventBrowser EB;
    private final SpeakerMessageController SMC;

    List<String> SpeakerOptions = new ArrayList<>(Arrays.asList("1","2","3","4","5", "7"));
    /**
     * Constructs a AttendeeController assigned to a specific conference
     *
     * @param conference This is the conference the attendee is attending.
     * @param userinfo   This is the userinfo for the specific attendee.
     */
    public SpeakerController(ConferenceController conference, UserData userinfo, EventManager EM, EventBrowser EB) {
        super(conference, userinfo);
        SM = conference.getSpeakerManager();
        this.EB = EB;
        this.EM = EM;
        this.SMC = new SpeakerMessageController(conference, (SpeakerData)userinfo); //Convert UserData to SpeakerData
        this.AccessibleOptions = SpeakerOptions;
    }

    /**
     * Accesses SpeakerController UI
     */
    public void subMenuUI() {
        boolean run = true;
        while(run) {
            System.out.println("=== Speaker Menu ===");
            System.out.println("[1] - Speak at an Event");
            System.out.println("[2] - View Events Speaking at");
            System.out.println("[3] - Speaker message Menu");
            System.out.println("[4] - Exit");

            Scanner reader = new Scanner(System.in);
            String in = reader.nextLine();

            switch(in){
                case "1":
                    while(true){
                        System.out.println("Please enter a event id");
                        System.out.println("Press enter to return to Speaker options");
                        in = reader.nextLine();

                        if(in.equals(""))
                            break;

                        try {
                            if (!EM.checkEventExists(Integer.parseInt(in))) {
                                System.out.println("That event does not exist.");
                            } else if (EM.addEventID(userData, Integer.parseInt(in))) {
                                addEventSpeaking(userData.getUsername(), Integer.parseInt(in));
                                System.out.println("Successfully signed up for the event");
                                break;
                            } else System.out.println("You are already signed up for the event.");
                        }
                        catch(NumberFormatException e){
                            System.out.println("That event is not valid. Please try again");
                        }
                    }
                    break;
                case "2":
                    System.out.println("Speaking Events:");
                    System.out.println("[ID] | <Event Name>");
                    System.out.println("-------------------------------");
                    for(int eventID : getSpeakingEvents(userData.getUsername())){
                        System.out.println("["+eventID+"] - "+EB.getEventFromID(eventID).getEventTitle());
                    }
                    break;
                case "3":
                    SMC.accessUI();
                    break;
                case "4":
                    run = false;
                    break;
            }
        }
    }

    /**
     * Adds speaking event for Speaker
     * @param username username of Speaker
     * @param eventID id of the event
     */
    private void addEventSpeaking(String username, int eventID) {
        SM.addEventSpeakingIn(username, eventID);
    }

    /**
     *
     * @param username username of Speaker
     * @return list of events Speaker is speaking at
     */
    private List<Integer> getSpeakingEvents(String username) {
        return SM.getSpeakingEvents(username);
    }

}
