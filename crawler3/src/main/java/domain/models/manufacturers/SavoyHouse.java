package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class SavoyHouse extends Manufacturer implements ICrawler {

    public int manufacturer_id = 55;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.savoyhouse.com/1-9260-6-112-carrolton-6-light-chandelier-in-weathered-birch");
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
            if(href.contains("www.savoyhouse.com")){
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


            //System.out.println("VISITING: " + url);

            if(!document.select(".product-info-stock-sku").isEmpty()) {

                //product
                Product product = new Product();

                /* Data will be mostly used on the price sheet*/

                product.setUrl(document.location());
                System.out.println("THIS IS A PRODUCT PAGE");
                System.out.println(document.location());

                product.setCategory1("Lighting");

                //name
                product.setName(document.select("meta[property=og:title]")
                        .first().attr("content"));

                product.setImageLink(document.select("meta[property=og:image]")
                        .first().attr("abs:content"));

                if(!document.select("meta[property=product:price:amount]").isEmpty()){
                    product.setListPrice(document.select("meta[property=product:price:amount]")
                            .first().attr("content"));

                }

                //model number
                product.setModelNumber(document.select(".sku .value")
                        .first().ownText());

                product.setUpc(document.select(".upc .value")
                        .first().ownText());

                Elements finish = document.select(".swatch-attribute-selected-option");
                if(!finish.isEmpty()){
                    product.setFinishes(finish.first().text());
                    product.setName(product.getName() + " - " + finish.first().text());
                }

                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;
                System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
                SpecBooksController.PAGE_COUNTER++;

                ProductService.createProduct(product, this.manufacturer_id);
                if (SpecBooksController.PRODUCT_COUNTER > 10000000) {
                    System.exit(0);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
