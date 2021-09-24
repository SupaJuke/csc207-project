package gateway;

import entity.AttendeeData;
import entity.Message;
import entity.SpeakerData;
import entity.UserData;
import usecase.*;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalFileGateway {
    DatabaseGateway DG;

    public Map<String, Savable> savedFiles;
    ArrayList<String> savableFileNames = new ArrayList<String>() {{
        add("UserManager");
        add("MessageManager");
        add("SpeakerManager");
        add("AttendeeManager");
        add("EventManager");
        add("RoomManager");
        add("VIPManager");
    }};

    /* GENERAL SAVABLE OBJECT IMPLEMENTATION BELOW */

    public LocalFileGateway(Connection conn){
        this.savedFiles = new HashMap<>();
        this.DG = new DatabaseGateway(conn);
    }

    /**
     * Creates a new file of the class that can be saved
     * @param savableType the savable class name
     * @return The savable class
     */
    public Savable makeNewSavable(String savableType){
        if (savableType.equals("UserManager")){
            return new UserManager();
        }
        if (savableType.equals("MessageManager")){
            return new MessageManager();
        }
        if (savableType.equals("SpeakerManager")){
            return new SpeakerManager();
        }
        if (savableType.equals("AttendeeManager")){
            return new AttendeeManager();
        }
        if (savableType.equals("EventManager")){
            return new EventManager();
        }
        if (savableType.equals("RoomManager")){
            return new RoomManager();
        }
        if (savableType.equals("VIPManager")) {
            return new VIPManager();
        }
        return null;
    }

    /**
     * Loads all the files that exist
     */
    public void loadAllFiles(){
        for (String saveFileName : savableFileNames){
            if (this.checkSavableFileExists(saveFileName)){
                Savable loadedFile = loadSavable(saveFileName);
                this.savedFiles.put(saveFileName, loadedFile);
            } else {
                this.savedFiles.put(saveFileName, makeNewSavable(saveFileName));
            }
        }
    }

    /**
     * Loads a specific file based on its file name
     * @param classFileName the name of file based on the class name
     * @return the savable file that was loaded
     */
    public Savable loadSavable(String classFileName){
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/phase2/src/SaveFiles/" + classFileName + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Savable savable = (Savable) in.readObject();
            this.savedFiles.put(classFileName, savable);
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return this.savedFiles.get(classFileName);
    }

    /**
     * Check if the savable file exist
     * @param classFileName the name of class
     * @return True iff the class exists
     */
    public boolean checkSavableFileExists(String classFileName){
        File savableFile = new File(System.getProperty("user.dir") + "/phase2/src/SaveFiles/" + classFileName + ".ser");
        return savableFile.exists();
    }

    /**
     * Saves the savable file if it exists otherwise makes a new file and saves it
     * @param savableClass The class that can be saved
     * @param classFileName The name of the file name
     */
    public void saveSavable(Savable savableClass, String classFileName){
        this.savedFiles.put(classFileName, savableClass);

        try {
            FileOutputStream fileOut =
                    new FileOutputStream(System.getProperty("user.dir") + "/phase2/src/SaveFiles/" + classFileName + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(savableClass);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in ... " + System.getProperty("user.dir") + "/phase2/src/SaveFiles/" + classFileName + ".ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Saves all files
     */
    public void saveAllFiles(){
        /* We have moved most things to the database so we only save EventManager and RoomManager in ser files now.
        for (String saveFileName : savableFileNames){
            saveSavable(this.savedFiles.get(saveFileName), saveFileName);
        }
        */
        saveSavable(this.savedFiles.get("EventManager"), "EventManager");
        saveSavable(this.savedFiles.get("RoomManager"), "RoomManager");
    }

    public void loadAllFiles(String ThisOverLoadedMethodUsesDatabase) throws SQLException {
        ListsOfUsers LoU = getAllUserInfoInDB();
        List<UserData> userDataList = LoU.getUsers();
        List<AttendeeData> attendeeDataList = LoU.getAttendees();
        List<SpeakerData> speakerDataList = LoU.getSpeakers();

        for (String saveFileName : savableFileNames){
            if (saveFileName.equals("UserManager") && !userDataList.isEmpty()){
                this.savedFiles.put("UserManager", new UserManager(userDataList));
            }
            else if(saveFileName.equals("SpeakerManager")){
                this.savedFiles.put("SpeakerManager", new SpeakerManager(speakerDataList));
            }
            else if(saveFileName.equals("AttendeeManager")){
                this.savedFiles.put("AttendeeManager", new AttendeeManager(attendeeDataList));
            }
            else if (saveFileName.equals("MessageManager")){
                this.savedFiles.put("MessageManager", new MessageManager(loadMessagesFromDB()));
            }
            else if (this.checkSavableFileExists(saveFileName)){
                Savable loadedFile = loadSavable(saveFileName);
                this.savedFiles.put(saveFileName, loadedFile);
            }
            else {
                this.savedFiles.put(saveFileName, makeNewSavable(saveFileName));
            }
        }

    }
    public ListsOfUsers getAllUserInfoInDB() throws SQLException {
        return DG.getAllUserInfoInDB();
    }
    public ListsOfUsers initUserFriendsFromDB(UserData userData) throws SQLException {
        return DG.initUserFriendsFromDB(userData);
    }
    public void insertNewUser(String user, String disName, String pass, String accType) throws SQLException {
        DG.insertNewUser( user,  disName,  pass,  accType);
    }
    public void removeFriendFromDB(String MyUsername, String TheirUsername) throws SQLException {
        DG.removeFriendFromDB(MyUsername, TheirUsername);
    }
    public void addFriendToDB(String MyUsername, String TheirUsername) throws SQLException {
        DG.addFriendToDB(MyUsername, TheirUsername);
    }
    public void changeDisplaynameInDB(String username, String newDisplayname) throws SQLException {
        DG.changeDisplaynameInDB(username, newDisplayname);
    }
    public void changePasswordInDB(String username, String newPassword) throws SQLException {
        DG.changePasswordInDB(username, newPassword);
    }
    public void addMessageToDB(int msgID, String sender, List<String> recipients, String messageBody, String toTitle) throws SQLException {
        DG.addMessageToDB(msgID, sender, recipients, messageBody, toTitle);
    }
    public List<Message> loadMessagesFromDB() throws SQLException{
        return DG.loadMessagesFromDB();
    }
    public void signUpToEventInDB(String user, int eID) throws SQLException {
        DG.signUpToEventInDB(user, eID);
    }
    public void cancelForEventInDB(String MyUsername, int eID) throws SQLException {
        DG.cancelForEventInDB(MyUsername, eID);
    }
    public void addSpeakingAt(int eID, String user) throws SQLException {
        DG.addSpeakingAt(eID, user);
    }
    public void cancelSpeakingAtInDB(int eID, String user) throws SQLException {
        DG.cancelSpeakingAtInDB(eID, user);
    }
}
