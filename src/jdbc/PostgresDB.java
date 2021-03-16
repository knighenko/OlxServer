package jdbc;

import entity.Advertisement;

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
           // log.log(Level.INFO, "Connected to Database: " + DB_URL);

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
       // log.log(Level.INFO, "Closed connection to Database: " + DB_URL);
    }

    /**
     * Method checks current e_mail and password in table USERS
     * return true when current e_mail is in the table
     */
    public static boolean checkUser(String e_mail, String password) {
        boolean flag = false;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Accounts where e_mail=" + "\'" + e_mail + "\'");
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
     * Method checks current push from table USERS
     * return true when current e_mail has flag true in the table
     */
    public static boolean checkPush(String e_mail) {
        boolean flag = false;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select push from Accounts where e_mail=" + "\'" + e_mail + "\'");
            while (rs.next()) {
                if (rs.getBoolean("push") == true)
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
     * Method insert current push from table USERS
     * return true when current e_mail has flag true in the table
     */
    public static boolean addPush(String e_mail, String flagPush) {
        boolean flag = false;
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("update ACCOUNTS set push=? where e_mail=?");
            statement.setBoolean(1, Boolean.valueOf(flagPush));
            statement.setString(2, e_mail);
            statement.executeUpdate();
            statement.close();
            closeConnection(connection);
            flag = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return flag;
    }

    /**
     * Method create e_mail in table USERS
     * return true when create
     */
    public static boolean createUser(String e_mail, String password, String deviceToken) {
        boolean flag = false;
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into ACCOUNTS (e_mail,password,devicetoken, push) values (?,?,?,?)");
            statement.setString(1, e_mail);
            statement.setString(2, password);
            statement.setString(3, deviceToken);
            statement.setBoolean(4, false);
            statement.executeUpdate();
            statement.close();
            closeConnection(connection);
            flag = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return flag;
    }


    /**
     * Method create table [advertisements_+{username}] for each user who will be monitoring advertisements
     */
    public static boolean createUserAdvTable(String e_mail) {

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String SQL = "CREATE TABLE IF NOT EXISTS " + "advertisements_" + e_mail +
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
    public static boolean addAdvertisement(int id, int searchId, String url, String imgSrc, String title, String phoneNumber, String description) {

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into ADVERTISEMENTS (olxid, searchId, url, imgSrc,title, phoneNumber, description) values (?,?,?,?,?,?,?)");
            statement.setInt(1, id);
            statement.setInt(2, searchId);
            statement.setString(3, url);
            statement.setString(4, imgSrc);
            statement.setString(5, title);
            statement.setString(6, phoneNumber);
            statement.setString(7, description);
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
     * Method delete advertisement from the table Advertisements
     */
    public static boolean deleteAdvertisement(int id, int searchId, String url, String imgSrc, String title, String phoneNumber, String description) {

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into ADVERTISEMENTS (olxid, searchId, url, imgSrc,title, phoneNumber, description) values (?,?,?,?,?,?,?)");
            statement.setInt(1, id);
            statement.setInt(2, searchId);
            statement.setString(3, url);
            statement.setString(4, imgSrc);
            statement.setString(5, title);
            statement.setString(6, phoneNumber);
            statement.setString(7, description);
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
     * Method checks new advertisement in table Advertisements
     */
    public static boolean checksAdvertisement(String urlAdvertisement) {
        boolean flag = false;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Advertisements where url=" + "\'" + urlAdvertisement + "\'");

            if (rs.next()) {
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
     * Method gets last 20 advertisements from the table Advertisements
     */
    public static String getLastTwentyAdvertisements() {
        StringBuffer advertisements = new StringBuffer();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select max(id) from Advertisements");

            while (resultSet.next()) {
                int advId = resultSet.getInt(1);

                for (int i = 0; i < 20; i++) {
                    advertisements.append(PostgresDB.getAdvertisementById(advId));
                        advId--;

                }
            }
            statement.close();
            resultSet.close();
            closeConnection(connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return advertisements.toString();

    }

    /**
     * Method gets  advertisement from the table Advertisements by id of advertisement
     */
    public static Advertisement getAdvertisementById(int id) {
        Advertisement advertisement = new Advertisement();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Advertisements where id=" + "\'" + id + "\'");

            if (rs.next()) {
                advertisement.setId(String.valueOf(id));
                advertisement.setUrl(rs.getString(4));
                advertisement.setImageSrc(rs.getString(5));
                advertisement.setTitle(rs.getString(6));
                advertisement.setDescription(rs.getString(8));

            }
            statement.close();
            rs.close();
            closeConnection(connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return advertisement;

    }

    /**
     * Method get arrayList of SearchUrls from table SEARCHLIST
     */
    public static HashMap<String, String> getSearchUrls() {
        HashMap<String, String> urlMap = new HashMap<String, String>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select Id, URL from Searchlist");
            while (rs.next()) {
                urlMap.put(rs.getString("id"), rs.getString("url"));
            }
            statement.close();
            rs.close();
            closeConnection(connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return urlMap;
    }


    /**
     * Method get arrayList of Devicestoken of users from table Accounts, who subscribes on Push notification
     */
    public static ArrayList<String> getDevicesToken() {
        ArrayList<String> devicesToken = new ArrayList<String>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select devicetoken from Accounts where push='t'");
            while (rs.next()) {
                devicesToken.add(rs.getString("devicetoken"));
            }
            statement.close();
            rs.close();
            closeConnection(connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return devicesToken;
    }


}
