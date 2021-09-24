package gateway;

import controller.ConferenceController;
import entity.*;
import usecase.*;

import java.io.*;
import java.util.List;

public class LocalFileGateway {
    private MessageManager SavedMsgManager;
    private EventManager SavedEventManager;
    private RoomManager SavedRoomManager;
    ListsOfUsers SavedUserList;

    public void SaveMessageManager(MessageManager messageManager){
        SavedMsgManager = messageManager;

        try {
            FileOutputStream fileOut =
                    new FileOutputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/MessageManager.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(SavedMsgManager);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in ... " + System.getProperty("user.dir") + "/phase1/SaveFiles/MessageManager.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public MessageManager LoadMessageManager(){
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/MessageManager.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SavedMsgManager = (MessageManager) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        return SavedMsgManager;
    }
    public boolean MessageManagerFileExists(){
        File messageManagerFile = new File(System.getProperty("user.dir") + "/phase1/SaveFiles/MessageManager.ser");
        return messageManagerFile.exists();
    }

    public void SaveEventManager(EventManager eventManager){
        SavedEventManager = eventManager;

        try {
            FileOutputStream fileOut =
                    new FileOutputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/EventManager.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(SavedEventManager);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in ... " + System.getProperty("user.dir") + "/phase1/SaveFiles/EventManager.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public EventManager LoadEventManager(){
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/EventManager.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SavedEventManager = (EventManager) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        return SavedEventManager;
    }
    public boolean EventManagerFileExists(){
        File eventManagerFile = new File(System.getProperty("user.dir") + "/phase1/SaveFiles/EventManager.ser");
        return eventManagerFile.exists();
    }

    public void SaveRoomManager(RoomManager roomManager){
        SavedRoomManager = roomManager;

        try {
            FileOutputStream fileOut =
                    new FileOutputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/RoomManager.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(SavedRoomManager);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in ... " + System.getProperty("user.dir") + "/phase1/SaveFiles/RoomManager.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public RoomManager LoadRoomManager(){
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/RoomManager.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SavedRoomManager = (RoomManager) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        return SavedRoomManager;
    }
    public boolean RoomManagerFileExists(){
        File roomManagerFile = new File(System.getProperty("user.dir") + "/phase1/SaveFiles/RoomManager.ser");
        return roomManagerFile.exists();
    }

    //Later I can implement this generic save to reduce duplicated code or we can ditch the .ser files for a database:
    public void SaveToFile(String FileName, Object objectToSave){

    }

    public void SaveUsers(ConferenceController conference){
        SavedUserList = new ListsOfUsers(conference.getUserManager().getUsers(),
                conference.getAttendeeManager().getAttendees(), conference.getSpeakerManager().getSpeakers());

        try {
            FileOutputStream fileOut =
                    new FileOutputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/Users.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(SavedUserList);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in ... " + System.getProperty("user.dir") + "/phase1/SaveFiles/Users.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public ListsOfUsers LoadUsers(){
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/phase1/SaveFiles/Users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SavedUserList = (ListsOfUsers) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        return SavedUserList;
    }
    public boolean UserFileExists(){
        File UserFile = new File(System.getProperty("user.dir") + "/phase1/SaveFiles/Users.ser");
        return UserFile.exists();
    }

    public void SaveAll(ConferenceController conference){
        SaveUsers(conference);
        SaveRoomManager(conference.getRoomManager());
        SaveEventManager(conference.getEventManager());
        SaveMessageManager(conference.getMessageManager());
    }
}
