package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
        boolean flag = false;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Accounts where e_mail=" + "\'" + user + "\'");
            while (rs.next()) {
                if (password.equals(rs.getString("password")))
                    flag = true;
            }
            statement.close();
            rs.close();
            closeConnection(connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return flag;
    }

    /**
     * Method create user in table USERS
     * return true when create
     */
    public static boolean createUser(String user, String password) {

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into ACCOUNTS (e_mail,password) values (?,?)");
            statement.setString(1, user);
            statement.setString(2, password);
            statement.executeUpdate();
            statement.close();
            closeConnection(connection);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        return false;
    }




    /**
     * Method create table [advertisements_+{username}] for each user who will be monitoring advertisements
     */
    public static boolean createUserAdvTable(String e_mail) {

        try {
            Connection connection = getConnection();
            Statement  statement = connection.createStatement();
            String SQL = "CREATE TABLE IF NOT EXISTS " +"advertisements_"+e_mail+
                    "(id serial PRIMARY KEY, " +
                    " Url text, " +
                    "  Flag boolean)";

            statement.executeUpdate(SQL);
            statement.close();
            closeConnection(connection);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }


    /**
     * Method add favourite search in table SEARCHLIST
     */
    public static boolean addSearch(String url, String title) {

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into SEARCHLIST (url,title) values (?,?)");
            statement.setString(1, url);
            statement.setString(2, title);
            statement.executeUpdate();
            statement.close();
            closeConnection(connection);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return false;
    }

    /**
     * Method add new advertisement in table Advertisements
     */
    public static boolean addAdvertisement(int id, int searchId, String url, String title, String phoneNumber) {

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into ADVERTISEMENTS (id, searchId, url,title, phoneNumber) values (?,?,?,?,?)");
            statement.setInt(1, id);
            statement.setInt(2, searchId);
            statement.setString(3, url);
            statement.setString(4, title);
            statement.setString(5, phoneNumber);
            statement.executeUpdate();
            statement.close();
            closeConnection(connection);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return false;
    }

    /**
     * Method get arrayList of SearchUrls from table SEARCHLIST
     */
    public static HashMap<String,String> getSearchUrls() {
        HashMap<String,String> urlMap=new HashMap<String,String>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select Id, URL from Searchlist");
            while (rs.next()) {
               urlMap.put(rs.getString("id"),rs.getString("url"));
            }
            statement.close();
            rs.close();
            closeConnection(connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return urlMap;
    }

}
