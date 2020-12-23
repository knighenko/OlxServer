package server;

import entity.SiteReader;
import jdbc.PostgresDB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerClientDialog implements Runnable {
    private final Socket client;
    private static final Logger log = Logger.getLogger(ServerClientDialog.class.getName());
    private String response;
    private FileHandler fh;

    public ServerClientDialog(Socket client) {
        this.client = client;
    }

    /**
     * This method gets request from clent and sends responce to him
     */
    @Override
    public void run() {


        try {
            fh = new FileHandler("MyLogFile.log");
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            log.log(Level.INFO, "Client connected. Time is:" + Calendar.getInstance().getTime());
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
            String requestUrl = inputStream.readUTF();
            log.log(Level.INFO, "Read from client message: " + requestUrl);
            outputStream.writeUTF(getResponse(requestUrl));

            outputStream.flush();
            inputStream.close();
            outputStream.close();
            client.close();
            log.log(Level.INFO, "Closing operation! Client was disconnected! Time is: " + Calendar.getInstance().getTime());
            PostgresDB.connection();
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * This method return Json string from URL
     */
    private String getResponse(String Url) {
        SiteReader siteReader = new SiteReader(Url);
        response = siteReader.getJsonString();
        //  System.out.println("Response is "+ response);
        return response;
    }
}
