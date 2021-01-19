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
                if(json!=null) {
                    ArrayList<Advertisement> advertisements = Process.getAdvertisements(json);
                    for (Advertisement advertisement : advertisements) {
                        String urlAdv = advertisement.getUrl();
                        if (!PostgresDB.checksAdvertisement(urlAdv)) {
                            PostgresDB.addAdvertisement(advertisement.getId(), Integer.parseInt(pair.getKey()), urlAdv, advertisement.getTitle(), "+380685654215", advertisement.getDescription());
                            /****************Send advertisement to client***********************/
                            for (String deviceToken:   PostgresDB.getDevicesToken())
                            SendPush.sendPush(advertisement.getTitle(), advertisement.getUrl(), deviceToken);
                            /*********************************************/
                        }
                    }
                }
            }

           /*
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           */
            System.out.println("FillingBaseThread. Time is:" + ((sec - Calendar.getInstance().getTimeInMillis())/1000)+" sec");
        }
    }
}
