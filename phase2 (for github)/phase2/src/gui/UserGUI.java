package gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presenter.AccountPresenter;
import presenter.UserPresenter;

import java.sql.SQLException;

public class UserGUI implements Viewable {

    private final UserPresenter UP;
    private final AccountPresenter AP;
    private Stage window;
    private Scene scene;

    /**
     * Constructs a UserGUI, an interface for profile managing
     * @param UP UserPresenter that handles the logic behind this GUI
     * @param AP AccountPresenter of the User
     */
    public UserGUI(UserPresenter UP, AccountPresenter AP) {
        this.UP = UP;
        this.AP = AP;
    }

    /**
     * Runs the GUI
     * @param primaryStage the Stage this GUI is based on
     */
    @Override
    public void accessUI(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Conference");
        optionsUI();
    }

    /**
     * Helper method to create the UI for options available
     */
    private void optionsUI() {
        // Button 1: Change Display Name
        Button button1 = createButton("Change Display Name", 200);
        new Button("Change Display Name");
        button1.setOnAction(e -> nameUI());

        // Button 2: Change Password
        Button button2 = createButton("Change Password", 200);
        button2.setOnAction(e -> passwordUI());

        // Button 3: Quit
        Button button3 = createButton("Exit", 200);
        button3.setOnAction(e -> closeGUI());

        // Main Layout
        VBox main = new VBox(20);
        Text title = new Text("Profile Management System");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        main.getChildren().addAll(title, button1, button2, button3);
        main.setAlignment(Pos.CENTER);
        scene = new Scene(main, 600, 400);
        window.setScene(scene);

        window.show();
    }

    /**
     * Helper method to create the change display name UI
     */
    private void nameUI() {
        GridPane grid1 = new GridPane();
        grid1.setPadding(new Insets(10, 10, 10, 10));
        grid1.setHgap(10);
        grid1.setVgap(8);

        Text title1 = new Text("Display Name Changing");
        title1.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        GridPane.setConstraints(title1, 0, 0, 2, 1,
                HPos.CENTER, VPos.CENTER);

        Label nameCurrent = new Label("Current Name: " + UP.getDisplayName());
        GridPane.setConstraints(nameCurrent, 0, 1, 2, 1,
                HPos.CENTER, VPos.CENTER);

        Label nameLabel = new Label("New Display Name: ");
        GridPane.setConstraints(nameLabel, 0, 2);

        TextField nameInput = new TextField();
        nameInput.setPromptText("display name");
        GridPane.setConstraints(nameInput, 1, 2);

        Label nameSystem = new Label("Please type in the new display name");
        GridPane.setConstraints(nameSystem, 0, 5, 2, 1,
                HPos.CENTER, VPos.CENTER);

        Button nameConfirm = createButton("Confirm", 100);
        nameConfirm.setOnAction(e -> {
            try {
                nameSystem.setText(UP.changeDisplayName(nameInput.getText()));
                nameCurrent.setText("Current Name: " + UP.getDisplayName());
            }
            catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });
        GridPane.setConstraints(nameConfirm, 0, 3);

        Button nameCancel = createButton("Cancel", 100);
        nameCancel.setOnAction(e -> window.setScene(scene));
        GridPane.setConstraints(nameCancel, 1, 3, 1, 1,
                HPos.RIGHT, VPos.CENTER);

        grid1.getChildren().addAll(title1, nameCurrent, nameLabel, nameInput,
                nameConfirm, nameCancel, nameSystem);
        grid1.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(grid1, 600, 400);

        window.setScene(scene1);
    }

    /**
     * Helper method to create the change password UI
     */
    private void passwordUI() {
        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(10, 10, 10, 10));
        grid2.setHgap(10);
        grid2.setVgap(8);

        Text title2 = new Text("Password Changing");
        title2.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        GridPane.setConstraints(title2, 0, 0, 2, 1,
                HPos.CENTER, VPos.CENTER);

        Label passwordOldLabel = new Label("Old Password: ");
        GridPane.setConstraints(passwordOldLabel, 0, 1);

        PasswordField passwordOldInput = new PasswordField();
        passwordOldInput.setPromptText("old password");
        GridPane.setConstraints(passwordOldInput, 1, 1);

        Label passwordNewLabel = new Label("New Password: ");
        GridPane.setConstraints(passwordNewLabel, 0, 2);

        PasswordField passwordNewInput = new PasswordField();
        passwordNewInput.setPromptText("new password");
        GridPane.setConstraints(passwordNewInput, 1, 2);

        Label passwordConfirmLabel = new Label("Confirm New Password");
        GridPane.setConstraints(passwordConfirmLabel, 0, 3);

        PasswordField passwordConfirmInput = new PasswordField();
        passwordConfirmInput.setPromptText("confirm password");
        GridPane.setConstraints(passwordConfirmInput, 1, 3);

        Label passwordSystem = new Label("Please type in the new password");
        GridPane.setConstraints(passwordSystem, 0, 5, 2, 1,
                HPos.CENTER, VPos.CENTER);

        Button passwordConfirm = createButton("Confirm", 100);
        passwordConfirm.setOnAction(e -> {
            String oldPW = passwordOldInput.getText();
            String newPW = passwordNewInput.getText();
            String confirmPW = passwordConfirmInput.getText();
            try {
                passwordSystem.setText(UP.changePassword(oldPW, newPW, confirmPW));
            }
            catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });

        GridPane.setConstraints(passwordConfirm, 0, 4);

        Button passwordCancel = createButton("Cancel", 100);
        passwordCancel.setOnAction(e -> window.setScene(scene));
        GridPane.setConstraints(passwordCancel, 1, 4, 1, 1,
                HPos.RIGHT, VPos.CENTER);

        grid2.getChildren().addAll(title2, passwordOldLabel, passwordOldInput, passwordNewLabel, passwordNewInput,
                passwordConfirmLabel, passwordConfirmInput, passwordConfirm, passwordCancel, passwordSystem);
        grid2.setAlignment(Pos.CENTER);
        Scene scene2 = new Scene(grid2, 600, 400);

        window.setScene(scene2);
    }

    /**
     * The method used in the Return (to AccountGUI) button
     */
    private void closeGUI() {
        // Return to the accountGUI
        AP.getAccountGUI().accessUI(window);
    }

    private Button createButton(String text, int width) {
        Button button = new Button(text);
        button.setMinSize(width, 30);
        button.setMaxSize(width, 30);
        return button;
    }
}