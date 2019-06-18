package domain.models.manufacturers;

import config.Config;
import controllers.SpecBooksController;
import domain.models.ICrawler;
import domain.models.Manufacturer;
import domain.models.Product;
import domain.services.ProductService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import helpers.StringManipulation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Elegant extends Manufacturer implements ICrawler {

    public int manufacturer_id = 61;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.elegantlighting.com/product/10-lights-2032-maxime-collection/");
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
                || href.contains("pinterest")
                || href.contains("twitter")
        ){
            return false;
        }else {
            if(href.contains("www.elegantlighting.com")){
                return super.shouldVisit(referringPage, url);
            }
        }
        return false;

    }


    @Override
    public void visit(Page page) {
        try {


            Document document = Jsoup.connect(page.getWebURL().getURL()).followRedirects(true).
                    timeout(0).
                    get();;
            String url = document.location();


            if(!document.select(".woocommerce-product-details__short-description").isEmpty()){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");

                //cate2
                Elements cat2 = document.select(".posted_in p_meta");
                if(!cat2.isEmpty()){
                    for(Element c2 : cat2){
                        if(c2.text().toLowerCase().contains("category")){
                            product.setCategory2(c2.select("a").first().text().trim());
                        }
                    }
                }



                Elements sku = document.select(".sku");
                if(!sku.isEmpty()){
                    product.setModelNumber(sku.first().text());
                }

                product.setName(document.select(".woocommerce-product-details__short-description").first().text());


                product.setImageLink(document.select(".woocommerce-product-gallery__image img")
                        .first().attr("abs:src"));

                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);


            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 100000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
