package controller;

import entity.UserData;

import java.util.*;

public class AttendeeController extends AccountController implements Accessible{

    List<String> AttendeeOptions = new ArrayList<String>(Arrays.asList("1","2","3","4","5"));

    /**
     * Constructs a AttendeeController assigned to a specific conference
     *
     * @param conference This is the conference the attendee is attending.
     * @param userinfo   This is the userinfo for the specific attendee.
     * @va messageController
     */
    public AttendeeController(ConferenceController conference, UserData userinfo) {
        super(conference, userinfo);
        this.AccessibleOptions = AttendeeOptions;
    }


}
