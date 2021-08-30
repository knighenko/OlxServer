package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Advertisement;
import entity.SiteReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {


    /**
     * This method return Json string from URL - OLX url
     */
    public static String getJsonFromUrl(String Url) {
        SiteReader siteReader = new SiteReader(Url);
        String response = siteReader.getJsonString();
        //   System.out.println("Response is "+ response);
        return response;
    }


    /**
     * This method returns ArrayList of Advertisements from from String getJsonFromUrl(String Url)
     */
    public static ArrayList<Advertisement> getAdvertisements(String jsonString) {
        ArrayList<Advertisement> advertisements = new ArrayList<>();

        String regEx = "\\{[^{}]*\\}";
        Pattern logEntry = Pattern.compile(regEx);
        Matcher matchPattern = logEntry.matcher(jsonString.replaceAll("\\s+", " "));
        while (matchPattern.find()) {
            ObjectMapper mapper = new ObjectMapper();
            Advertisement adv = null;
            try {
               // System.out.println(matchPattern.group());
                adv = mapper.readValue(matchPattern.group(), Advertisement.class);
                advertisements.add(adv);
               // System.out.println("//////////////="+adv);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return advertisements;
    }
}
