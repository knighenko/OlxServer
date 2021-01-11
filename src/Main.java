import jdbc.PostgresDB;
import server.ServerClientDialog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
         /*Tests connection--------------------------------------------*/
        Connection connection=PostgresDB.getConnection();
        try {
            PostgresDB.closeConnection(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        /*---------------------------------------------------------------*/

        ExecutorService executorService = Executors.newFixedThreadPool(50);

        try {
            ServerSocket server = new ServerSocket(8080);
            while (!server.isClosed()){
                Socket client=server.accept();
                executorService.execute(new ServerClientDialog(client));
                log.log(Level.INFO, "Execute ServerSocket");
            }
            log.log(Level.INFO, "Done execute ServerSocket");
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }



    }
}
