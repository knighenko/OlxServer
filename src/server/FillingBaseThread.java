package server;

import entity.Advertisement;
import jdbc.PostgresDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FillingBaseThread extends Thread{
    public void  run() {
        while (true) {

            System.out.println("FillingBaseThread. Time is:" + Calendar.getInstance().getTime());
            HashMap<String, String> searchUrls = PostgresDB.getSearchUrls();
            for (Map.Entry<String, String> pair : searchUrls.entrySet()) {
                String json = Process.getJsonFromUrl(pair.getValue());
                ArrayList<Advertisement> advertisements = Process.getAdvertisements(json);
                for (Advertisement advertisement : advertisements) {
                    int idAdv = advertisement.getId();
                    if (!PostgresDB.checksAdvertisement(idAdv)) {
                        PostgresDB.addAdvertisement(idAdv, Integer.parseInt(pair.getKey()), advertisement.getUrl(), advertisement.getTitle(), "+380685654215");
                    }
                }
            }
        }
    }
}
