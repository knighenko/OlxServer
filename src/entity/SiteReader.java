package entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import jdbc.PostgresDB;

import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;
/**Class for site parsing*/
public class SiteReader {
    private final String newUrl;
    private int length;
    private static final Logger log = Logger.getLogger(SiteReader.class.getName());
    private String jsonString;

    /**
     * Конструктор класса с инициализацией и ЮРЛ и обьекта в виде строки Json
     */
    public SiteReader(String url) {

        /* This code for Kharkov olx parser
        if (Integer.parseInt(url.substring(url.length() - 1)) == 1) {
            this.newUrl = url.substring(0, url.length() - 1);
            length = 30;
        } else {
            this.newUrl = url;
            length = 3;
        }
        */

 /* This code for Kyiv olx parser*/
            this.newUrl = url;
            length = 3;


        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            HtmlPage page = client.getPage(newUrl);
            List<HtmlElement> items = page.getByXPath("//table[@class='fixed offers breakword redesigned'] //div[@class='offer-wrapper']");
            if (items.isEmpty()) {
                System.out.println("No items found!");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                StringWriter writer = new StringWriter();

                // for (HtmlElement item : items) {
                for (int i = 0; i < items.size() & i < length; i++) {
                    HtmlElement item = items.get(i);
                    // System.out.println(item.asXml());
                    /*Check does have an imgSrc?*/
                    HtmlImage element = item.getFirstByXPath(".//table/tbody/tr[1]/td[1]/a/img");
                    DomAttr idAdv = item.getFirstByXPath(".//table[@data-id]/@data-id");
                    HtmlAnchor anchor = item.getFirstByXPath(".//table/tbody/tr[1]/td[2]/div/h3/a");
                   //  System.out.println("----------------------"+anchor+"----------------------------------------------------------------------");
                    HtmlPage advPage = client.getPage(anchor.getHrefAttribute());
                   // System.out.println(advPage.asXml());
                   // HtmlElement advItemDescription = advPage.getHtmlElementById("css-g5mtbi-Text");
                     HtmlDivision advItemDescription = advPage.getFirstByXPath("//div[@class='css-g5mtbi-Text']");
                    //System.out.println("Description "+advItemDescription.asXml());
                    Advertisement adv = new Advertisement();
                    if ((advItemDescription.getTextContent() != null)||(advItemDescription!=null)) {
                        adv.setDescription(advItemDescription.getTextContent().trim());
                    }


                    if (element != null) {

                        adv.setId(idAdv.getValue());
                        adv.setTitle(anchor.getTextContent().trim());
                        adv.setUrl(anchor.getHrefAttribute());
                        adv.setImageSrc(element.getSrcAttribute());
                        mapper.writeValue(writer, adv);


                    } else {

                        adv.setId(idAdv.getValue());
                        adv.setTitle(anchor.getTextContent().trim());
                        adv.setUrl(anchor.getHrefAttribute());
                        adv.setImageSrc("No Img");
                        mapper.writeValue(writer, adv);

                    }


                }
                jsonString = writer.toString();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method gets Json string
     */
    public String getJsonString() {
        return jsonString;
    }


}