package helpers;

import org.jsoup.select.Elements;

import javax.xml.bind.Element;

public class ElementPatterns {


    public static String grabElementText(Elements elements){
        if(!elements.isEmpty()){
            return elements.first().text().trim();
        }
        return "";
    }

    public static String grabMetaContentUrl(Elements elements){
        if(!elements.isEmpty()){
            return elements.first().attr("abs:content").trim();
        }
        return "";
    }
    public static String grabMetaContent(Elements elements){
        if(!elements.isEmpty()){
            return elements.first().attr("content").trim();
        }
        return "";
    }



}
