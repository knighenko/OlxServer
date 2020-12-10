package entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.StringWriter;
import java.util.List;

public class SiteReader {
    private final String newUrl;
    private int length;

    private String jsonString;

    /**
     * Конструктор класса с инициализацией и ЮРЛ и обьекта в виде строки Json
     */
    public SiteReader(String url) {
        if (Integer.parseInt(url.substring(url.length() - 1)) == 1) {
            this.newUrl = url.substring(0, url.length() - 1);
            length = 30;
        } else {
            this.newUrl = url;
            length = 3;
        } ;
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            HtmlPage page = client.getPage(newUrl);
            List<HtmlElement> items = page.getByXPath("//div[@class='offer-wrapper']");
            if (items.isEmpty()) {
                System.out.println("No items found!");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                StringWriter writer = new StringWriter();

                // for (HtmlElement item : items) {
                for (int i = 0; i < items.size() & i < length; i++) {
                    HtmlElement item = items.get(i);
                    // System.out.println(item.asXml());

                    HtmlImage element = item.getFirstByXPath(".//table/tbody/tr[1]/td[1]/a/img");
                    HtmlAnchor anchor = item.getFirstByXPath(".//table/tbody/tr[1]/td[2]/div/h3/a");
                    HtmlPage advPage = client.getPage(anchor.getHrefAttribute());
                    HtmlElement advItem = advPage.getHtmlElementById("textContent");

                    if (element != null) {
                        Advertisement adv = new Advertisement();
                        adv.setTitle(anchor.getTextContent().trim());
                        adv.setUrl(anchor.getHrefAttribute());
                        adv.setImageSrc(element.getSrcAttribute());
                        adv.setDescription(advItem.getTextContent().trim());
                        mapper.writeValue(writer, adv);


                    } else {
                        Advertisement adv = new Advertisement();
                        adv.setTitle(anchor.getTextContent().trim());
                        adv.setUrl(anchor.getHrefAttribute());
                        adv.setImageSrc("No Img");
                        adv.setDescription(advItem.getTextContent().trim());
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