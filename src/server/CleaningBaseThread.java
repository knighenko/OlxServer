package server;

import jdbc.PostgresDB;
/**Сlass for CleaningBaseThread*/
public class CleaningBaseThread extends Thread {
    private String url;

    public CleaningBaseThread(String url) {
        this.url = url;

    }

    @Override
    public void run() {
        PostgresDB.cleanTableAdvertisements();
    }
}
