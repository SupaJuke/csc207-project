package gui;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenter.AccountPresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AccountGUI extends BasicGUI implements Viewable{
    AccountPresenter AP;

    Map<String, Runnable> options = new LinkedHashMap<>();

    public void setPresenter(AccountPresenter accountPresenter){
        AP = accountPresenter;
    }

    @Override
    public void accessUI(Stage stage) {
        stage.setTitle("Conference");
        createOptions(stage, options);

        List<Button> buttons = new ArrayList<>();
        for (String option : options.keySet()) {
            Button button = new Button(option);
            button.setMinSize(200, 30);
            button.setMaxSize(200, 30);
            button.setOnAction(e -> options.get(option).run());
            buttons.add(button);
        }

        stage.setScene(createButtonScene(new Text(AP.getDisplayName() + "'s Account Menu"), buttons));
    }

    public void createOptions(Stage stage, Map<String, Runnable> options){
        options.put("Messages", () -> AP.getMessageGUI().accessUI(stage));
        options.put("Events", () -> AP.getEventGUI().accessUI(stage));
        options.put("Profile", () -> AP.getUserGUI().accessUI(stage));
        options.put("Friends", () -> AP.getFriendGUI().accessUI(stage));
        options.put("Sign Out", () -> AP.getLoginGUI().accessUI(stage));
    }

    public Button makeExitButton(Stage stage) {
        Button button = new Button("Back");
        button.setOnAction(e -> this.accessUI(stage));
        return button;
    }

}
