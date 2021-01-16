import entity.Advertisement;
import entity.SiteReader;
import jdbc.PostgresDB;
import server.Process;
import server.ServerClientDialog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        /*Tests connection--------------------------------------------*/

        HashMap<String, String> searchUrls = PostgresDB.getSearchUrls();
        for (Map.Entry<String, String> pair : searchUrls.entrySet()) {
            String json = Process.getJsonFromUrl(pair.getValue());
            ArrayList<Advertisement> advertisements = Process.getAdvertisements(json);
            for (Advertisement advertisement : advertisements) {
                    int idAdv=advertisement.getId();
                                  if (!PostgresDB.checksAdvertisement(idAdv)) {
                    PostgresDB.addAdvertisement(idAdv, Integer.parseInt(pair.getKey()), advertisement.getUrl(), advertisement.getTitle(), "+380685654215");
                }
            }
        }
        SiteReader reader=new SiteReader("https://www.olx.ua/uk/dom-i-sad/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15");
        reader.getJsonString();
        /* ---------------------------------------------------------------*/

        ExecutorService executorService = Executors.newFixedThreadPool(50);

        try {
            ServerSocket server = new ServerSocket(8080);
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
