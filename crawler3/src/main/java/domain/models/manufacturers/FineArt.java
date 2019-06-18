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

public class FineArt extends Manufacturer implements ICrawler {

    public int manufacturer_id = 57;

    public ArrayList<String> getSeeds() {
        ArrayList<String> seeds =  new ArrayList<String>();
        seeds.add("https://www.fineartlamps.com/AMidsummerNightsDream_427150ST");

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
            if(href.contains("fineartlamps.com")){
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

            if(!document.select("#ProductImagesWpr").isEmpty()){

                //product
                Product product = new Product();
                product.setUrl(document.location());
                product.setCategory1("Lighting");

                //get model number
                product.setModelNumber(document.title().split(" ")[0].trim());
                product.setName("");
                product.setCollection(StringManipulation.capitalCase(document.select("h1").first().text().replace(product.getModelNumber(), "").trim().toLowerCase()));
                product.setImageLink(document.select("#ProductImagesWpr img").first().attr("abs:src"));
                product.setDescription(document.select("#ProductInfo").first().ownText());


                System.out.println(String.valueOf(SpecBooksController.PRODUCT_COUNTER) + ") " + product);
                SpecBooksController.PRODUCT_COUNTER++;
                ProductService.createProduct(product, this.manufacturer_id);

            }


            System.out.println(String.valueOf(SpecBooksController.PAGE_COUNTER) + ") " + document.location());
            SpecBooksController.PAGE_COUNTER++;

            if(SpecBooksController.PRODUCT_COUNTER > 1000000000){
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
