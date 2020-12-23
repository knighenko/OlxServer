package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresDB {
    //  Database credentials
    private static final Logger log = Logger.getLogger(PostgresDB.class.getName());
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/mydb";
    static final String USER = "myuser";
    static final String PASS = "123";

    public static void connection() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = null;
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            log.log(Level.INFO, "Connected to Database: " + DB_URL);
            System.out.println("Connected to Database: " + DB_URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
