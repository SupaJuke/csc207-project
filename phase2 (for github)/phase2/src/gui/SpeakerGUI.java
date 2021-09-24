package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import presenter.SpeakerPresenter;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * UI containing Speaker options for the Speaker to interact with
 */
public class SpeakerGUI extends AccountGUI implements Viewable{
    SpeakerPresenter SP;
    Stage window;

    //formatting
    private final int spacing = 10;
    private final int width = 600;
    private final int height = 400;

    /**
     * Setter for SP
     * @param SP SpeakerPresenter object
     */
    public void setPresenter(SpeakerPresenter SP) {
        this.SP = SP;
    }

    /**
     * Method is called from Speaker Submenu button
     * @param primaryStage the Stage that the GUI uses
     */
    public void subMenuUI(Stage primaryStage){
        window = primaryStage;
        createOptionsUI(primaryStage);
    }


    /**
     * Adds Speaker SubMenu button in Account options
     * @param stage passes stage of program
     * @param options Hashmap containing default Account options
     */
    public void createOptions(Stage stage, Map<String, Runnable> options){
        super.createOptions(stage, options);
        options.put("Speaker SubMenu", () -> subMenuUI(stage));
    }

    /**
     * Changes stage to UI showing Speaker options
     * @param primaryStage stage of program
     */
    private void createOptionsUI(Stage primaryStage){
        VBox layout = new VBox(spacing);
        Button[] buttons = new Button[4];

        for(int i=0; i<4; i++)
            buttons[i] = new Button();
        buttons[0].setText("Book to speak at an event");
        buttons[1].setText("View events speaking at");
        buttons[2].setText("Speaker message menu");
        buttons[3].setText("Return");

        //uses lambda function for JavaFX event handling
        buttons[0].setOnAction((event) -> createSpeakUI(primaryStage));
        buttons[1].setOnAction((event) -> viewEventsSpeakingUI(primaryStage));
        buttons[2].setOnAction((event) -> SP.getMessageGUI().accessUI(primaryStage));
        buttons[3].setOnAction((event) -> this.accessUI(primaryStage));

        Text title = new Text("Speaker Menu");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        layout.getChildren().add(title);

        layout.setAlignment(Pos.CENTER);
        for (Button button : buttons) {
            button.setMinSize(200, 30);
            button.setMaxSize(200, 30);
            layout.getChildren().add(button);}


        window.setScene(new Scene(layout, width, height));
    }

    /**
     * Changes stage to speaking at an event UI
     * @param primaryStage stage in the program
     */
    private void createSpeakUI(Stage primaryStage) {
        VBox layout = new VBox(spacing);
        Label label = new Label();
        TextField textField = new TextField();
        textField.setMaxSize(200, 100);
        Button confirmBtn = new Button();
        Button cancelBtn = new Button();

        label.setText("Please enter an event ID");

        confirmBtn.setText("Confirm");
        cancelBtn.setText("Cancel");

        confirmBtn.setOnAction((event) -> label.setText(SP.speakerEventHelper(textField.getText())));
        cancelBtn.setOnAction((event) -> createOptionsUI(primaryStage));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(label);
        layout.getChildren().add(textField);
        layout.getChildren().add(confirmBtn);
        layout.getChildren().add(cancelBtn);

        window.setScene(new Scene(layout, width, height));
    }

    /**
     * Changes stage to view events speaking at UI (in the format "Speaking Events: [ID] | <Event Name>")
     * @param primaryStage stage of program
     */
    private void viewEventsSpeakingUI(Stage primaryStage){
        VBox layout = new VBox(spacing);

        Label label = new Label();
        label.setText("Speaking Events: \n[ID] | <Event Name>");

        Button returnButton = new Button("Return");
        returnButton.setOnAction((event -> createOptionsUI(primaryStage)));

        ListView<String> list = new ListView<>();
        list.setMaxSize(400, 200);
        for(int eventID : SP.getSpeakingEvents(SP.getUsername())){
            list.getItems().add("["+eventID+"] - "+ SP.getEM().getEventFromID(eventID).getEventTitle());
        }
        Text title = new Text("Event List");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(title, label);
        layout.getChildren().add(list);
        layout.getChildren().add(returnButton);

        window.setScene(new Scene(layout, width, height));
    }
}
