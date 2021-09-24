package gui;

import entity.Taggable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import presenter.messages.AbstractMessagePresenter;
import presenter.messages.Group;
import usecase.NewMessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class MessageGUI implements Viewable, NewMessageListener {

    //graphics stuff

    private final int leftTabWidth = 200;

    private final double scrollSpeed = 0.01;

    private int chatWidth = 800;
    private int chatHeight = 400;

    private Insets defaultInsets = new Insets(2,2,2,2);

    //


    private final AbstractMessagePresenter MP;

    private List<String> sendTargets;
    private String sendGroupName;

    private Button sendGroupButton;

    private int numMessages = 0;
    private GridPane messagesGP;


    /**
     * Create a MessageGUI for given MC
     * @param MP the MessagePresenter
     */
    public MessageGUI(AbstractMessagePresenter MP){
        this.MP = MP;
    }

    private void changeTarget(List<String> sendTargets, String sendGroupName){
        this.sendTargets = sendTargets;
        this.sendGroupName = sendGroupName;
        this.sendGroupButton.setText(sendGroupName);
    }

    /**
     * turns a Taggable message into a GridPane displaying its info
     * @param msgData the message Taggable
     * @return the finished GridPane
     */
    private GridPane getMessageGridPane(Taggable msgData) {

        SortedMap<String, String> tags = msgData.getTags();

        GridPane msgGP = new GridPane();
        msgGP.setPrefWidth(100000);

        String messageID = tags.get("MID");
        String fromUsername = tags.get("From");

        Button idButton = new Button( String.format("MID: %s", messageID) );
        msgGP.add(idButton, 0, 0);
        idButton.setOnMouseClicked(arg0 -> {
            //set contactTarget
            ArrayList<String> targetUsernames = new ArrayList<>();
            targetUsernames.add(fromUsername);
            changeTarget(targetUsernames, String.format("MID %s", messageID) );
        });

        Label from = new Label( String.format("From: %s", fromUsername) );
        msgGP.add(from, 1, 0);

        Label to = new Label( String.format("To: %s", tags.get("To")) );
        msgGP.add(to, 2, 0);

        Label msg = new Label(tags.get("Body"));
        msgGP.add(msg, 0, 1, 3, 1);

        msgGP.setHgap(10);
        msgGP.setVgap(2);
        msgGP.setPadding(defaultInsets);

        msgGP.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return msgGP;
    }

    /**
     * make the pane for scrolling through messages
     * @return ScrollPane for chat
     */
    private ScrollPane getChatScrollPane() {

        ScrollPane messagesSP = new ScrollPane();

        messagesGP = new GridPane();
        messagesGP.setPrefWidth(100000);
        messagesGP.setPrefHeight(100000);

        messagesSP.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        messagesSP.setContent(messagesGP);
        messagesSP.setFitToHeight(true);
        messagesSP.setFitToWidth(true);

        ArrayList<Taggable> messages = new ArrayList<>(MP.getMyMessages());

        for (numMessages = 0; numMessages < messages.size(); numMessages++) {
            GridPane msgGP = getMessageGridPane(messages.get(numMessages));
            msgGP.setPrefWidth(messagesGP.getPrefWidth());

            messagesGP.add(msgGP, 0, numMessages);
        }
        GridPane.setValignment(messagesGP, VPos.BOTTOM);
        messagesGP.setPadding(defaultInsets);

        return messagesSP;
    }


    /**
     * make a GridPane for sending messages
     * @return GridPane for sending messages
     */
    private GridPane getSender() {

        GridPane sendGP = new GridPane();
        sendGP.setPrefHeight(200);
        sendGP.setPrefWidth(100000);

        sendGP.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Label toLabel = new Label( "To: ");
        sendGP.add(toLabel, 0, 0);
        sendGroupButton = new Button();
        sendGP.add(sendGroupButton, 1, 0);
        sendGroupButton.setOnMouseClicked(arg0 -> {
            //set contactTarget
            changeTarget(null, null);
        });

        TextField inputTF = new TextField();
        int sendTabHeight = 200;
        inputTF.setPrefHeight(sendTabHeight);
        inputTF.setPrefWidth(100000);
        inputTF.setPrefColumnCount(2);
        sendGP.add(inputTF, 0,1, 3, 1);

        sendGP.setHgap(2);
        sendGP.setVgap(2);

        Button sendButton = new Button( "Send" );
        sendGP.add(sendButton, 4, 1);
        sendButton.setStyle("-fx-background-color: #84e38c; ");

        sendButton.setOnMouseClicked(arg0 -> {
            //set contactTarget
            if (sendTargets != null && sendTargets.size() != 0){
                MP.deliverMessage(sendTargets, inputTF.getText(), sendGroupName);
                inputTF.clear();
                sendGroupButton.setText("");
                sendTargets.clear();
                sendGroupName = "";
            }
        });

        sendGP.setPadding(defaultInsets);

        return sendGP;
    }


    /**
     * make a contacts GridPane
     * @return contacts GridPane
     */
    private GridPane getContactsGridPane() {

        GridPane contactsGP = new GridPane();
        int scrollbarWidth = 15;
        contactsGP.setPrefWidth(leftTabWidth - scrollbarWidth);
        contactsGP.setPrefHeight(100000);

        return contactsGP;
    }

    private void fillContactsGridPane(GridPane contactsGP, List<Group> contacts){
        contactsGP.getChildren().clear();
        for (int i = 0; i < contacts.size(); i++) {
            Group mg = contacts.get(i);
            Button newButton = new Button(mg.getGroupName());
            newButton.setPrefWidth(contactsGP.getPrefWidth());

            newButton.setOnMouseClicked(arg0 -> {
                //set contactTarget
                changeTarget(new ArrayList<>(mg.getGroupTargets()), mg.getGroupName() );
            });

            contactsGP.add(newButton, 0, i);
        }
    }


    /**
     * create the GUI and put it in the stage
     * @param primaryStage the stage the GUI will be placed on
     */
    private void generateGUI(Stage primaryStage) {
        sendTargets = new ArrayList<>();
        sendGroupName = "";

        primaryStage.setTitle("Conference");

        VBox mainVB = new VBox();

        BorderPane toolBarBP = new BorderPane();

        mainVB.getChildren().add(toolBarBP);

        Label welcomeLabel = new Label(String.format( "Message GUI for %s %s", MP.getUsertype(), MP.getUsername() )  );
        toolBarBP.setLeft(welcomeLabel);
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);

        Insets TBBPInsets = new Insets(0,5,0,5);
        BorderPane.setMargin(welcomeLabel, TBBPInsets);


        Scene oldScene = primaryStage.getScene();
        Button exitButton = new Button("Back");
        exitButton.setStyle("-fx-background-color: #e05e5e; ");
        exitButton.setOnAction(e -> {
            primaryStage.setScene(oldScene);
            MP.getMessageNotifier().removeListener(this);
        });
        toolBarBP.setRight(exitButton);

        HBox chatHB = new HBox();
        mainVB.getChildren().add(chatHB);

        GridPane leftGP = new GridPane();
        leftGP.setPrefWidth(leftTabWidth);

        Map<String, List<Group>> targetGroups = MP.getValidTargetGroups();
        int tabs = targetGroups.size();

        GridPane contactsList = getContactsGridPane();

        String defaultGroupName = "";
        int iter = 0;
        for (String groupName: targetGroups.keySet()) {
            if(iter == 0){
                defaultGroupName = groupName;
            }
            Button newButton = new Button(groupName);
            newButton.setOnMouseClicked(arg0 -> fillContactsGridPane(contactsList, targetGroups.get(groupName)) );
            newButton.setPrefWidth(leftGP.getPrefWidth() / tabs);
            newButton.setMaxWidth(leftGP.getPrefWidth() / tabs);
            leftGP.add(newButton, iter, 0);
            iter++;
        }

        ScrollPane contactsScroller = new ScrollPane();
        contactsScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        contactsScroller.setContent(contactsList);
        contactsScroller.setFitToHeight(true);

        fillContactsGridPane(contactsList, targetGroups.get(defaultGroupName));

        leftGP.add(contactsScroller, 0, 1, tabs, 1);
        leftGP.setMinWidth(leftTabWidth);
        chatHB.getChildren().add(leftGP);

        //---------------------------------

        VBox rightV = new VBox();

        ScrollPane chatScrollPane = getChatScrollPane();

        chatScrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * scrollSpeed;
            chatScrollPane.setVvalue(chatScrollPane.getVvalue() - deltaY);
        });

        rightV.setAlignment(Pos.BOTTOM_LEFT);

        rightV.getChildren().add(chatScrollPane);
        rightV.getChildren().add(getSender());

        chatHB.getChildren().add(rightV);

        MP.getMessageNotifier().addListener(this);

        Scene scene = new Scene(mainVB, chatWidth, chatHeight);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens this GUI on a given Stage
     * @param primaryStage the Stage to display this GUI on
     */
    @Override
    public void accessUI(Stage primaryStage) {
        generateGUI(primaryStage);
    }

    /**
     * Notify this obj that a new message has arrived
     * It will display it
     * @param messageTags the Taggable representing the new message
     */
    @Override
    public void notifyNewMessage(Taggable messageTags) {
        GridPane msgGP = getMessageGridPane(messageTags);
        msgGP.setPrefWidth(messagesGP.getPrefWidth());

        numMessages++;
        messagesGP.add(msgGP, 0, numMessages);
    }
}
