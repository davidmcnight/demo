package domain.models.manufacturers;

import com.sleepycat.utilint.StringUtils;
import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
import org.apache.poi.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;

public class Baldwin extends Manufacturer implements ICrawler {


    public int manufacturer_id = 13;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("http://www.seagulllighting.com/57766/One-Light-Wall-/-Bath-Sconce-4134501EN-848.html");
        return seeds;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if(href.contains("plus.google.com")
                || href.contains("myFoldersNotLoggedIn")
                || href.contains("product.language")
                || href.contains("facebook")
                || href.contains("designservice")
                || href.contains("smartbim")
                || href.contains("youtube")

        ){
            return false;
        }else {
            if(href.contains("seagulllighting.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }

    @Override
    public void onStart() {

        try {

            //get the home page
            Document document = Jsoup.connect("https://www.baldwinhardware.com/").userAgent(Config.USER_AGENT).followRedirects(true).
                    timeout(0).
                    get();

            //add root category pages
            Elements baseCategoryLinks = document.select("#MainNav_T5408FA6E041_liProducts .menu-dropdown-links").first().select("a");
            ArrayList <String> baseList = new ArrayList<String>();
            for (Element baseCategoryLink: baseCategoryLinks ){
                if(!baseList.contains(baseCategoryLink.attr("abs:href"))){
                    baseList.add(baseCategoryLink.attr("abs:href"));
                }
            }

            //setup properties
            System.setProperty("webdriver.chrome.driver","/Users/specbooks/Tools/chromedriver");
            WebDriver webDriver = new ChromeDriver();

            //loop through base category -- download page to get links
            for(String base: baseList){
                //get page
                webDriver.get(base);
                System.out.println("Downloading Page");
                System.out.println(base);
                Thread.sleep(20000);
                String pageSource = webDriver.getPageSource();
                webDriver.close();
                Document subDocument = Jsoup.parse(pageSource);
                Elements subCategoryLinks = subDocument.select("#ng-app-categorylist a");
                for (Element subCategoryLink: subCategoryLinks ){
                    System.out.println(subCategoryLink.text());
                }
            }



            System.exit(0);
            System.exit(0);
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void visit(Page page) {
        try {

            Document document = Jsoup.connect(page.getWebURL().getURL()).userAgent(Config.USER_AGENT).followRedirects(true).
                    timeout(0).
                    get();;
            String url = document.location();

            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(!document.select(".maincontentProduct").isEmpty()) {

                //product
                Product product = new Product();


//                ProductService.createProduct(product, this.manufacturer_id);

                System.out.println("PRODUCT...............");
                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                System.out.println("_________________________________________");
                SpecBooksController.PRODUCT_COUNTER++;
                if (SpecBooksController.PRODUCT_COUNTER > 10000000) {
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
