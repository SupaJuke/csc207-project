package presenter;

import controller.ConferenceController;
import gui.AccountGUI;
import gui.MessageGUI;
import gui.Viewable;
import javafx.stage.Stage;
import presenter.messages.AttendeeMessagePresenter;

public class VIPPresenter extends AccountPresenter{

    /**
     * A VIPPresenter that is assigned to a conference
     * @param conference the conference controller assigned to the conference
     * @param username the VIP
     * @param PM The Stage used for the GUI
     * @param LG The viewable that uses the Stage
     * @param VIPGUI The GUI for the presenter
     */
    public VIPPresenter(ConferenceController conference, String username, Stage PM, Viewable LG, AccountGUI VIPGUI) {
        super(conference, username, PM, LG, VIPGUI);

        messagePresenter = new AttendeeMessagePresenter(conference, username);
        messageGUI = new MessageGUI(messagePresenter);
    }

}

