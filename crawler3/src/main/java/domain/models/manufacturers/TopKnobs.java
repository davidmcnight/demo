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
import helpers.ElementPatterns;
import helpers.StringManipulation;
import org.apache.poi.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class TopKnobs extends Manufacturer implements ICrawler {


    public int manufacturer_id = 12;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.topknobs.com/products/knobs-pulls/tk827bsn-lily-pull-12-inch-c-c.html");
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
            if(href.contains("www.topknobs.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

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

            if(!document.select(".product-view").isEmpty()) {

                //product
                Product product = new Product();

               // product.setModelNumber(ElementPatterns.grabElementText() d.select(".sku"));




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
