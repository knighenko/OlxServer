package server;

import entity.Advertisement;
import jdbc.PostgresDB;
import pushy.SendPush;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FillingBaseThread extends Thread {
    public void run() {
        while (true) {
            long sec = Calendar.getInstance().getTimeInMillis();

            HashMap<String, String> searchUrls = PostgresDB.getSearchUrls();
            for (Map.Entry<String, String> pair : searchUrls.entrySet()) {
                String json = Process.getJsonFromUrl(pair.getValue());
                ArrayList<Advertisement> advertisements = Process.getAdvertisements(json);
                for (Advertisement advertisement : advertisements) {
                    String urlAdv = advertisement.getUrl();
                    if (!PostgresDB.checksAdvertisement(urlAdv)) {
                        PostgresDB.addAdvertisement(advertisement.getId(), Integer.parseInt(pair.getKey()), urlAdv, advertisement.getTitle(), "+380685654215", advertisement.getDescription());
                        /****************DELETE***********************/
                        SendPush.sendPush(advertisement.getTitle(), advertisement.getUrl(), "2e4f84e963d35d05eeb354");
                        /*********************************************/
                    }
                }
            }
            try {
                Thread.sleep(1000);
                System.out.println("FillingBaseThread. Time is:" + ((sec - Calendar.getInstance().getTimeInMillis())/1000)+" sec");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
