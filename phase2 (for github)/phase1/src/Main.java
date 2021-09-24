import controller.*;

public class Main {
    public static void main(String[] args){
        ConferenceController conference = new ConferenceController();
        LoginController loginController;

        loginController = new LoginController(conference);
        loginController.accessUI();

    }
}
