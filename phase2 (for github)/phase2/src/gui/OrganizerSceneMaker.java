package gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

public class OrganizerSceneMaker {

    OrganizerGUI OG;
    final int defaultWidth;
    final int defaultHeight;
    final int spacing;

    public OrganizerSceneMaker(int width, int height, int spacing){
        this.defaultWidth = width;
        this.defaultHeight = height;
        this.spacing = spacing;
    }
    public void setGUI(OrganizerGUI organizerGUI) {
        this.OG = organizerGUI;
    }

    public Scene createEventScene(Stage stage, Text title, List<Text> prompts, List<TextField> textFields) {
        GridPane grid = OG.createGrid(new Insets(20, 0, 0, 0), Pos.TOP_CENTER);
        Map<String, String> promptInputs = new LinkedHashMap<>();
        Button next = new Button("Next");

        grid.add(title, 0, 1);
        int i = 2;
        for (Text prompt : prompts){
            int finalI = prompts.indexOf(prompt);
            Label label = new Label();
            promptInputs.put(prompt.getText(), textFields.get(finalI).getText());
            textFields.get(finalI).setOnAction(e -> {
                promptInputs.replace(prompt.getText(), textFields.get(finalI).getText());

                String text = OG.OP.update(prompt.getText(), textFields.get(finalI).getText());
                if (!text.equals("valid")){
                    label.setTextFill(Color.RED);
                    label.setText(text);
                }
                else{
                    label.setTextFill(Color.DARKGREEN);
                    label.setText("✓");
                }
            });

            grid.add(prompt, 0, i);
            grid.add(textFields.get(finalI), 0, i + 1);
            grid.add(label, 1, i + 1);
            i++;
            i++;
        }
        next.setOnAction(e -> stage.setScene(createRestrictionScene(stage, title,
                createEventScene(stage, title, prompts, textFields), promptInputs, prompts, textFields)));
        grid.add(OG.makeExitButton(stage), 0, i);
        grid.add(next, 1, i);
        return new Scene(grid, defaultWidth, defaultHeight);
    }
    public Scene createRoomScene(Stage stage, Text title, List<Text> headers, Text prompt, TextField textField,
                                 List<CheckBox> techReqs, List<RadioButton> furniture){
        GridPane grid = OG.createGrid(new Insets(20, 0, 0, 0), Pos.TOP_CENTER);
        final ToggleGroup furnitureGroup = new ToggleGroup();
        Label label = new Label();
        Label verify = new Label();
        grid.add(title, 0, 1);
        Map<String, String> promptInput = new LinkedHashMap<>();
        promptInput.put(prompt.getText(), textField.getText());
        textField.setOnAction(e -> {
            promptInput.replace(prompt.getText(), textField.getText());

            String text = OG.OP.update(prompt.getText(), textField.getText());
            if (!text.equals("valid")){
                label.setTextFill(Color.RED);
                label.setText(text);
            }
            else{
                label.setTextFill(Color.DARKGREEN);
                label.setText("✓");
            }
        });
        grid.add(prompt, 0, 2);
        grid.add(textField, 0, 3);
        int lastIndex = OG.addRequirements(grid, headers, furnitureGroup, techReqs, furniture, 0, 4);
        Button back = OG.makeExitButton(stage);
        Button confirm = new Button("Confirm");
        confirm.setMinSize(60, 25);
        confirm.setMinSize(60, 25);
        confirm.setOnAction(e -> {
            promptInput.replace(prompt.getText(), textField.getText());
            textField.fireEvent(new ActionEvent());
            String text = OG.OP.checkInputs(title.getText(),
                    new ArrayList<>(promptInput.keySet()), new ArrayList<>(promptInput.values()));
            System.out.println(text);
            if (text.startsWith("Room")){
                OG.checkRoomRequirements(techReqs, furniture, text);
                stage.setScene(OG.createSuccessScene(stage));
            } else {
                verify.setTextFill(Color.RED);
                verify.setText(text);
            }});
        grid.add(back, 0, lastIndex);
        grid.add(confirm, 1, lastIndex);
        grid.add(verify, 0, lastIndex + 1);
        return new Scene(grid, defaultWidth, defaultHeight);
    }
    public Scene createRestrictionScene(Stage stage, Text oldTitle, Scene oldScene, Map<String, String> promptInputs,
                                        List<Text> prompts, List<TextField> textFields){
        GridPane grid = OG.createGrid(new Insets(20, 20, 0, 20), Pos.TOP_LEFT);
        Label verify = new Label();
        List<Text> headers = OG.createHeaders(
                new ArrayList<>(Arrays.asList("Restrictions", "Speaker Restrictions", "User Restrictions",
                        "Technology Requirements (optional)", "Furniture")));
        List<RadioButton> speakersRst = OG.createRestrictions(
                new ArrayList<>(Arrays.asList("No Speakers", "One Speaker", "Multiple Speakers")));
        List<RadioButton> userRst = OG.createRestrictions(new ArrayList<>(
                Arrays.asList("No Restriction", "Attendee Only", "Organizer Only", "Speaker Only", "VIP Only")));
        List<CheckBox> techReqs = OG.createRequirements(new ArrayList<>(Arrays.asList("Projector", "Drawing Tablet")));
        List<RadioButton> furniture = OG.createRestrictions(new ArrayList<>(
                Arrays.asList("None", "Tables and Chairs", "Rows of Chairs")));
        headers.get(0).setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 16));
        headers.get(0).setTextAlignment(TextAlignment.CENTER);

        Button back = new Button("Back");
        back.setMinSize(50, 25);
        back.setOnAction(e -> stage.setScene(oldScene));
        Button confirm = new Button("Confirm");
        confirm.setMinSize(60, 25);
        confirm.setOnAction(e -> {
            for (TextField textField: textFields){
                promptInputs.replace(prompts.get(textFields.indexOf(textField)).getText(), textField.getText());
                textField.fireEvent(new ActionEvent());
            }
            String text = OG.OP.checkInputs(oldTitle.getText(),
                    new ArrayList<>(promptInputs.keySet()),
                    new ArrayList<>(promptInputs.values()));
            System.out.println(text);
            if (text.startsWith("Event ID")){
                OG.checkRestrictions(speakersRst, userRst, techReqs, furniture,  text);
                stage.setScene(OG.createSuccessScene(stage));
            } else {
                verify.setTextFill(Color.RED);
                verify.setText(text);
            }});
        final ToggleGroup speakerGroup = new ToggleGroup();
        final ToggleGroup userGroup = new ToggleGroup();
        final ToggleGroup furnitureGroup = new ToggleGroup();
        grid.add(headers.get(0), 3, 1);

        int lastIndex = OG.addRestrictions(grid, headers, speakerGroup, userGroup, speakersRst, userRst);
        OG.addRequirements(grid, headers, furnitureGroup, techReqs, furniture, 5, 2);
        grid.add(back, 2, lastIndex);
        grid.add(confirm, 4, lastIndex);
        grid.add(verify, 5, lastIndex);
        return new Scene(grid, defaultWidth, defaultHeight);
    }

    /**
     * Creates a Scene object to view the rooms of the conference.
     *
     * @param stage the Stage object for the user interface
     * @param title the title of the menu
     * @param roomsInfo the room's info including id, capacity and features
     * @return returns a Scene object showing the rooms of the conference.
     */
    public Scene createRoomViewScene(Stage stage, Text title, List<String> roomsInfo) {
        // #TODO
        VBox layout = new VBox(spacing);
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().add(title);

        ListView<String> list = new ListView<>();
        list.setMaxSize(400, 200);
        for (String info : roomsInfo) {
            list.getItems().add(info);
        }
        layout.getChildren().add(list);
        layout.getChildren().addAll(OG.makeExitButton(stage));

        return new Scene(layout, defaultWidth, defaultHeight);
    }

    public Scene createSetRoomScene(Stage stage, Text title, List<Text> prompts, List<TextField> textFields){
        List<Label> labels = new ArrayList<>();
        ListView<String> list = new ListView<>();
        list.setMaxSize(200, 200);
        for (int i = 0; i < prompts.size(); i++){
            labels.add(new Label());
        }
        GridPane grid = createPromptGrid(stage, title, prompts, textFields, labels);

        Button button = new Button ("Check Recommended Rooms");
        button.setOnAction(e -> {
           textFields.get(0).fireEvent(new ActionEvent());
           list.getItems().clear();
           if (labels.get(0).getText().equals("✓")){
               list.getItems().addAll(OG.OP.getRecommendedRooms(Integer.parseInt(textFields.get(0).getText())));
           }
        });

        grid.add(list, 0, 8);
        grid.add(button, 0, 9);
        return new Scene(grid, defaultWidth, defaultHeight + 100);
    }
    public GridPane createPromptGrid(Stage stage, Text title, List<Text> prompts, List<TextField> textFields){
        List<Label> labels = new ArrayList<>();
        for (int i = 0; i < prompts.size(); i++){
            labels.add(new Label());
        }
        return createPromptGrid(stage, title, prompts, textFields, labels);
    }

    public GridPane createPromptGrid(Stage stage, Text title, List<Text> prompts, List<TextField> textFields,
                                     List<Label> labels){
        GridPane grid = OG.createGrid(new Insets(20, 0, 0, 0), Pos.TOP_CENTER);

        Map<String, String> promptInputs = new LinkedHashMap<>();
        Label verify = new Label();
        Button confirm = new Button("Confirm");

        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 16));
        grid.add(title, 0, 1);

        int i = 2;
        for (Text prompt : prompts){
            int finalI = prompts.indexOf(prompt);

            promptInputs.put(prompt.getText(), textFields.get(finalI).getText());
            textFields.get(finalI).setOnAction(e -> {
                promptInputs.replace(prompt.getText(), textFields.get(finalI).getText());

                String text = OG.OP.update(prompt.getText(), textFields.get(finalI).getText());
                if (!text.equals("valid")){
                    labels.get(finalI).setTextFill(Color.RED);
                    labels.get(finalI).setText(text);
                }
                else{
                    labels.get(finalI).setTextFill(Color.DARKGREEN);
                    labels.get(finalI).setText("✓");
                }
            });

            confirm.setOnAction(e -> {
                for (TextField textField: textFields){
                    textField.fireEvent(new ActionEvent());
                }
                String text = OG.OP.checkInputs(title.getText(),
                        new ArrayList<>(promptInputs.keySet()),
                        new ArrayList<>(promptInputs.values()));
                if (text.equals("valid")){
                    stage.setScene(OG.createSuccessScene(stage));
                }
                else {
                    verify.setTextFill(Color.RED);
                    verify.setText(text);
                }
            });
            grid.add(prompt, 0, i);
            grid.add(textFields.get(finalI), 0, i + 1);
            grid.add(labels.get(finalI), 1, i + 1);
            i++;
            i++;
        }
        grid.add(confirm, 0, i);
        grid.add(verify, 1, i);
        grid.add(OG.makeExitButton(stage), 0, i+1);
        return grid;
    }

}

