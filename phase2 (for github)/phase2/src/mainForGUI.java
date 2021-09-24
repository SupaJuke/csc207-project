import controller.ConferenceController;
import gui.LoginGUI;
import javafx.application.Application;
import javafx.stage.Stage;
import presenter.LoginPresenter;

public class mainForGUI extends Application {
    public static void test(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Conference");

        ConferenceController conference = new ConferenceController();
        LoginGUI loginGUI = new LoginGUI();
        LoginPresenter loginPresenter = new LoginPresenter(conference);
        loginGUI.setLoginPresenter(loginPresenter);

        loginGUI.accessUI(primaryStage);

        primaryStage.show();
    }

}

