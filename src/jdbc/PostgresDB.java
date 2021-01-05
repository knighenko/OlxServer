package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresDB {

    private static final Logger log = Logger.getLogger(PostgresDB.class.getName());
    //  Database credentials
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/mydb";
    static final String USER = "myuser";
    static final String PASS = "123";

    /**
     * Create DB connection
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            log.log(Level.INFO, "Connected to Database: " + DB_URL);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    /**
     * Close DB connection
     */
    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
        log.log(Level.INFO, "Closed connection to Database: " + DB_URL);
    }

    /**
     * Method checks current user and password in table USERS
     * return true when current user is in the table
     */
    public static boolean checkUser(String user, String password) {
        return false;
    }

    /**
     * Method create user in table USERS
     * return true when create
     */
    public static boolean createUser(String user, String password) {
        return false;
    }
}
