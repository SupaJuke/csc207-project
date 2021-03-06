package gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenter.LoginPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginGUI implements Viewable, ISignUP {

    private LoginPresenter loginPresenter;
    private Scene scene, scene1, scene2;
    final int defaultWidth = 600;
    final int defaultHeight = 400;
    final int spacing = 15;

    /**
     * A setter for the GUI's presenter
     * @param presenter the LoginPresenter
     */
    public void setLoginPresenter(LoginPresenter presenter) {
        loginPresenter = presenter;
    }

    /**
     * The graphical user interface that allows users to log in and sign up and quit the program
     * @param primaryStage the Stage that is required to run the GUI
     */
    public void accessUI(Stage primaryStage){
        // Opening Sound
        String musicFile = "phase2/sound1.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.25);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
        primaryStage.setTitle("Conference");

        // Buttons
        Button loginButton = new Button("Login");
        Button signUpButton = new Button("Sign Up");
        Button cancelButton = new Button("Cancel");
        Button exitButton = createExitButton(primaryStage);

        // Button list
        List<Button> buttons = new ArrayList<>(Arrays.asList(loginButton, signUpButton, exitButton));
        for (Button button : buttons) {
            button.setMaxSize(200, 30);
            button.setMinSize(200, 30);
        }

        // Login
        accessLoginGUI(primaryStage, loginButton, cancelButton);

        // Sign Up
        accessSignUpGUI(primaryStage, signUpButton, scene);

        // Main Layout
        VBox layout = createLayout(buttons);
        layout.getChildren().add(mediaView);
        scene = new Scene(layout, defaultWidth, defaultHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void accessLoginGUI(Stage primaryStage, Button loginButton, Button returnButton){
        loginButton.setOnAction(e -> primaryStage.setScene(scene1));

        // Layout 1
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 0, 0, 0));
        grid.setHgap(spacing);
        grid.setVgap(spacing);

        // Labels
        List<Label> labelList = createLabelList(Arrays.asList("username", "password"), 1, "Enter your ");
        Label loginSystem = new Label("Please type in your username and password");
        GridPane.setConstraints(loginSystem, 0, 5, 4, 1, HPos.CENTER, VPos.CENTER);

        // TextFields
        TextField nameInput = createTextField("username", 1, 1);

        // PasswordFields
        PasswordField passInput = createPasswordField();

        // Layout 2
        Text title = new Text("Login");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        GridPane.setConstraints(title, 1, 0);
        grid.getChildren().add(title);
        grid.getChildren().addAll(labelList);
        grid.getChildren().addAll(returnButton, loginSystem, nameInput, passInput,
                loginHelper(nameInput, passInput, loginSystem, primaryStage, returnButton));
        grid.setAlignment(Pos.CENTER);
        scene1 = new Scene(grid, defaultWidth, defaultHeight);
    }

    /**
     * Creates a scene where the user can create an account on the program
     * @param primaryStage The Stage the GUI uses
     * @param button The button that will send the Stage to the scene when activated
     * @param goBackScene The return scene
     * @return returns the sign up scene
     */
    public Scene accessSignUpGUI(Stage primaryStage, Button button, Scene goBackScene){
        button.setOnAction(e -> primaryStage.setScene(scene2));

        // Layout 1
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 0, 0, 0));
        grid.setHgap(spacing);
        grid.setVgap(spacing);

        // Labels
        List<Label> labelList = createLabelList(
                Arrays.asList("username", "password", "display name"), 2, "Enter your new ");
        Label confirmLabel = new Label();

        // TextFields
        TextField nameInput = createTextField("username", 0,2);
        TextField passInput = createTextField("password", 0,4);
        TextField displayInput = createTextField("display name", 0,6);

        // Checkboxes
        List<CheckBox> checkBoxes = createCheckBoxes(Arrays.asList("Attendee", "Organizer", "Speaker", "VIP"));
        checkBoxCheck(checkBoxes);

        // Buttons
        Button confirmButton = signUpConfirm(nameInput, passInput, displayInput, confirmLabel, checkBoxes);
        grid.getChildren().add(confirmLabel);
        Button returnButton = signUpButtons(confirmLabel, confirmButton, Arrays.asList(nameInput, passInput,
                displayInput), checkBoxes, primaryStage, goBackScene);

        // Layout 2
        signUpLayout(grid, labelList, Arrays.asList(confirmButton, returnButton), Arrays.asList(nameInput, passInput,
                        displayInput), checkBoxes);
        scene2 = new Scene(grid, defaultWidth, defaultHeight);
        return scene2;
    }

    private Button loginConfirm(TextField nameInput, PasswordField passInput, Label nameSystem, Stage primaryStage) {
        Button nameConfirm = new Button("Confirm");
        nameConfirm.setOnAction(e -> {
            if (loginPresenter.login(nameInput.getText(), passInput.getText())){
                nameSystem.setText("Log in Successful!");
                loginPresenter.createAccountPresenter(
                        nameInput.getText(), primaryStage, this).getAccountGUI().accessUI(primaryStage);
            }
            else {nameSystem.setText("Incorrect Username or Password. Please Try again.");}});
        GridPane.setConstraints(nameConfirm, 1, 3);
        return nameConfirm;
    }

    private Button signUpButtons(Label confirmLabel, Button confirmButton, List<TextField> textFields,
                               List<CheckBox> checkBoxes, Stage primaryStage, Scene goBackScene) {
        GridPane.setConstraints(confirmButton, 0, 8, 1, 1, HPos.LEFT, VPos.CENTER);
        Button returnButton = new Button("Cancel");
        returnButton.setOnAction(e -> {
            for (TextField textField : textFields) {
                textField.clear();}
            confirmLabel.setText(""); for (CheckBox checkBox : checkBoxes) {checkBox.setSelected(false);}
            primaryStage.setScene(goBackScene);});
        GridPane.setConstraints(returnButton, 0, 8, 1, 1, HPos.RIGHT, VPos.CENTER);
        return returnButton;
    }

    private Button loginHelper(TextField nameInput, PasswordField passInput, Label loginSystem, Stage primaryStage,
                               Button returnButton) {
        Button nameConfirm = loginConfirm(nameInput, passInput, loginSystem, primaryStage);
        returnButton.setOnAction(e -> {nameInput.clear(); passInput.clear();
            loginSystem.setText("Please type in your username and password"); primaryStage.setScene(scene);});
        GridPane.setConstraints(returnButton, 1, 3, 1, 1, HPos.RIGHT, VPos.CENTER);
        return nameConfirm;
    }

    private Button signUpConfirm(TextField nameInput, TextField passInput, TextField displayInput, Label confirmLabel,
                                 List<CheckBox> checkBoxes) {
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            String type = "";
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    type = checkBox.getText();
                }
            }
            if (loginPresenter.signUp(nameInput.getText(), passInput.getText(), displayInput.getText(), type)){
                confirmLabel.setText("Signed up successfully!");
            }
            else {confirmLabel.setText(
                    "Either the username already exists or there in an invalid input. Please try again.");}
            GridPane.setConstraints(confirmLabel, 0, 9);});
        return confirmButton;
    }

    private void signUpLayout(GridPane grid, List<Label> labelList, List<Button> buttonList, List<TextField> textFields,
                              List<CheckBox> checkBoxes) {
        Text title = new Text("Sign Up");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        grid.getChildren().add(title);
        grid.getChildren().addAll(labelList);
        grid.getChildren().addAll(buttonList);
        grid.getChildren().addAll(textFields);
        grid.getChildren().addAll(checkBoxes);
        grid.setAlignment(Pos.CENTER);
    }

    private Label createLabel(String input, int rowIndex) {
        Label label = new Label(input);
        GridPane.setConstraints(label, 0, rowIndex);
        return label;
    }

    private List<Label> createLabelList(List<String> asList, int increment, String offset) {
        List<Label> labelList = new ArrayList<>();
        int i = 1;
        int j = 0;
        while (j < asList.size()) {
            labelList.add(createLabel(offset + asList.get(j), i));
            i = i + increment;
            j++;
        }
        return labelList;
    }

    private TextField createTextField(String input, int columnIndex, int rowIndex) {
        TextField textField = new TextField();
        textField.setPromptText(input);
        GridPane.setConstraints(textField, columnIndex, rowIndex);
        return textField;
    }

    private VBox createLayout(List<Button> buttons) {
        VBox layout = new VBox(spacing);
        Text title = new Text("Conference");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(0, 0, 0, 0));
        layout.getChildren().add(title);
        layout.getChildren().addAll(buttons);
        return layout;
    }

    private PasswordField createPasswordField() {
        PasswordField passInput = new PasswordField();
        passInput.setPromptText("password");
        GridPane.setConstraints(passInput, 1,2);
        return passInput;
    }

    private List<CheckBox> createCheckBoxes(List<String> inputs) {
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            CheckBox checkBox = new CheckBox(inputs.get(i));
            GridPane.setConstraints(checkBox, 5, i+1);
            checkBoxes.add(checkBox);
        }
        return checkBoxes;
    }

    private void checkBoxCheck(List<CheckBox> checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setAllowIndeterminate(false);
            checkBox.setOnAction(e -> {
                for (CheckBox otherCheckBox : checkBoxes) {
                    if (!checkBox.equals(otherCheckBox)) {
                        otherCheckBox.setSelected(false);
                    }
                }
            });
        }
    }

    private Button createExitButton(Stage primaryStage){
        Button button3 = new Button("Quit");
        button3.setOnAction(e -> shutdownSequence(primaryStage));
        return button3;
    }

    private void shutdownSequence(Stage primaryStage) {
        String musicFile = "phase2/sound2.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.25);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(primaryStage::close);
        MediaView mediaView = new MediaView(mediaPlayer);
        System.out.println("Shutting down ...");

        Text endCredits = new Text("Brought to you by, Group_0406");
        endCredits.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 36));

        StackPane layout = new StackPane();
        layout.getChildren().add(mediaView);
        layout.getChildren().add(endCredits);
        StackPane.setAlignment(endCredits, Pos.CENTER);
        scene = new Scene(layout, defaultWidth, defaultHeight);
        primaryStage.setScene(scene);
        loginPresenter.shutdownSequence();
    }
}
