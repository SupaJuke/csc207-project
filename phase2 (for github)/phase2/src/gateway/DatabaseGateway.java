package gateway;

import entity.AttendeeData;
import entity.Message;
import entity.SpeakerData;
import entity.UserData;
import usecase.MessageBuilder;
import usecase.UserFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseGateway {
    Connection connection;
    public DatabaseGateway(Connection conn) {
        this.connection = conn;
    }
    public ListsOfUsers getAllUserInfoInDB() throws SQLException {
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "select * FROM AttendeeAccounts";

        try{
            pr = this.connection.prepareStatement(sql);
            rs = pr.executeQuery();
            ListsOfUsers returnList = new ListsOfUsers();

            while (rs.next()) {
                UserData userData;
                UserFactory userFactory = new UserFactory();

                System.out.println(rs.getString("accountType"));//you can delete these 3 lines after
                System.out.println(" username: " + rs.getString("username"));
                System.out.println(" password: " + rs.getString("password"));

                switch (rs.getString("accountType")) {
                    case "Attendee":
                        userData = userFactory.createDefaultAttendee(
                                rs.getString("username"),
                                rs.getString("displayname"),
                                rs.getString("password"));
                        returnList.addAD((AttendeeData) userData);
                        break;
                    case "Organizer":
                        userData = userFactory.createDefaultOrganizer(
                                rs.getString("username"),
                                rs.getString("displayname"),
                                rs.getString("password"));
                        break;
                    case "Speaker":
                        userData = userFactory.createDefaultSpeaker(
                                rs.getString("username"),
                                rs.getString("displayname"),
                                rs.getString("password"),
                                loadSpeakingAtsFromDB(rs.getString("username"))
                        );

                        returnList.addSD((SpeakerData) userData);
                        break;
                    case "VIP":
                        userData = userFactory.createDefaultVIP(
                                rs.getString("username"),
                                rs.getString("displayname"),
                                rs.getString("password"));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + rs.getString("accountType"));

                }

                initUserFriendsFromDB(userData);
                for (int ID : loadEventIDsFromDB(rs.getString("username"))){
                    userData.addEventID(ID);
                }

                returnList.addUD(userData);
            }
            return returnList;
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
            assert rs != null;
            rs.close();
        }
        return null;
    }
    public ListsOfUsers initUserFriendsFromDB(UserData userData) throws SQLException {
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "select * FROM Friends where ListOwner = '" + userData.getUsername() + "'";

        try{
            pr = this.connection.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                userData.addFriend(rs.getString("friendusername"));
                System.out.println("*Added friend "+ rs.getString("friendusername")+ ", to " + rs.getString("ListOwner") + "'s list");
            }
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
            assert rs != null;
            rs.close();
        }
        return null;
    }

    public void insertNewUser(String user, String disName, String pass, String accType) throws SQLException {
        PreparedStatement pr = null;

        String sql = "INSERT INTO AttendeeAccounts(username, password, displayname, accountType) VALUES(?,?,?,?)";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, user);
            pr.setString(2, pass);
            pr.setString(3, disName);
            pr.setString(4, accType);

            System.out.println("*Added new signUp " + user);
            pr.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }

    public void removeFriendFromDB(String MyUsername, String TheirUsername) throws SQLException {
        PreparedStatement pr = null;

        String sql = "DELETE FROM Friends WHERE ListOwner = ? and friendusername = ?";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, MyUsername);
            pr.setString(2, TheirUsername);

            System.out.println("*Removed friend " + TheirUsername);
            pr.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }

    public void addFriendToDB(String MyUsername, String TheirUsername) throws SQLException {
        PreparedStatement pr = null;

        String sql = "INSERT INTO Friends(ListOwner, friendusername) VALUES(?,?)";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, MyUsername);
            pr.setString(2, TheirUsername);

            System.out.println("*Added new friend " + TheirUsername);
            pr.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }

    public void changeDisplaynameInDB(String username, String newDisplayname) throws SQLException {

        PreparedStatement pr = null;
        String sql = "UPDATE AttendeeAccounts set displayname = '"+newDisplayname+"' where username = '" + username + "'";

        try{
            pr = this.connection.prepareStatement(sql);
            System.out.println("*Changed display name of \"" + username + "\" to \"" + newDisplayname + "\"");
            pr.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }
    public void changePasswordInDB(String username, String newPassword) throws SQLException {

        PreparedStatement pr = null;
        String sql = "UPDATE AttendeeAccounts set password = '" + newPassword + "' where username = '" + username + "'";

        try{
            pr = this.connection.prepareStatement(sql);
            System.out.println("*Changed password of \"" + username + "\" to \"" + newPassword + "\"");
            pr.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }
    public void signUpToEventInDB(String user, int eID) throws SQLException {
        PreparedStatement pr = null;

        String sql = "INSERT INTO EventAttendance(username, EventID) VALUES(?,?)";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, user);
            pr.setString(2, String.valueOf(eID));

            System.out.println("*Signed up to event in DB");
            pr.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }
    public void cancelForEventInDB(String MyUsername, int eID) throws SQLException {
        PreparedStatement pr = null;

        String sql = "DELETE FROM EventAttendance WHERE username = ? and EventID = ?";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, MyUsername);
            pr.setString(2, String.valueOf(eID));

            System.out.println("*Canceled for event in DB ");
            pr.executeUpdate();
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }
    public List<Integer> loadEventIDsFromDB(String user) throws SQLException {
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "select * FROM EventAttendance where username = '" + user +"'";

        try{
            pr = this.connection.prepareStatement(sql);
            rs = pr.executeQuery();
            List<Integer> returnList = new ArrayList<>();

            while (rs.next()) {
                int eventID = Integer.parseInt(rs.getString("EventID"));

                returnList.add(eventID);
            }

            return returnList;
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
            assert rs != null;
            rs.close();
        }
        return null;
    }
    public void addSpeakingAt(int eID, String user) throws SQLException {
        PreparedStatement pr = null;

        String sql = "INSERT INTO EventSpeakers(EventID, speakerusername) VALUES(?,?)";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, String.valueOf(eID));
            pr.setString(2, user);

            System.out.println("*Booked to event in DB");
            pr.executeUpdate();
            //signUpToEventInDB(user, eID);
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }
    public void cancelSpeakingAtInDB(int eID, String user) throws SQLException {
        PreparedStatement pr = null;

        String sql = "DELETE FROM EventSpeakers WHERE EventID = ? and speakerusername = ?";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, String.valueOf(eID));
            pr.setString(2, user);

            System.out.println("*Canceled for event in DB ");
            pr.executeUpdate();
            //cancelForEventInDB(user, eID);
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }
    public List<Integer> loadSpeakingAtsFromDB(String user) throws SQLException {
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "select * FROM EventSpeakers where speakerusername = '" + user +"'";

        try{
            pr = this.connection.prepareStatement(sql);
            rs = pr.executeQuery();
            List<Integer> returnList = new ArrayList<>();

            while (rs.next()) {
                int eventID = Integer.parseInt(rs.getString("EventID"));
                returnList.add(eventID);
            }
            return returnList;
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
            assert rs != null;
            rs.close();
        }
        return null;
    }
    public void addMessageToDB(int msgID, String sender, List<String> recipients, String messageBody, String toTitle) throws SQLException {
        PreparedStatement pr = null;

        String sql = "INSERT INTO Messages(MessageID, messagecontent, sender, recipientTitle) VALUES(?,?,?,?)";

        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1, String.valueOf(msgID));
            pr.setString(2, messageBody);
            pr.setString(3, sender);
            pr.setString(4, toTitle);

            System.out.println("*Added new msg to DB");
            pr.executeUpdate();

            addMessageRecipientToDB(msgID, recipients);
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }
    private void addMessageRecipientToDB(int msgID, List<String> recievers) throws SQLException {
        PreparedStatement pr = null;
        String sql = "INSERT INTO MessageRecipients(MessageID, recipient) VALUES(?,?)";

        try{
            for(String recipient : recievers){
                pr = this.connection.prepareStatement(sql);
                pr.setString(1, String.valueOf(msgID));
                pr.setString(2, recipient);

                System.out.println("*Added new msg recipient to DB");
                pr.executeUpdate();
            }
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
        }
    }

    public List<Message> loadMessagesFromDB() throws SQLException{
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "select * FROM Messages";

        try{
            pr = this.connection.prepareStatement(sql);
            rs = pr.executeQuery();
            List<Message> returnList = new ArrayList<>();

            while (rs.next()) {
                Message msg;
                MessageBuilder MsgBuilder = new MessageBuilder();
                //String message, String fromUN, List<String> toUNs, int msgID, String toTitle
                msg = MsgBuilder.buildMessage(
                        rs.getString("messagecontent"),
                        rs.getString("sender"),
                        getRecipientsFromDB(rs.getString("MessageID")),
                        Integer.parseInt(rs.getString("MessageID")),
                        rs.getString("recipientTitle")
                );
                returnList.add(msg);
            }

            return returnList;
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
            assert rs != null;
            rs.close();
        }
        return null;
    }

    private List<String> getRecipientsFromDB(String msgID) throws SQLException {
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "select * FROM MessageRecipients where MessageID = '" + msgID + "'";

        try{
            pr = this.connection.prepareStatement(sql);
            rs = pr.executeQuery();
            List<String> returnList = new ArrayList<>();

            while (rs.next()) {
                returnList.add(rs.getString("recipient"));
            }
            return returnList;
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        finally{
            assert pr != null;
            pr.close();
            assert rs != null;
            rs.close();
        }
        return null;
    }
}
