import entity.Advertisement;
import entity.SiteReader;
import jdbc.PostgresDB;
import pushy.SendPush;
import server.FillingBaseThread;
import server.Process;
import server.ServerClientDialog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Main class*/
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {


        /**Search new advertisements and send to the users for each url has one Tread*/

        HashMap<String, String> searchUrlsKharkiv = PostgresDB.getSearchUrls();
        for (Map.Entry<String, String> pair : searchUrlsKharkiv.entrySet()) {

            new FillingBaseThread(pair.getKey(), pair.getValue()).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        HashMap<String, String> searchUrlsKyiv = PostgresDB.getSearchUrls();
        for (Map.Entry<String, String> pair : searchUrlsKyiv.entrySet()) {

            new FillingBaseThread(pair.getKey(), pair.getValue()).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        ExecutorService executorService = Executors.newFixedThreadPool(50);

        try {
            ServerSocket server = new ServerSocket(8089);
            while (!server.isClosed()) {
                Socket client = server.accept();
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
