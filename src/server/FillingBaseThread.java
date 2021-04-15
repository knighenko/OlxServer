package server;

import entity.Advertisement;
import jdbc.PostgresDB;
import pushy.SendPush;

import java.util.*;

public class FillingBaseThread extends Thread {
    private String url;
    private String key;
    private Random rnd = new Random();

    public FillingBaseThread(String key, String url) {
        this.url = url;
        this.key = key;
    }

    /**
     * Method run
     */
    public void run() {


        while (true) {
            /* ------------------------------------------*/
            long sec = Calendar.getInstance().getTimeInMillis();
            int number = rnd.nextInt(6) + 10;
            if (Calendar.getInstance().getTime().getHours() > 5 && Calendar.getInstance().getTime().getHours() < 21) {
                try {
                    Thread.sleep(number * 1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(1800200 + number);
                    /* ------------------------------------------*/
                    System.out.println("Current thread is: " + currentThread().getId() + ". Time is:" + ((sec - Calendar.getInstance().getTimeInMillis()) / 1000) + " sec");
                    /* -----------------------------------------------*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /* ------------------------------------------*/
            System.out.println("Current thread is: " + currentThread().getId() + ". Time is: " + Calendar.getInstance().getTime() + " Stopping " + ((sec - Calendar.getInstance().getTimeInMillis()) / 1000) + " sec");
            /* -----------------------------------------------*/
            /* -----------------------------------------------*/
            String json = Process.getJsonFromUrl(url);
            if (json != null) {
                ArrayList<Advertisement> advertisements = Process.getAdvertisements(json);
                for (Advertisement advertisement : advertisements) {
                    String urlAdv = advertisement.getUrl();
                    if (!PostgresDB.checksAdvertisement(urlAdv)) {
                        PostgresDB.addAdvertisement(Integer.parseInt(advertisement.getId()), Integer.parseInt(key), urlAdv, advertisement.getImageSrc(), advertisement.getTitle(), "+380685654215", advertisement.getDescription());
                        /****************Send advertisement to client***********************/
                        for (String deviceToken : PostgresDB.getDevicesToken())
                            SendPush.sendPush(advertisement.getTitle(), advertisement.getUrl(), advertisement.getImageSrc(), advertisement.getDescription(), deviceToken);
                        /*********************************************/
                    }
                }
            }

        }

    }
}
