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
import presenter.FriendPresenter;
import presenter.AccountPresenter;
import javafx.scene.text.Text;

import java.sql.SQLException;

/**
 * The UI containing Friend options for the user to interact with
 */
public class FriendGUI implements Viewable{

    private FriendPresenter FP;
    private AccountPresenter AP;
    private Stage window;

    //formatting
    private final int spacing = 10;
    private final int width = 600;
    private final int height = 400;

    /**
     * A setter for LoginGUI that adds a friend presenter and account presenter
     * @param FP The FriendPresenter
     * @param AP The AccountPresenter
     */
    public void setPresenters(FriendPresenter FP, AccountPresenter AP){
        this.FP = FP;
        this.AP = AP;
    }

    /**
     * A setter for the LoginGUI that adds a friend presenter
     * @param FP the FriendPresenter
     */
    public void setPresenters(FriendPresenter FP){
        this.FP = FP;
    }

    /**
     * Method is called from AccountGUI
     * @param primaryStage the Stage that the GUI is using
     */
    @Override
    public void accessUI(Stage primaryStage) {
        primaryStage.setTitle("Conference");
        window = primaryStage;
        createOptionsUI();
    }

    /**
     * Changes stage to UI showing Friend options
     */
    private void createOptionsUI(){
        VBox layout = new VBox(spacing);
        Button[] buttons = new Button[4];

        for(int i=0; i<4; i++)
            buttons[i] = new Button();
        buttons[0].setText("Add a Friend");
        buttons[1].setText("Remove a Friend");
        buttons[2].setText("View Friend List");
        buttons[3].setText("Exit");

        //uses lambda function for JavaFX event handling
        buttons[0].setOnAction((event) -> createFriendUI(true));
        buttons[1].setOnAction((event) -> createFriendUI(false));
        buttons[2].setOnAction((event) -> createFriendsListUI());
        //returns to Account options UI
        buttons[3].setOnAction((event) -> this.AP.getAccountGUI().accessUI(window));

        Text title = new Text("Friend Menu");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(title);
        for(Button button : buttons) {
            button.setMinSize(200, 30);
            button.setMaxSize(200, 30);
            layout.getChildren().add(button);
        }

        window.setScene(new Scene(layout, width, height));
    }

    /**
     * Changes stage to friend adding / removing UI
     * @param adding true if adding a friend. false if removing a friend
     */
    private void createFriendUI(boolean adding){
        VBox layout = new VBox(spacing);
        Label label = new Label();
        TextField textField = new TextField();
        textField.setMaxSize(150, 200);
        Button confirmBtn = new Button();
        Button cancelBtn = new Button();

        label.setText("Enter a friend username");

        confirmBtn.setText("Confirm");
        cancelBtn.setText("Cancel");

        confirmBtn.setOnAction((event) -> {
            try {
                if (FP.friendHelper(adding, textField.getText()))
                    if (adding)
                        label.setText("Successfully added friend.");
                    else
                        label.setText("Successfully removed friend.");
                else
                    label.setText("Friend already exists or is not a User. Please try again");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        });
        cancelBtn.setOnAction((event) -> createOptionsUI());

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(label);
        layout.getChildren().add(textField);
        layout.getChildren().add(confirmBtn);
        layout.getChildren().add(cancelBtn);

        window.setScene(new Scene(layout, width, height));
    }

    /**
     * Changes stage to Friends List UI
     */
    private void createFriendsListUI(){
        VBox layout = new VBox(spacing);
        Button cancelBtn = new Button("Return");
        cancelBtn.setOnAction((event -> createOptionsUI()));

        ListView<String> list = new ListView<>();
        list.setMaxSize(400, 200);
        for (String friend : FP.getFriendManager().getFriendsList())
            list.getItems().add(friend);

        Text title = new Text("Friends List");
        title.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(title);
        layout.getChildren().add(list);
        layout.getChildren().addAll(cancelBtn);

        window.setScene(new Scene(layout, width, height));
    }

}