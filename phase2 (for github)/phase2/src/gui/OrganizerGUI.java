package gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenter.OrganizerPresenter;

import java.util.*;

public class OrganizerGUI extends AccountGUI implements Viewable {
    OrganizerPresenter OP;
    OrganizerSceneMaker OSM;
    public OrganizerGUI(){
        OSM = new OrganizerSceneMaker(defaultWidth, defaultHeight, spacing);
        OSM.setGUI(this);
    }
    public void setPresenter(OrganizerPresenter organizerPresenter){
        OP = organizerPresenter;
    }

    public void createOptions(Stage stage, Map<String, Runnable> options){
        super.createOptions(stage, options);
        options.put("Organizer Menu", () -> subMenuUI(stage));
    }

    // Note to anyone trying to use this, requires a stage to be created, and for OrganizerGUI.accessGUI(stage)
    // to be called
    public void subMenuUI(Stage stage) {
        Map<String, Runnable> options = new LinkedHashMap<>();
        options.put("Create an Event", () -> createEventUI(stage));
        options.put("Remove an Event", () -> removeEventUI(stage));
        options.put("Add a Speaker to an Event", () -> addSpeakerUI(stage));
        options.put("Remove a Speaker from an Event", () -> removeSpeakerUI(stage));
        options.put("Create a Room", () -> createRoomUI(stage));
        options.put("Set a Room for an Event", () -> setEventRoomUI(stage));
        options.put("View All Rooms", () -> viewRoomsUI(stage));

        List<Button> buttons = new ArrayList<>();
        for (String option : options.keySet()) {
            Button button = new Button(option);
            button.setMinSize(200, 30);
            button.setMaxSize(200, 30);
            button.setOnAction(e -> options.get(option).run());
            buttons.add(button);
        }

        Button exit = new Button("Back");
        exit.setOnAction(e -> this.accessUI(stage));

        Button signUpButton = new Button("Create a New Account");
        signUpButton.setMaxSize(200, 30);
        signUpButton.setMinSize(200, 30);

        buttons.add(signUpButton);
        buttons.add(exit);

        Scene orgMenu = createButtonScene(new Text("Organizer Menu"), buttons);

        Scene signUp = AP.getLoginGUIForSignUP().accessSignUpGUI(stage, signUpButton, orgMenu);
        signUpButton.setOnAction((event) -> stage.setScene(signUp));

        stage.setScene(orgMenu);
    }

