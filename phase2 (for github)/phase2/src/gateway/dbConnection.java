package gateway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {

    private static final String dbFileDir = System.getProperty("user.dir") + "/phase2/src/databaseFiles/ConferenceSystem.db";
    private static final String SQCONN = "jdbc:sqlite:" + dbFileDir;

    public static Connection getConnection() throws SQLException{
        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(SQCONN);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
