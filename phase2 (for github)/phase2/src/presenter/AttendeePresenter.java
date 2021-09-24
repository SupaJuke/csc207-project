package presenter;

import controller.ConferenceController;
import gui.AccountGUI;
import gui.MessageGUI;
import gui.Viewable;
import javafx.stage.Stage;
import presenter.messages.AttendeeMessagePresenter;

public class AttendeePresenter extends AccountPresenter{

    /**
     * Constructs a AttendeeController assigned to a specific conference
     * @param conference This is the conference the attendee is attending.
     * @param username  This is the userinfo for the specific attendee.
     */
    public AttendeePresenter(ConferenceController conference, String username, Stage PM, Viewable LG, AccountGUI AttendeeGUI) {
        super(conference, username, PM, LG, AttendeeGUI);

        messagePresenter = new AttendeeMessagePresenter(conference, username);
        messageGUI = new MessageGUI(messagePresenter);
    }


}