    /**
     * A GUI for creating an Event
     *
     * @param stage the Stage object for the user interface
     */
    public void createEventUI(Stage stage) {
        Text title = new Text("Create Event");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 16));
        List<Text> prompts = new ArrayList<>(Arrays.asList(new Text("Event Name"),
                new Text("Time"), new Text("Event Duration"), new Text("Event Capacity")));
        List<TextField> textFields = new ArrayList<>();

        for (Text prompt : prompts){
            textFields.add(createPromptTextField(prompt.getText()));
        }
        stage.setScene(OSM.createEventScene(stage, title, prompts, textFields));
    }

    /**
     * A GUI for removing an Event
     *
     * @param stage the Stage object for the user interface
     */
    public void removeEventUI(Stage stage) {
        List<Text> prompts = new ArrayList<>(Collections.singletonList(
                new Text("Please enter the Event ID of the event you want to remove")));
        List<TextField> textFields = new ArrayList<>();

        for (Text prompt : prompts){
            textFields.add(createPromptTextField(prompt.getText()));
        }
        stage.setScene(createPromptScene(OSM.createPromptGrid(stage,
                new Text("Remove Event"), prompts, textFields)));

    }

    /**
     * A GUI for adding a speaker to an Event
     *
     * @param stage the Stage object for the user interface
     */
    public void addSpeakerUI(Stage stage) {
        List<Text> prompts = new ArrayList<>(Arrays.asList(new Text("Event ID"),
                new Text("Speaker's Username")));
        List<TextField> textFields = new ArrayList<>();

        for (Text prompt : prompts){
            textFields.add(createPromptTextField(prompt.getText()));
        }
        stage.setScene(createPromptScene(OSM.createPromptGrid(stage,
                new Text("Add Speaker to Event"), prompts, textFields)));
    }

    /**
     * A GUI for removing a speaker from an Event
     *
     * @param stage the Stage object for the user interface
     */
    public void removeSpeakerUI(Stage stage) {
        List<Text> prompts = new ArrayList<>(Arrays.asList(new Text("Event ID"),
                new Text("Speaker's Username")));
        List<TextField> textFields = new ArrayList<>();

        for (Text prompt : prompts){
            textFields.add(createPromptTextField(prompt.getText()));
        }
        stage.setScene(createPromptScene(OSM.createPromptGrid(stage,
                new Text("Remove Speaker from Event"), prompts, textFields)));
    }

    /**
     * A GUI for creating a room for an Event
     *
     * @param stage the Stage object for the user interface
     */
    public void createRoomUI(Stage stage) {
        Text title = new Text("Create a Room");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 16));
        Text prompt = new Text("Room Capacity");
        TextField textField = createPromptTextField(prompt.getText());
        List<Text> headers = createHeaders(
                new ArrayList<>(Arrays.asList("Restrictions", "Speaker Restrictions", "User Restrictions",
                        "Available Technologies ", "Available Furniture")));
        List<CheckBox> techReqs = createRequirements(new ArrayList<>(Arrays.asList("Projector", "Drawing Tablet")));
        List<RadioButton> furniture = createRestrictions(
                new ArrayList<>(Arrays.asList("None", "Tables and Chairs", "Rows of Chairs")));

        stage.setScene(OSM.createRoomScene(stage, title, headers, prompt, textField, techReqs, furniture));
    }

    /**
     * A GUI for setting a room for an Event
     *
     * @param stage the Stage object for the user interface
     */
    public void setEventRoomUI(Stage stage) {
        List<Text> prompts = new ArrayList<>(Arrays.asList(new Text("Event ID"),
                new Text("Room ID")));
        List<TextField> textFields = new ArrayList<>();
        for (Text prompt : prompts){
            textFields.add(createPromptTextField(prompt.getText()));
        }


        stage.setScene(OSM.createSetRoomScene(stage, new Text("Set Room for an Event"), prompts, textFields));
    }

    /**
     * A GUI for viewing the rooms of the event.
     *
     * @param stage the Stage object for the user interface
     */
    public void viewRoomsUI(Stage stage) {
        // #TODO
        Text title = new Text("View Rooms");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 16));
        Scene scene = OSM.createRoomViewScene(stage, new Text("View Rooms"), OP.getRoomsForGUI());
        stage.setScene(scene);
    }

    private TextField createPromptTextField(String prompt){
        TextField promptField = new TextField();
        switch (prompt) {
            case "Event Name":{
                promptField.setPromptText("Enter an event name");
            }
            case "Event ID": {
                promptField.setPromptText("Enter an event ID (an integer)");
                break;
            }
            case "Time":{
                promptField.setPromptText("Enter a time (0-23)");
                break;
            }
            case "Speaker's Username": {
                promptField.setPromptText("Enter a speaker username");
                break;
            }
            case "Room ID": {
                promptField.setPromptText("Enter a room ID (an integer)");
                break;
            }
            case "Event Duration":{
                promptField.setPromptText("Enter the event duration (an integer)");
                break;
            }
            case "Capacity":{
                promptField.setPromptText("Enter the event capacity (greater than 0))");
                break;
            }
        }
        return promptField;
    }

    int addRestrictions(GridPane grid, List<Text> headers, ToggleGroup speakerGroup, ToggleGroup userGroup,
                                List<RadioButton> speakerRst, List<RadioButton> userRst){
        int i = 2;
        grid.add(headers.get(1), 1, i);
        i++;
        for (RadioButton restriction : speakerRst) {
            restriction.setToggleGroup(speakerGroup);
            grid.add(restriction, 1, i);
            i++;
        }
        speakerRst.get(0).setSelected(true);
        grid.add(headers.get(2), 1, i);
        i++;
        for (RadioButton restriction : userRst){
            restriction.setToggleGroup(userGroup);
            grid.add(restriction, 1, i);
            i++;
        }
        userRst.get(0).setSelected(true);
        return i;
    }
    int addRequirements(GridPane grid, List<Text> headers, ToggleGroup furnitureGroup,
                                List<CheckBox> techReqs, List<RadioButton> furniture, int colIndex, int rowIndex){
        int i = rowIndex;
        grid.add(headers.get(3), colIndex, i);
        for (CheckBox req : techReqs){
            i++;
            grid.add(req, colIndex, i);
        }
        i++;
        grid.add(headers.get(4), colIndex, i);
        for (RadioButton r : furniture){
            i++;
            r.setToggleGroup(furnitureGroup);
            grid.add(r, colIndex, i);
        }
        furniture.get(0).setSelected(true);
        return i + 1;
    }
    void checkRestrictions(List<RadioButton> speakerRst, List<RadioButton> userRst,
                                 List<CheckBox> techReqs, List<RadioButton> furniture, String textID){
        for (RadioButton rst : speakerRst){
            if (rst.isSelected()) {
                OP.update(String.format("Speaker Restrictions%s", textID), rst.getText());

            }
        }
        for (RadioButton rst : userRst){
            if (rst.isSelected()){
                OP.update(String.format("User Restrictions%s", textID), rst.getText());
            }
        }
        for (CheckBox req : techReqs){
            if (req.isSelected()){
                OP.update(String.format("Tech Requirements%s", textID), req.getText());
            }
        }
        for (RadioButton rst : furniture){
            if (rst.isSelected()){
                OP.update(String.format("Furniture%s", textID), rst.getText());
            }
        }
    }
    void checkRoomRequirements(List<CheckBox> techReqs, List<RadioButton> furniture, String textID){
        for (CheckBox req : techReqs){
            if (req.isSelected()){
                OP.update(String.format("Tech Requirements%s", textID), req.getText());
            }
        }
        for (RadioButton rst : furniture){
            if (rst.isSelected()){
                OP.update(String.format("Furniture%s", textID), rst.getText());
            }
        }
    }
    List<Text> createHeaders(List<String> headers){
        List<Text> newHeaders = new ArrayList<>();

        for (String header : headers){
            Text text = new Text(header);
            text.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 12));
            newHeaders.add(text);
        }
        return newHeaders;
    }
    List<RadioButton> createRestrictions(List<String> restrictions){
        List<RadioButton> selection = new ArrayList<>();
        for (String restriction : restrictions){
            selection.add(new RadioButton(restriction));
        }
        return selection;
    }
    List<CheckBox> createRequirements(List<String> requirements){
        List<CheckBox> selection = new ArrayList<>();
        for (String restriction : requirements){
            selection.add(new CheckBox(restriction));
        }
        return selection;
    }

    public Button makeExitButton(Stage stage) {
        Button exit = new Button("Back");
        exit.setOnAction(e -> this.subMenuUI(stage));
        return exit;
    }

}

