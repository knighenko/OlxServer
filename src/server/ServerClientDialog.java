package server;

import entity.SiteReader;
import jdbc.PostgresDB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerClientDialog implements Runnable {
    private final Socket client;
    private static final Logger log = Logger.getLogger(ServerClientDialog.class.getName());

    private FileHandler fh;

    public ServerClientDialog(Socket client) {
        this.client = client;
    }

    /**
     * This method gets request from client and sends responce to him
     */
    @Override
    public void run() {


        try {

            log.log(Level.INFO, "Client connected. Time is:" + Calendar.getInstance().getTime());
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
            String requestAndroid = inputStream.readUTF();
            log.log(Level.INFO, "Read from client message: " + requestAndroid);
            outputStream.writeUTF(checkCommand(requestAndroid));
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            client.close();
            log.log(Level.INFO, "Closing operation! Client was disconnected! Time is: " + Calendar.getInstance().getTime());

        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * This method checks what command was sent from Android
     */
    public String checkCommand(String requestAndroid) {
        String responseToAndroid = "false";
        String[] array = requestAndroid.split(":");
        String retval = array[0];
        switch (retval) {
            case "1":
                try{
                return String.valueOf(PostgresDB.checkUser(array[1], array[2]));}
                catch (Exception e){
                    return "false";
                }
            case "2":
                break;
        }
        return responseToAndroid;
    }



}
