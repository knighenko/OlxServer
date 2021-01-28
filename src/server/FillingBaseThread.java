package server;

import entity.Advertisement;
import jdbc.PostgresDB;
import pushy.SendPush;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FillingBaseThread extends Thread {
    private String url;
    private String key;

    public FillingBaseThread(String key, String url) {
        this.url = url;
        this.key = key;
    }

    public void run() {
        while (true) {
            /* ------------------------------------------*/
            long sec = Calendar.getInstance().getTimeInMillis();
            /* -----------------------------------------------*/
            String json = Process.getJsonFromUrl(url);
            if (json != null) {
                ArrayList<Advertisement> advertisements = Process.getAdvertisements(json);
                for (Advertisement advertisement : advertisements) {
                    String urlAdv = advertisement.getUrl();
                    if (!PostgresDB.checksAdvertisement(urlAdv)) {
                        PostgresDB.addAdvertisement(advertisement.getId(), Integer.parseInt(key), urlAdv, advertisement.getImageSrc(), advertisement.getTitle(), "+380685654215", advertisement.getDescription());
                        /****************Send advertisement to client***********************/
                        for (String deviceToken : PostgresDB.getDevicesToken())
                            SendPush.sendPush(advertisement.getTitle(), advertisement.getUrl(), advertisement.getImageSrc(), advertisement.getDescription(), deviceToken);
                        /*********************************************/
                    }
                }
            }
            try {
                Thread.sleep(12000);
                /* ------------------------------------------*/
                System.out.println("Current thread is: "+currentThread().getId()+". Time is:" + ((sec - Calendar.getInstance().getTimeInMillis()) / 1000) + " sec");
                /* -----------------------------------------------*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
