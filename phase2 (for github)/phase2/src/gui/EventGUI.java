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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenter.EventPresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EventGUI extends BasicGUI implements Viewable{

    private EventPresenter EP;
    private Viewable view;

    Map<String, Runnable> options = new LinkedHashMap<>();

    /**
     * A setter for the event presenter and the viewable GUI
     * @param eventPresenter the presenter for the events
     * @param view the GUI of the account
     */
    public void setPresenters(EventPresenter eventPresenter, Viewable view){
        EP = eventPresenter;
        this.view = view;
    }

    /**
     * A method that allows GUIs to access the event menu
     * @param stage the Stage that the GUI uses
     */
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

        stage.setScene(createButtonScene(new Text("Event Menu"), buttons));
    }

    /**
     * A method that makes the exit button for a scene
     * @param stage The stage that the GUI uses
     * @return returns a Button for exit
     */
    @Override
    public Button makeExitButton(Stage stage) {
        Button button = new Button("Return");
        button.setOnAction(e -> this.accessUI(stage));
        return button;
    }

    private void createOptions(Stage stage, Map<String, Runnable> options){
        options.put("Browse all Events", () -> browsingGUI(stage));
        options.put("View the Events you signed up to", () -> viewMyEventsGUI(stage));
        options.put("Exit", () -> view.accessUI(stage));
    }

    private void browsingGUI(Stage primaryStage){

        VBox layout = new VBox(spacing);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("Event List");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        layout.getChildren().add(title);

        ListView<String> list = new ListView<>();
        list.setMaxSize(400, 100);
        for (String event : EP.getListOfEvents()) {
            list.getItems().add(event);
        }
        Label label = new Label("Enter an Event ID in order to check its details");
        TextField textField = new TextField();
        textField.setMaxSize(200, 200);

        Button ExtraDetailsBtn = new Button();
        ExtraDetailsBtn.setText("View Details");
        ExtraDetailsBtn.setOnAction((event) -> {
            try {
                int eventID = Integer.parseInt(textField.getText());
                if (EP.getEM().checkEventExists(eventID)) {
                    showEventDetailsUI(primaryStage, eventID);}
                else {
                    label.setText("Invalid ID. Please try again.");
                }
            }
            catch (NumberFormatException e) {
                label.setText("Invalid ID. Please try again.");
            }
        });
        layout.getChildren().add(list);
        layout.getChildren().addAll(label, textField, ExtraDetailsBtn, makeExitButton(primaryStage));

        primaryStage.setScene(new Scene(layout, 600, 400));
    }

    private void showEventDetailsUI(Stage primaryStage, int eventID) {

        VBox layout = new VBox(spacing);
        layout.setAlignment(Pos.CENTER);

        Label eventTitle = new Label(EP.getEM().getEventFromID(eventID).getEventTitle());
        eventTitle.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));

        Label eventTime = new Label("Start Time (military time): " +
                EP.getEM().getEventFromID(eventID).getEventTime() + ":00");
        eventTime.setFont(Font.font("Calibri",14));

        Label eventDuration = new Label("Duration: " + EP.getEM().getEventFromID(eventID).getEventDuration()
                + " hour(s)");
        eventDuration.setFont(Font.font("Calibri", 14));

        Map<String, List<String>> restrictions = EP.getEM().getEventFromID(eventID).getRestrictions();

        Label eventRestrictions = new Label();
        eventRestrictions.setText("Restrictions: " + EP.checkRestrictions(restrictions));
        eventRestrictions.setFont(Font.font("Calibri", 14));

        List<String> speakers = EP.getEM().getEventFromID(eventID).getSpeakerUsernames();

        Label eventSpeakers = new Label();
        System.out.println(EP.getEM().getEventFromID(eventID));
        eventSpeakers.setText("Speakers: " + EP.checkSpeakers(speakers));
        eventSpeakers.setFont(Font.font("Calibri", 14));

        Label eventRooms = new Label();
        eventRooms.setText("RoomID: " + EP.checkRoomStatus(eventID));
        eventRooms.setFont(Font.font("Calibri",14));

        Label eventTech = new Label();
        eventTech.setText("Event Technology: " + EP.getEventTechnology(eventID));
        eventTech.setFont(Font.font("Calibri",14));

        Label eventFur = new Label();
        eventFur.setText("Event Furniture: " + EP.getEM().getEventFromID(eventID).getRequirements().get("Furniture"));
        eventFur.setFont(Font.font("Calibri",14));

        Label resultLabel = new Label();
        resultLabel.setFont(Font.font("Calibri",14));

        Button signUpForEventBtn = new Button("Sign Up For This Event");
        signUpForEventBtn.setOnAction((event) -> EP.signUpToEvent(eventID, resultLabel,
                EP.getEM().getEventFromID(eventID).getRoom()));

        Button returnButton = new Button("Return");
        returnButton.setOnAction((event -> browsingGUI(primaryStage)));
        layout.getChildren().addAll(eventTitle, eventTime, eventDuration, eventRestrictions, eventSpeakers,
                eventRooms, eventTech, eventFur,
                signUpForEventBtn, resultLabel, returnButton);
        primaryStage.setScene(new Scene(layout, 600, 400));
    }

    private void viewMyEventsGUI(Stage primaryStage){

        VBox layout = new VBox(spacing);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("My Events Schedule");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        layout.getChildren().add(title);

        ListView<String> list = new ListView<>();
        list.setMaxSize(400, 100);
        for (String eventInfo : EP.viewMyEvents()) {
            list.getItems().add(eventInfo);
        }
        Label label = new Label("Enter an Event ID to cancel attending the event");
        TextField textField = new TextField();
        textField.setMaxSize(200, 200);

        Button CancelForEventBtn = new Button();
        CancelForEventBtn.setText("Cancel for Event");
        CancelForEventBtn.setOnAction(
                (event) -> {
                    if(EP.cancelForEvent(label, textField)) {
                        viewMyEventsGUI(primaryStage);
                    }
        });
        layout.getChildren().add(list);
        layout.getChildren().addAll(label, textField, CancelForEventBtn, makeExitButton(primaryStage));

        primaryStage.setScene(new Scene(layout, 600, 400));

    }



}
